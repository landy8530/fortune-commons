package org.landy.commons.datacache.conf;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan("org.landy.commons.datacache")
public class MemCachedConfig extends AbstractCacheConfig {

    @Value("${memcache.initConn}")
    private int initConn = 50;
    @Value("${memcache.minConn}")
    private int minConn = 50;
    @Value("${memcache.maxConn}")
    private int maxConn = 500;
    @Value("${memcache.expiredTime}")
    private int expiredTime4Memcached = 0;

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

    public int getExpiredTime4Memcached() {
        return expiredTime4Memcached;
    }

    public void setExpiredTime4Memcached(int expiredTime4Memcached) {
        this.expiredTime4Memcached = expiredTime4Memcached;
    }
    //https://github.com/killme2008/xmemcached/wiki/Xmemcached%20%E4%B8%AD%E6%96%87%E7%94%A8%E6%88%B7%E6%8C%87%E5%8D%97
    @Bean("memCachedClient")
    public MemcachedClient memCachedClient() {
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses("localhost:11211"));
        MemCachedClient memCachedClient = new MemCachedClient();
        LOGGER.info("memServers=" + super.getHubCacheServer());
        String[] servers = super.getHubCacheServer().split(",");
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        if (this.initConn == 0) {
            this.initConn = 50;
        }
        LOGGER.info("initConn=" + this.initConn);
        pool.setInitConn(this.initConn);

        if (this.minConn == 0) {
            this.minConn = 50;
        }
        LOGGER.info("minConn=" + this.minConn);
        pool.setMinConn(this.minConn);

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
        return memCachedClient;
    }


}
