package com.hudissonxavier.avisame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração global de CORS da aplicação.
 * Permite que o frontend (ex: Angular em localhost:4200)
 * consuma a API sem bloqueios do navegador.
 * @Configuration: Indica que esta classe contém definições de configurações do Spring.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer{

    /**
     * Configura as regras de CORS (Cross-Origin Resource Sharing).
     * O CORS é um mecanismo de segurança que define quem pode acessar sua API 
     * a partir de um domínio diferente (ex: o front-end acessando o back-end).
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        // Define que estas regras de acesso valem para TODAS as rotas da API ("/**")
        registry.addMapping("/**")

        /**
         * Lista branca de endereços (origens) que têm permissão para acessar a API
         * Aqui você deve colocar a URL oficial do seu site e o endereço de teste local
         * ou usar allowedOrigins("*") para permitir acesso de qualquer endereço web
         */
            .allowedOrigins("*")

            // Especifica quais métodos (verbos HTTP) os sites acima podem executar.
            // Métodos não listados aqui serão bloqueados pelo navegador.
            .allowedMethods("GET","POST","PUT","DELETE")

            .allowedHeaders("*"); // Importante para o Bearer Token
    }
}
