package com.edu.boot;

import com.edu.storage.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FileStorageLoader {
    @Bean
    CommandLineRunner init(FileStorageService service) {
        return (args) -> {
            service.init();
        };
    }
}
