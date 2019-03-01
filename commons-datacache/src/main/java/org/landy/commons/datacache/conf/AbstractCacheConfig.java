package org.landy.commons.datacache.conf;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public abstract class AbstractCacheConfig {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCacheConfig.class);
    private String hubCacheServer;
    private String hubCacheAccount;
    private String hubCachePassword;
    private boolean mappingLocalFlag = true;
    private long expiredTime = 10L;
    private String cacheKeyPrefix;
    private boolean loadFlag = true;

    public AbstractCacheConfig() {
    }

    public String getHubCacheServer() {
        return this.hubCacheServer;
    }

    public void setHubCacheServer(String hubCacheServer) {
        this.hubCacheServer = hubCacheServer;
    }

    public boolean getMappingLocalFlag() {
        return this.mappingLocalFlag;
    }

    public void setMappingLocalFlag(boolean mappingLocalFlag) {
        this.mappingLocalFlag = mappingLocalFlag;
    }

    public long getExpiredTime() {
        return this.expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getCacheKeyPrefix() {
        return this.cacheKeyPrefix;
    }

    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }

    public boolean getLoadFlag() {
        return this.loadFlag;
    }

    public void setLoadFlag(boolean loadFlag) {
        this.loadFlag = loadFlag;
    }

    private void parseCommon(ResourceBundle resBund) {
        this.setHubCacheServer(resBund.getString("hubCacheHost"));

        try {
            this.setHubCacheAccount(resBund.getString("hubCacheAccount"));
            this.setHubCachePassword(resBund.getString("hubCachePassword"));
        } catch (Exception var3) {
            LOGGER.error("没有配置账号和密码");
        }

        String val = resBund.getString("mappingLocalFlag");
        if (StringUtils.isNotEmpty(val) && BooleanUtils.toBooleanObject(val)) {
            this.mappingLocalFlag = Boolean.parseBoolean(val);
        } else {
            this.mappingLocalFlag = true;
        }

        LOGGER.info("mappingLocalFlag=" + this.mappingLocalFlag);
        val = resBund.getString("expiredTime");
        if (StringUtils.isNotEmpty(val) && NumberUtils.isNumber(val)) {
            this.expiredTime = Long.parseLong(val);
        } else {
            this.expiredTime = 50L;
        }

        if (this.expiredTime == 0L) {
            this.expiredTime = 500L;
        }

        LOGGER.info("expiredTime=" + this.expiredTime);
        val = resBund.getString("loadFlag");
        val = StringUtils.isEmpty(val) ? "" : val.toLowerCase();
        if (!StringUtils.isNotEmpty(val) || !"true".equals(val) && !"false".equals(val)) {
            this.loadFlag = true;
        } else {
            this.loadFlag = Boolean.parseBoolean(val);
        }

        LOGGER.info("loadFlag=" + this.loadFlag);
        val = resBund.getString("cacheKeyPrefix");
        if (StringUtils.isNotEmpty(val)) {
            this.cacheKeyPrefix = val;
        } else {
            this.cacheKeyPrefix = "zbs_" + System.currentTimeMillis() + "_";
        }

        LOGGER.info("cacheKeyPrefix=" + this.cacheKeyPrefix);
    }

    public void parseConfig(ResourceBundle resBund) {
        LOGGER.info("******************解析缓存配置参数******************");
        this.parseCommon(resBund);
        this.parseOtherConfig(resBund);
    }

    abstract void parseOtherConfig(ResourceBundle var1);

    public String getHubCacheAccount() {
        return this.hubCacheAccount;
    }

    public void setHubCacheAccount(String hubCacheAccount) {
        this.hubCacheAccount = hubCacheAccount;
    }

    public String getHubCachePassword() {
        return this.hubCachePassword;
    }

    public void setHubCachePassword(String hubCachePassword) {
        this.hubCachePassword = hubCachePassword;
    }

}
