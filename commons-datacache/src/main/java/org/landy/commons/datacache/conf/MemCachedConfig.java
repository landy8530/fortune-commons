package org.landy.commons.datacache.conf;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.buffer.SimpleBufferAllocator;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import net.rubyeye.xmemcached.utils.XMemcachedClientFactoryBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.landy.commons.core.setting.Settings;
import org.landy.commons.core.utils.StringUtil;
import org.landy.commons.datacache.exception.DataCacheConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
//@ComponentScan("org.landy.commons.datacache")
public class MemCachedConfig extends AbstractCacheConfig {

    @Autowired
    private Settings settings;

    @Value("${memcache.initConn}")
    private int initConn = 50;
    @Value("${memcache.minConn}")
    private int minConn = 50;
    @Value("${memcache.maxConn}")
    private int maxConn = 500;
    @Value("${memcache.expiredTime}")
    private int expiredTime4Memcached = 0;
    @Value("${memcache.connectionPoolSize}")
    private int connectionPoolSize;
//    @Value("${memcache.cluster}")
    private List<String> cluster;

    private List<Integer> weights;
    private List<String> accounts = new ArrayList<>();
    private String servers;

    private Map<InetSocketAddress, AuthInfo> authInfoMap = new HashMap<>();

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

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public List<String> getCluster() {
        return cluster;
    }

    public void setCluster(List<String> cluster) {
        this.cluster = cluster;
    }

    @Bean("memCachedClient")
    public MemcachedClient memCachedClient() {
        XMemcachedClientFactoryBean xMemcachedClientFactoryBean = xMemcachedClientFactoryBean();
        MemcachedClient memCachedClient = null;
        try {
            memCachedClient = (MemcachedClient)xMemcachedClientFactoryBean.getObject();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return memCachedClient;
    }

    @Bean(value = "xMemcachedClientFactoryBean",destroyMethod = "shutdown")
    public XMemcachedClientFactoryBean xMemcachedClientFactoryBean() {
        XMemcachedClientFactoryBean factoryBean = new XMemcachedClientFactoryBean();

        this.initServerInfo();

        factoryBean.setServers(servers);
        // server's weights
        factoryBean.setWeights(weights);
        // AuthInfo map,only valid on 1.2.5 or later version
        factoryBean.setAuthInfoMap(authInfoMap);
        // nio connection pool size
        factoryBean.setConnectionPoolSize(connectionPoolSize);
        // Use binary protocol,default is TextCommandFactory
        factoryBean.setCommandFactory(binaryCommandFactory());
        // Distributed strategy
        factoryBean.setSessionLocator(sessionLocator());
        // Serializing transcoder
        factoryBean.setTranscoder(serializingTranscoder());
        // ByteBuffer allocator
        factoryBean.setBufferAllocator(bufferAllocator());
        // Failure mode
        factoryBean.setFailureMode(false);

        return factoryBean;
    }

    @Bean
    public BinaryCommandFactory binaryCommandFactory() {
        return new BinaryCommandFactory();
    }

    @Bean
    public KetamaMemcachedSessionLocator sessionLocator() {
        return new KetamaMemcachedSessionLocator();
    }

    @Bean
    public SerializingTranscoder serializingTranscoder() {
        return new SerializingTranscoder();
    }

    @Bean
    public SimpleBufferAllocator bufferAllocator() {
        return new SimpleBufferAllocator();
    }

    private void initServerInfo() {
        List<String> serverList = new ArrayList<>();
        if(super.isClusterFlag()) {
            this.cluster = settings.getAsList("memcache.cluster");
            List<String> cluster = this.cluster;
            if(CollectionUtils.isNotEmpty(cluster)) {
                weights = new ArrayList<>();
                cluster.stream().forEach(item -> {
                    String[] serversArr = StringUtils.split(item,SERVERS_ACCOUNTS_DELIMITER);
                    if(serversArr == null || serversArr.length != 3) {
                        throw new DataCacheConfigException("集群配置不符合预定规则，集群配置格式：account:password@host:port@weight");
                    }
                    String account = serversArr[0];//account:password
                    String server = serversArr[1];//host:port
                    String weight = serversArr[2];//weight
                    serverList.add(server);
                    accounts.add(account);
                    weights.add(Integer.valueOf(weight));
                    String[] accountArr = StringUtils.split(account,ACCOUNT_PASSWORD_DELIMITER);
                    if(accountArr != null && accountArr.length == 2) {
                        String username = accountArr[0];
                        String password = accountArr[1];
                        buildAuthInfo(server,username,password);
                    }
                });
            }
        } else {
            String server = super.getHubCacheServer() + SERVER_PORT_DELIMITER + super.getHubCachePort();
            serverList.add(server);
            String username = super.getHubCacheAccount();
            String password = super.getHubCachePassword();
            buildAuthInfo(server,username,password);
        }
        servers = StringUtil.listToString(serverList,SERVERS_DELIMITER);
    }

    public void buildAuthInfo(String server,String username,String password) {
        InetSocketAddress socketAddress = AddrUtil.getOneAddress(server);
        AuthInfo authInfo = AuthInfo.typical(username,password); //CRAM-MD5 or PLAIN auth
        authInfoMap.put(socketAddress,authInfo);
    }
}
