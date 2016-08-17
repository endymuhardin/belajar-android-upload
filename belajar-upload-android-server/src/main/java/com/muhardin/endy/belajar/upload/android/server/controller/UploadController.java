package com.muhardin.endy.belajar.upload.android.server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    public static final String UPLOAD_FOLDER = "uploads";
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    private final ResourceLoader resourceLoader;

    @Autowired
    public UploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<Map<String, Object>> listUploads(HttpServletRequest req) throws IOException {
        return Files.walk(Paths.get(UPLOAD_FOLDER))
                .filter(path -> !path.equals(Paths.get(UPLOAD_FOLDER)))
                .map(path -> pathToMap(path, req.getRequestURL().append("uploads/").toString()))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/uploads/{filename:.+}")
    public ResponseEntity<?> getFile(@PathVariable String filename) {

        try {
            return ResponseEntity
                    .ok(resourceLoader.getResource("file:" + Paths.get(UPLOAD_FOLDER, filename).toString()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file,
            HttpServletRequest req) {

        Map<String, Object> hasil = new LinkedHashMap<>();
        if (!file.isEmpty()) {
            try {
                String newFilename = UUID.randomUUID().toString() + "."
                        + file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf('.') + 1);
                Files.copy(file.getInputStream(), Paths.get(UPLOAD_FOLDER, newFilename));
                LOGGER.info("You successfully uploaded {} to {}", file.getOriginalFilename(), newFilename);
                hasil.put("success", true);
                hasil.put("filename", newFilename);
                hasil.put("url", req.getRequestURL().append("uploads/").toString() +newFilename);
            } catch (IOException | RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
                hasil.put("success", false);
                hasil.put("error", e.getMessage());
            }
        } else {
            LOGGER.error("File is empty");
            hasil.put("success", false);
            hasil.put("error", "File is empty");
        }
        return hasil;
    }

    private Map<String, Object> pathToMap(Path p, String contextPath) {
        try {
            Map<String, Object> hasil = new LinkedHashMap<>();
            hasil.put("url", contextPath + p.toString());
            hasil.put("path", p.toAbsolutePath());
            hasil.put("size", Files.size(p));
            return hasil;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(UploadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
