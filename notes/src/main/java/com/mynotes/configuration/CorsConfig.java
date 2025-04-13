//package com.mynotes.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.List;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOrigins(List.of("https://my-notes-3cdx.onrender.com/","http://localhost:5173/"));
//        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        corsConfig.setExposedHeaders(List.of("Authorization"));
//        corsConfig.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//        return new CorsFilter(source);
//    }
//}
