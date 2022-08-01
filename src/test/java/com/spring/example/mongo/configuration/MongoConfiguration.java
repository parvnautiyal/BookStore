package com.spring.example.mongo.configuration;

import com.mongodb.WriteConcern;
import com.spring.example.mongo.repository.BooksRepository;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;

import java.io.IOException;

@Configuration
public class MongoConfiguration {

    static MongodExecutable executable;

    @Bean
    public MongoDatabaseFactory factory() {
        return new SimpleMongoClientDatabaseFactory("mongodb://localhost:27019/imager200_test");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        MongoTemplate template = new MongoTemplate(mongoDbFactory);
        template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return template;
    }

    @Bean
    public MongoRepositoryFactoryBean mongoFactoryRepositoryBean(MongoTemplate template) {
        MongoRepositoryFactoryBean mongoDbFactoryBean = new MongoRepositoryFactoryBean(BooksRepository.class);
        mongoDbFactoryBean.setMongoOperations(template);
        return mongoDbFactoryBean;
    }

    public static void configureAndStartEmbeddedMongoDB() throws IOException {
        int port = 27019;

        MongodStarter starter = MongodStarter.getDefaultInstance();

        MongodConfig mongodConfig = MongodConfig.builder().version(Version.Main.V4_0)
                .net(new Net(port, Network.localhostIsIPv6())).build();
        executable = starter.prepare(mongodConfig);
        executable.start();
    }

    public static void stopAll() {
        executable.stop();
    }
}