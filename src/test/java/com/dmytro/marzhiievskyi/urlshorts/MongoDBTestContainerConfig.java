package com.dmytro.marzhiievskyi.urlshorts;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;


public class MongoDBTestContainerConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        Startables.deepStart(mongoDBContainer).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        TestPropertyValues.of(
                "spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
        ).applyTo(context.getEnvironment());
    }

}
