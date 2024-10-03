package org.example.demoproductservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.ZonedDateTime;
import java.util.Optional;

@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@Configuration
public class JpaConfig {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
}
