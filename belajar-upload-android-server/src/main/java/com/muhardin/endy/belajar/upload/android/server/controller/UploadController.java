package com.muhardin.endy.belajar.upload.android.server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {
    public static final String UPLOAD_FOLDER = "uploads";
    
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public List<Path> listUploads(HttpServletRequest req) throws IOException {
        return Files.walk(Paths.get(UPLOAD_FOLDER))
                .filter(path -> !path.equals(Paths.get(UPLOAD_FOLDER)))
                .map(path -> Paths.get(UPLOAD_FOLDER).relativize(path))
                .collect(Collectors.toList());
    }
}
