package com.yershovkiril.learningspringboot.clientproxy;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;

@Configuration
public class ClientProxyConfig {

    @Value("${users.api.url.v1}")
    private String usersEndpointUrl;

    @Bean
    public UserResourceV1 geUserControllerRestEasyProxy() {
        // Client
        ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();
        // WebTarget
        ResteasyWebTarget target = client.target(usersEndpointUrl);
        // Return Proxy
        UserResourceV1 proxy = target.proxy(UserResourceV1.class);
        return proxy;
    }
}
