package com.muhardin.endy.belajar.upload.android.server.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {
    private final String uploadFolder = "/uploads";
    
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public List<Map<String, String>> listUploads(HttpServletRequest req){
        Map<String, String> fileInfo = new LinkedHashMap<>();
        String namaFile = "contoh-file.jpg";
        
        fileInfo.put("nama", namaFile);
        
        String url = req.getContextPath()+uploadFolder+"/"+namaFile;
        fileInfo.put("url", url);
        
        String realPath = req.getServletContext().getRealPath(uploadFolder);
        String path = realPath + File.separator + namaFile;
        fileInfo.put("path", path);
        fileInfo.put("ukuran", "123KB");
        
        List<Map<String, String>> daftarFile = new ArrayList<>();
        daftarFile.add(fileInfo);
        
        return daftarFile;
    }
}
