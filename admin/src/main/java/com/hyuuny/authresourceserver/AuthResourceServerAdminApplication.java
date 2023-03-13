package com.hyuuny.authresourceserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AuthResourceServerAdminApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-core.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthResourceServerAdminApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
