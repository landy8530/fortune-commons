package org.landy.commons.datacache.plugins.mongo.load;

import org.landy.commons.datacache.handler.LoadFromCache;
import org.landy.commons.datacache.plugins.mongo.MongoDBOperator;
import org.landy.commons.datacache.plugins.support.load.FromCache;
import org.landy.commons.datacache.plugins.support.load.FromMongoDb;
import org.landy.commons.datacache.plugins.support.load.FromProxyLocalMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadFromMongo extends LoadFromCache {

    private Logger logger = LoggerFactory.getLogger(LoadFromMongo.class);
    private MongoDBOperator mongoDBOperate;
    private long expiredTime = 0L;
    private boolean mappingLocalFlag = true;
    private FromCache fromCache;

    public LoadFromMongo(MongoDBOperator mongoDBOperate, long expiredTime, boolean mappingLocalFlag) {
        this.mongoDBOperate = mongoDBOperate;
        this.setExpiredTime(expiredTime);
        this.setMappingLocalFlag(mappingLocalFlag);
    }

    public <T> T load(String key) {
        return this.fromCache.fetch(super.getKey(key));
    }

    public long getExpiredTime() {
        return this.expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
        this.expiredTime = expiredTime * 1000L;
        this.logger.info("映射本地缓存的过期时间(ms)：" + this.expiredTime);
    }

    public boolean getMappingLocalFlag() {
        return this.mappingLocalFlag;
    }

    public void setMappingLocalFlag(boolean mappingLocalFlag) {
        this.mappingLocalFlag = mappingLocalFlag;
        if (this.mappingLocalFlag) {
            this.logger.info("您启用了将Mongo的数据映射到本地内存功能");
            this.fromCache = new FromProxyLocalMemory(this.mongoDBOperate, this.expiredTime);
        } else {
            this.logger.info("直接从Mongo获取缓存数据");
            this.fromCache = new FromMongoDb(this.mongoDBOperate);
        }

    }

}
