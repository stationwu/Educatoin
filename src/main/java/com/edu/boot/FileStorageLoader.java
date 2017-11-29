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
            String ddlAuto = environment.getProperty("spring.jpa.hibernate.ddl-auto");
            if (ddlAuto.equals("create-drop") || ddlAuto.equals("create")) {
                service.deleteAll();
            }
            service.init();
        };
    }
}
