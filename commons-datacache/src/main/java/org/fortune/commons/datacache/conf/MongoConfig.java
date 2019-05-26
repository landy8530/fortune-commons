package org.fortune.commons.datacache.conf;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

@Configuration(MongoConfig.BEAN_NAME)
@ConditionalOnProperty(value = "data.cache.strategy",havingValue = "mongodb")
public class MongoConfig extends AbstractCacheConfig {
    public  static final String BEAN_NAME = "mongoConfig";
    public  static final String BEAN_NAME_MONGO_TEMPLATE = "mongoTemplate";
    private static final String MONGODB_PREFIX = "mongodb://";

    @Value("${mongodb.database}")
    private String mongoDBName;
    @Value("${mongodb.collectionName}")
    private String mongoCollectionName;

    @Bean("mongoCollection")
    public MongoCollection<Document> mongoCollection(@Qualifier("mongoTemplate") MongoOperations mongoTemplate) {
        return mongoTemplate.getCollection(mongoCollectionName);
    }

    @Bean("mongoTemplate")
    public MongoOperations mongoTemplate(@Qualifier("mongoDbFactory") MongoDbFactory mongoDbFactory) {
        MongoOperations mongoTemplate = new MongoTemplate(mongoDbFactory);
        return mongoTemplate;
    }

    @Bean("mongoDbFactory")
    public MongoDbFactory mongoDbFactory(@Qualifier("mongoClient") MongoClient mongoClient) {
        MongoDbFactory mongoDbFactory = new SimpleMongoClientDbFactory(mongoClient, mongoDBName);
        return mongoDbFactory;
    }

    @Bean("mongoClient")
    public MongoClient mongoClient(@Qualifier("connectionString") final ConnectionString connectionString) {
        MongoClient mongoClient = MongoClients.create(connectionString);
        return mongoClient;
    }

    @Bean("connectionString")
    public ConnectionString connectionString() {
        //https://docs.mongodb.com/manual/reference/connection-string/#connections-connection-examples
        //mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database.collection][?options]]
        //mongodb://sysop:moon@localhost/records
        String connString = MONGODB_PREFIX + super.getHubCacheServer() + ":" +super.getHubCachePort() + "/" + mongoDBName;
        if(super.isAuthFlag()) {
            connString = MONGODB_PREFIX + super.getHubCacheAccount() + ":" + super.getHubCachePassword() +
                    "@" + super.getHubCacheServer() + ":" +super.getHubCachePort() + "/" + mongoDBName;
        }
        ConnectionString connectionString = new ConnectionString(connString);
        return connectionString;
    }

    public String getMongoDBName() {
        return mongoDBName;
    }

    public String getMongoCollectionName() {
        return mongoCollectionName;
    }
}
