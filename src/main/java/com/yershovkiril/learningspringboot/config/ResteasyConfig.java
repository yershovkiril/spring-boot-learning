package com.yershovkiril.learningspringboot.config;

import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@Configuration
@ApplicationPath("/")
public class ResteasyConfig extends Application {

}
