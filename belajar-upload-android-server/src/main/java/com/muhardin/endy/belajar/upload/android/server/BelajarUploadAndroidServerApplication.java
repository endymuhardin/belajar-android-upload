package com.muhardin.endy.belajar.upload.android.server;

import com.muhardin.endy.belajar.upload.android.server.controller.UploadController;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.FileSystemUtils;

@SpringBootApplication
public class BelajarUploadAndroidServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BelajarUploadAndroidServerApplication.class, args);
    }
    
    @Bean
    CommandLineRunner init(){
        return (args) -> { 
            FileSystemUtils.deleteRecursively(new File(UploadController.UPLOAD_FOLDER));
            Files.createDirectory(Paths.get(UploadController.UPLOAD_FOLDER));
        };
    }
}
