package org.fortune.doc.server;

import org.fortune.commons.core.help.AbstractApplicationContextHelper;
import org.fortune.doc.server.support.DocServerPipelineFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @author: landy
 * @date: 2019/6/2 22:03
 * @description: 文件服务器netty启动类
 */
public class DocServer extends AbstractApplicationContextHelper {

    private void run() {
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new DocServerPipelineFactory());
        bootstrap.bind(new InetSocketAddress(DocServerContainer.getInstance()
                .getPort()));
    }

    public void init() {
        run();
    }

}
