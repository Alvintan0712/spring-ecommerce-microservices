package com.example.product_service.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.commands.ServerAddress;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.TransitionWalker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class EmbeddedMongoConfig {

    private static TransitionWalker.ReachedState<RunningMongodProcess> runningInstance = Mongod.instance().start(Version.Main.V8_0_RC);
    private static final ServerAddress serverAddress;

    static {
        serverAddress = runningInstance.current().getServerAddress();
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://" + serverAddress);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "test-db");
    }
}
