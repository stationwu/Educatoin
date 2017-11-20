package com.edu.boot;

import com.edu.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FileStorageLoader {
    @Autowired
    private Environment environment;

    @Bean
    CommandLineRunner init(FileStorageService service) {
        return (args) -> {
            if (environment.getProperty("spring.jpa.hibernate.ddl-auto").equals("create-drop")) {
                service.deleteAll();
            }
            service.init();
        };
    }
}
