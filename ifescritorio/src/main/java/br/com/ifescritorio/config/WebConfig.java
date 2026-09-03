package br.com.ifescritorio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.ifescritorio.util.Util;

@Configuration
public class WebConfig
        implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {

        registry.addResourceHandler(
                "/imagens/**"
        )
        .addResourceLocations(
                "file:"
                + Util.LOCAL_ARMAZENAMENTO_IMAGENS
        );

        registry.addResourceHandler(
                "/qrcodes/**"
        )
        .addResourceLocations(
                "file:"
                + Util.LOCAL_ARMAZENAMENTO_IMAGENS
                + "qrcodes/"
        );
    }
}