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
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

    public static final String UPLOAD_FOLDER = "uploads";
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public List<Map<String, Object>> listUploads(HttpServletRequest req) throws IOException {
        return Files.walk(Paths.get(UPLOAD_FOLDER))
                .filter(path -> !path.equals(Paths.get(UPLOAD_FOLDER)))
                .map(path -> pathToMap(path))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {
                String newFilename = UUID.randomUUID().toString() + "."
                        +file.getOriginalFilename()
                                .substring(file.getOriginalFilename().lastIndexOf('.')+1);
                Files.copy(file.getInputStream(), Paths.get(UPLOAD_FOLDER, newFilename));
                LOGGER.info("You successfully uploaded {} to {}", file.getOriginalFilename(), newFilename);
            } catch (IOException | RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            LOGGER.error("File is empty");
        }

        return "redirect:/";
    }
    
    private Map<String, Object> pathToMap(Path p) {
        try {
            Map<String, Object> hasil = new LinkedHashMap<>();
            hasil.put("url", p.toString());
            hasil.put("path", p.toAbsolutePath());
            hasil.put("size", Files.size(p));
            return hasil;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(UploadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
