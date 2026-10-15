package com.example.usernote;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;

import java.util.List;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@Profile("mysql")
class MySQLConversionsConfiguration {

    @Bean
    R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
        return R2dbcCustomConversions.of(
            DialectResolver.getDialect(connectionFactory),
            List.of(new UUIDToStringConverter(), new StringToUUIDConverter())
        );
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
