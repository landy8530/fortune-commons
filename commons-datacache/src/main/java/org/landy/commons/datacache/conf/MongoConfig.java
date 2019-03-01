package org.landy.commons.datacache.conf;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ResourceBundle;

public class MongoConfig extends AbstractCacheConfig {

    private int port;
    private String mongoDBName;
    private String mongoCollectionName;
    private DB db;

    public MongoConfig() {
    }

    void parseOtherConfig(ResourceBundle rs) {
        LOGGER.info("解析Mongo参数");
        LOGGER.info("MongoDB=" + super.getHubCacheServer());
        String val = rs.getString("port");
        Assert.notNull(val, "MongoDB的端口不为空");
        this.port = Integer.parseInt(val);
        this.mongoDBName = rs.getString("mongoDBName");
        Assert.notNull(this.mongoDBName, "Mongo数据库名不为空");
        this.mongoCollectionName = rs.getString("mongoCollectionName");
        Assert.notNull(this.mongoCollectionName, "Mongo集合名不为空");

        try {
            Mongo mongo = new Mongo(super.getHubCacheServer(), this.port);
            mongo.setWriteConcern(WriteConcern.SAFE);
            this.db = mongo.getDB(this.mongoDBName);
            if (StringUtils.isNotBlank(this.getHubCacheAccount()) && StringUtils.isNotBlank(this.getHubCachePassword())) {
                LOGGER.info("配置认证：account=" + this.getHubCacheAccount() + ",password=" + this.getHubCachePassword());
                char[] chpwds = this.getHubCachePassword().toCharArray();
                boolean isauth = this.db.authenticate(this.getHubCacheAccount(), chpwds);
                Assert.isTrue(isauth, "认证不成功");
            }
        } catch (Exception var6) {
            LOGGER.error("Mongo连接创建失败");
        }

    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMongoDBName() {
        return this.mongoDBName;
    }

    public void setMongoDBName(String mongoDBName) {
        this.mongoDBName = mongoDBName;
    }

    public String getMongoCollectionName() {
        return this.mongoCollectionName;
    }

    public void setMongoCollectionName(String mongoCollectionName) {
        this.mongoCollectionName = mongoCollectionName;
    }

    public DB getDb() {
        return this.db;
    }

}
