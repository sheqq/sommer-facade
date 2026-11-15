package de.sheqq.sommerfacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;

@SpringBootApplication
public class SommerFacadeApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SommerFacadeApplication.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(SommerFacadeApplication.class, args);
        if (context instanceof WebServerApplicationContext webContext) {
            String baseUrl = context.getEnvironment().getProperty("app.api.base-url");
            if (baseUrl == null || baseUrl.isBlank()) {
                baseUrl = "http://localhost:" + webContext.getWebServer().getPort();
            }
            LOGGER.info("API verfügbar: {}/api/v1/users", baseUrl.replaceAll("/+$", ""));
        }
    }

}
