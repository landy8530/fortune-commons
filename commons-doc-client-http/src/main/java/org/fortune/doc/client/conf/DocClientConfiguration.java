package org.fortune.doc.client.conf;

import org.fortune.doc.client.DocClientContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: landy
 * @date: 2019/7/6 10:40
 * @description:
 */
@Configuration
public class DocClientConfiguration {

    @Value("${upload.server.host}")
    private String docServerHost;
    @Value("${upload.server.port}")
    private int docServerPort;
    @Value("${upload.server.domainName}")
    private String docServerDomainName;
    @Value("${upload.userName}")
    private String userName;
    @Value("${upload.password}")
    private String password;

    @Bean(DocClientContainer.BEAN_NAME_DOC_CLIENT_CONTAINER)
    public DocClientContainer docClientContainer() {
        DocClientContainer docClientContainer = new DocClientContainer(docServerHost, docServerPort, docServerDomainName, userName, password);
        return docClientContainer;
    }

}
