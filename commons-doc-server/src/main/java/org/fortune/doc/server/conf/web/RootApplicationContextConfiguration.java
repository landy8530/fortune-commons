package org.fortune.doc.server.conf.web;

import org.apache.commons.beanutils.BeanUtils;
import org.fortune.doc.common.domain.Account;
import org.fortune.doc.server.DocServer;
import org.fortune.doc.server.DocServerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Landy
 * @date: 2019/4/7 00:04
 * @description: WebApplicationContext配置类,需要根据具体的业务逻辑进行配置，比如缓存门面类的配置
 * 需要在web端工程里具体配置
 */
@Configuration
@EnableAspectJAutoProxy // 相当于 xml 中的 <aop:aspectj-autoproxy/>
@EnableTransactionManagement // 开启注解事务
@Import({
        BeanInitializeCompletedConfiguration.class
})
public class RootApplicationContextConfiguration {

    private static final String DOC_SERVER_CONFIG_PATH = "classpath*:config/doc-server.yml";

    /**
     * 文件服务器上传鉴权账户列表
     */
    private List<Account> accounts;

    @Bean(name = DocServerContainer.BEAN_NAME_DOC_SERVER_CONTAINER)
    public DocServerContainer DocServerContainer() throws IOException {
        //升级到spring boot后就不用这么麻烦了，直接使用@ConfigurationProperties(prefix = "doc.server")
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(DOC_SERVER_CONFIG_PATH);
        Yaml yaml = new Yaml(new CustomConstructor());
        URL url = resources[0].getURL();
        DocServerContainer docServerContainer = yaml.loadAs(url.openStream(), DocServerContainer.class);
        return docServerContainer;
    }

    @Bean
    public DocServer docServer() {
        return new DocServer();
    }
    /**
     * Yaml读取自定义配置文件，转化为自定义的Java对象
     */
    private class CustomConstructor extends Constructor {
        @Override
        protected Object constructObject(Node node) {
            DocServerContainer docServerContainer = new DocServerContainer();
            if(node instanceof MappingNode) {
                List<NodeTuple> nodes = ((MappingNode) node).getValue();
                for(NodeTuple nodeTuple : nodes) {
                    Node keyNode = nodeTuple.getKeyNode();
                    Node valueNode = nodeTuple.getValueNode();

                    if(keyNode instanceof ScalarNode) {
                        String key = ((ScalarNode) keyNode).getValue();
                        //accounts属性
                        if("accounts".equals(key)) {
                            if(valueNode instanceof SequenceNode) {
                                List<Node> accountsValue = ((SequenceNode) valueNode).getValue();
                                List<Account> accounts = new ArrayList<>();
                                for(Node accNode : accountsValue) {
                                    if(accNode instanceof MappingNode) {
                                        List<NodeTuple> accNodes = ((MappingNode) accNode).getValue();
                                        Account account = new Account();
                                        for(NodeTuple accNodeTuple: accNodes) {
                                            ScalarNode accKeyNode = (ScalarNode)accNodeTuple.getKeyNode();
                                            ScalarNode accValueNode = (ScalarNode)accNodeTuple.getValueNode();
                                            String propertyName = accKeyNode.getValue();
                                            String propertyValue = accValueNode.getValue();
                                            try {
                                                BeanUtils.setProperty(account, propertyName, propertyValue);
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            } catch (InvocationTargetException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        accounts.add(account);
                                    }
                                }
                                docServerContainer.setAccounts(accounts);
                            }
                        }
                        if("fileBaseDirectory".equals(key)) {
                            if(valueNode instanceof ScalarNode) {
                                String dir = ((ScalarNode) valueNode).getValue();
                                docServerContainer.setFileBaseDirectory(dir);
                            }
                        }

                        if("port".equals(key)) {
                            if(valueNode instanceof ScalarNode) {
                                String port = ((ScalarNode) valueNode).getValue();
                                docServerContainer.setPort(Integer.valueOf(port));
                            }
                        }
                    }

                }
                return docServerContainer;
            }
            return super.constructObject(node);
        }
    }

}
