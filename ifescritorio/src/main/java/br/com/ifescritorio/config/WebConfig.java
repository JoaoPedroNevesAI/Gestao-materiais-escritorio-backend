package br.com.ifescritorio.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        // Toda requisição para /imagens/nome_da_foto.jpg será
        // redirecionada
        // para a pasta C:/ifes-patrimonio/uploads/


        registry.addResourceHandler("/imagens/**")
                .addResourceLocations("file:C:/ifes-patrimonio/uploads/");
        
        registry.addResourceHandler("/qrcodes/**")
        .addResourceLocations("file:C:/ifes-patrimonio/uploads/qrcodes/");
    }
}
