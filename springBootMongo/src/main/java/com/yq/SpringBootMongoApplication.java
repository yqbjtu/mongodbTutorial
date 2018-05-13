package com.yq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Simple to Introduction
 * className: SpringBootMongoApplication
 *
 * @author EricYang
 * @version 2018/5/13 19:11
 */

@SpringBootApplication
public class SpringBootMongoApplication {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootMongoApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootMongoApplication.class, args);
        logger.info("Done start Spring boot");
    }
}