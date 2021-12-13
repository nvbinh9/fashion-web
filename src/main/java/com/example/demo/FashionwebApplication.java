package com.example.demo;

import com.example.demo.entity.Order;
import com.example.demo.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@EntityScan(basePackageClasses = { FashionwebApplication.class, Jsr310Converters.class })

public class FashionwebApplication implements CommandLineRunner  {

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(FashionwebApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void run(String... args) throws Exception {
        HashMap<String, AtomicReference> map = new HashMap(){{
            put("Value", new AtomicReference<Map<String, Order>>(new HashMap<>()));
        }};
        System.out.println("Start!!!");
        System.out.println(map.get("Value"));
    }

//    @Override
//    public void run(String... args) throws Exception {
//        redisTemplate.opsForValue().set("nvbinh", "Hello World");
//        System.out.println("Value of key nvbinh" + redisTemplate.opsForValue().get("nvbinh"));
//    }
}
