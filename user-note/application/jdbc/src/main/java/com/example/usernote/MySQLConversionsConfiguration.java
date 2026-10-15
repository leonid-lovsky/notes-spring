package com.example.usernote;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;

import java.util.List;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@Profile("mysql")
class MySQLConversionsConfiguration {

    @Bean
    JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(new UUIDToStringConverter(), new StringToUUIDConverter()));
    }

    @WritingConverter
    static class UUIDToStringConverter implements Converter<UUID, String> {

        @Override
        public String convert(UUID source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToUUIDConverter implements Converter<String, UUID> {

        @Override
        public UUID convert(String source) {
            return UUID.fromString(source);
        }
    }
}
