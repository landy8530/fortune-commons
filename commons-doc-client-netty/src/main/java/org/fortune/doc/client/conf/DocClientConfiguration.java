package org.fortune.doc.client.conf;

import org.fortune.doc.client.DocClientContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: landy
 * @date: 2019/6/14 20:31
 * @description:
 */
@Configuration
public class DocClientConfiguration {

    @Value("${upload.userName}")
    private String userName ;
    @Value("${upload.password}")
    private String password ;
    @Value("${upload.server.host}")
    private String host ;
    @Value("${upload.server.port}")
    private int port ;
    @Value("${upload.server.access.domainName}")
    private String domainName ;
    @Value("${upload.server.access.port}")
    private int accessPort ;

    @Bean(DocClientContainer.BEAN_NAME_DOC_CLIENT_CONTAINER)
    public DocClientContainer docClientContainer() {
        DocClientContainer docClientContainer = new DocClientContainer(userName, password, host, port, domainName, accessPort);
        return docClientContainer;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
