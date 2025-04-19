package com.mynotes.configuration;

import com.mynotes.DTO.PostDTO;
import com.mynotes.entities.Post;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Post.class, PostDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getUsername(), PostDTO::setUsername);
            mapper.map(src -> src.getUser().getId(), PostDTO::setUserid);
            mapper.map(src -> src.getUser().getAvatar(), PostDTO::setAvatar);
        });
        return  modelMapper;
    }
}
