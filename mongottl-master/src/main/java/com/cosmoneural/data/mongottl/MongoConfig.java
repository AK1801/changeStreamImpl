package com.cosmoneural.data.mongottl;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import javax.annotation.Resource;

@Configuration
@EntityScan("com.cosmoneural.data")
public class MongoConfig extends AbstractMongoClientConfiguration {



    @Resource
    private Environment environment;

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("com.tatadigital.product.mongo.dbname");
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(environment.getProperty("com.tatadigital.product.mongo.connection.url"));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName());
    }

 /*   @Override
    protected boolean autoIndexCreation() {
        return true;
    }*/
}
