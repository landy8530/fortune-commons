package org.landy.commons.datacache.conf;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ResourceBundle;

public class MemCachedConfig extends AbstractCacheConfig {

    private MemCachedClient memCachedClient;
    private int initConn = 50;
    private int minConn = 50;
    private int maxConn = 500;

    public MemCachedConfig() {
    }

    public int getInitConn() {
        return this.initConn;
    }

    public void setInitConn(int initConn) {
        this.initConn = initConn;
    }

    public int getMinConn() {
        return this.minConn;
    }

    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    public int getMaxConn() {
        return this.maxConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public MemCachedClient getMemCachedClient() {
        return this.memCachedClient;
    }

    public void setMemCachedClient(MemCachedClient memCachedClient) {
        this.memCachedClient = memCachedClient;
    }

    private void parseMemCachedConfig(ResourceBundle resBund) {
        LOGGER.info("解析Memcached参数");
        LOGGER.info("memServers=" + super.getHubCacheServer());
        String[] servers = super.getHubCacheServer().split(",");
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        String val = resBund.getString("initConn");
        if (StringUtils.isNotEmpty(val) && NumberUtils.isNumber(val)) {
            this.initConn = Integer.parseInt(val);
        } else {
            this.initConn = 50;
        }

        LOGGER.info("initConn=" + this.initConn);
        pool.setInitConn(this.initConn);
        val = resBund.getString("minConn");
        if (StringUtils.isNotEmpty(val) && NumberUtils.isNumber(val)) {
            this.minConn = Integer.parseInt(val);
        } else {
            this.minConn = 50;
        }

        if (this.minConn == 0) {
            this.minConn = 50;
        }

        LOGGER.info("minConn=" + this.minConn);
        pool.setMinConn(this.minConn);
        val = resBund.getString("maxConn");
        if (StringUtils.isNotEmpty(val) && NumberUtils.isNumber(val)) {
            this.maxConn = Integer.parseInt(val);
        } else {
            this.maxConn = 50;
        }

        if (this.maxConn == 0) {
            this.maxConn = 500;
        }

        LOGGER.info("maxConn=" + this.maxConn);
        pool.setMaxConn(this.maxConn);
        pool.setMaxIdle(3600000L);
        pool.setMaintSleep(3000L);
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);
        pool.initialize();
    }

    void parseOtherConfig(ResourceBundle rs) {
        this.memCachedClient = new MemCachedClient();
        this.parseMemCachedConfig(rs);
    }

}
