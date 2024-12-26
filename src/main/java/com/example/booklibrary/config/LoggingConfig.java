package com.example.booklibrary.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Precorrelation;

import java.io.IOException;

@Configuration
public class LoggingConfig {

    @Bean
    public HttpLogWriter httpLogWriter() {
        return new InfoLevelHttpLogWriter();
    }

    static class InfoLevelHttpLogWriter implements HttpLogWriter {

        private final Logger log = LoggerFactory.getLogger(Logbook.class);

        @Override
        public boolean isActive() {
            return log.isInfoEnabled();
        }

        @Override
        public void write(Precorrelation precorrelation, String request) throws IOException {
            log.info(request);
        }

        @Override
        public void write(Correlation correlation, String response) throws IOException {
            log.info(response);
        }
    }

}
