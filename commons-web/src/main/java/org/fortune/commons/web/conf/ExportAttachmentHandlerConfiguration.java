package org.fortune.commons.web.conf;

import org.fortune.commons.web.export.ExportAttachmentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Landy
 * @date: 2019/4/16 22:21
 * @description:
 */
@Configuration
public class ExportAttachmentHandlerConfiguration {

    @Bean(ExportAttachmentHandler.BEAN_NAME)
    public ExportAttachmentHandler exportAttachmentHandler() {
        ExportAttachmentHandler exportAttachmentHandler = new ExportAttachmentHandler();
        exportAttachmentHandler.init();
        return exportAttachmentHandler;
    }

}
