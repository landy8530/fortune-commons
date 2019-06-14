package org.fortune.doc.client;

import org.fortune.doc.client.handler.*;
import org.fortune.doc.client.support.DocClientPipelineFactory;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.Result;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

/**
 * @author: landy
 * @date: 2019/5/30 23:38
 * @description: 客户端对外暴露的API接口
 * 1.上传文件
 * 2.替换文件
 * 3.删除文件
 * 4.生成缩略图
 */
public class DocClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocClient.class);

    private static URI getUri(String host, int port) {
        String postUrl = "http://" + host + ":" + port + "/formpost";
        URI uri;
        try {
            uri = new URI(postUrl);
        } catch (URISyntaxException e) {
            LOGGER.error("Error: " + e.getMessage());
            return null;
        }
        return uri;
    }

    private static ClientBootstrap createClientBootstrap(
            DocClientPipelineFactory clientPipelineFactory) {
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(clientPipelineFactory);

        return bootstrap;
    }

    private static HttpDataFactory getHttpDataFactory() {
        HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
        org.jboss.netty.handler.codec.http.multipart.DiskFileUpload.deleteOnExitTemporaryFile = false;
        org.jboss.netty.handler.codec.http.multipart.DiskFileUpload.baseDirectory = System
                .getProperty("user.dir");
        org.jboss.netty.handler.codec.http.multipart.DiskAttribute.deleteOnExitTemporaryFile = false;
        org.jboss.netty.handler.codec.http.multipart.DiskAttribute.baseDirectory = System
                .getProperty("user.dir");
        return factory;
    }

    private static void uploadFile(ClientBootstrap bootstrap, String host,
                                   int port, File file, String fileName, String thumbMark,
                                   String userName, String pwd) {
        //1.构建uri对象
        URI uri = getUri(host, port);
        //2.连接netty服务端
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
                port));
        //3.异步获取Channel对象
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
        //4.初始化文件上传句柄对象
        AbstractDocClientHandler handler = new UploadDocClientHandler(host, uri,
                file, fileName, thumbMark, userName, pwd);
        //5.获取Request对象
        HttpRequest request = handler.getRequest();
        //6.获取Http数据处理工厂
        HttpDataFactory factory = getHttpDataFactory();
        //7.进行数据的包装处理，主要是进行上传文件所需要的参数的设置，此时调用的句柄是具体的UploadFileClientHandler对象
        HttpPostRequestEncoder bodyRequestEncoder = handler
                .wrapRequestData(factory);
        //8.把request写到管道中，传输给服务端
        channel.write(request);
        //9.做一些关闭资源的动作
        if (bodyRequestEncoder.isChunked()) {
            channel.write(bodyRequestEncoder).awaitUninterruptibly();
        }
        bodyRequestEncoder.cleanFiles();
        channel.getCloseFuture().awaitUninterruptibly();

        bootstrap.releaseExternalResources();
        factory.cleanAllHttpDatas();
    }
    /**
     * 文件上传
     * @param file 需要上传的文件
     * @param fileName 文件名称
     * @param thumbMark 是否需要生成缩略图
     * @return
     * @author:landyChris
     */
    public static String uploadFile(File file, String fileName,
                                    boolean thumbMark) {
        DocClientPipelineFactory clientPipelineFactory = new DocClientPipelineFactory();
        //辅助类。用于帮助我们创建NETTY服务
        ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
        String strThumbMark = Constants.THUMB_MARK_NO;
        if (thumbMark) {
            strThumbMark = Constants.THUMB_MARK_YES;
        }
        //具体处理上传文件逻辑
        uploadFile(bootstrap, DocClientContainer.getInstance().getHost(),
                DocClientContainer.getInstance().getPort(), file, fileName, strThumbMark,
                DocClientContainer.getInstance().getUserName(),
                DocClientContainer.getInstance().getPassword());
        Result result = clientPipelineFactory.getResult();
        if ((result != null) && (result.isCode())) {
            return result.getFilePath();
        }
        return null;
    }

    private static void deleteFile(ClientBootstrap bootstrap, String host,
                                   int port, String filePath, String userName, String pwd) {
        URI uri = getUri(host, port);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
                port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }

        AbstractDocClientHandler handler = new DeleteDocClientHandler(host, uri,
                filePath, userName, pwd);
        HttpRequest request = handler.getRequest();
        HttpDataFactory factory = getHttpDataFactory();
        HttpPostRequestEncoder bodyRequestEncoder = handler
                .wrapRequestData(factory);
        channel.write(request);
        if (bodyRequestEncoder.isChunked()) {
            channel.write(bodyRequestEncoder).awaitUninterruptibly();
        }
        bodyRequestEncoder.cleanFiles();
        channel.getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        factory.cleanAllHttpDatas();
    }
    /**
     * 文件删除
     * @param filePath 文件服务器存储的文件路径（相对路径）
     * @param userName
     * @param pwd
     * @return
     * @author:landyChris
     */
    public static boolean deleteFile(String filePath, String userName,
                                     String pwd) {
        DocClientPipelineFactory clientPipelineFactory = new DocClientPipelineFactory();
        ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
        deleteFile(bootstrap, DocClientContainer.getInstance().getHost(),
                DocClientContainer.getInstance().getPort(), filePath, userName, pwd);
        Result result = clientPipelineFactory.getResult();
        if ((result != null) && (result.isCode())) {
            return result.isCode();
        }
        return false;
    }
    /**
     * 文件删除
     * @param filePath 文件服务器存储的文件路径（相对路径）
     * @return
     * @author:landyChris
     */
    public static boolean deleteFile(String filePath) {
        return deleteFile(filePath,DocClientContainer.getInstance().getUserName(), DocClientContainer.getInstance().getPassword());
    }

    private static void replaceFile(ClientBootstrap bootstrap, String host,
                                    int port, File file, String filePath, String userName, String pwd) {
        URI uri = getUri(host, port);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
                port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }

        AbstractDocClientHandler handler = new ReplaceDocClientHandler(host, uri,
                filePath, file, userName, pwd);
        HttpRequest request = handler.getRequest();
        HttpDataFactory factory = getHttpDataFactory();
        HttpPostRequestEncoder bodyRequestEncoder = handler
                .wrapRequestData(factory);
        channel.write(request);
        if (bodyRequestEncoder.isChunked()) {
            channel.write(bodyRequestEncoder).awaitUninterruptibly();
        }
        bodyRequestEncoder.cleanFiles();
        channel.getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        factory.cleanAllHttpDatas();
    }
    /**
     * 替换文件
     * @param file 需要替换的文件
     * @param filePath 文件服务器存储的文件路径（相对路径）
     * @return
     * @author:landyChris
     */
    public static boolean replaceFile(File file, String filePath) {
        DocClientPipelineFactory clientPipelineFactory = new DocClientPipelineFactory();
        ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
        replaceFile(bootstrap, DocClientContainer.getInstance().getHost(),
                DocClientContainer.getInstance().getPort(), file, filePath,
                DocClientContainer.getInstance().getUserName(),
                DocClientContainer.getInstance().getPassword());
        Result result = clientPipelineFactory.getResult();
        if ((result != null) && (result.isCode())) {
            return result.isCode();
        }

        return false;
    }

    private static void createThumbPicture(ClientBootstrap bootstrap,
                                           String host, int port, String filePath, String userName, String pwd) {
        URI uri = getUri(host, port);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
                port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }

        AbstractDocClientHandler handler = new CreateThumbPictureClientHandler(
                host, uri, userName, pwd, filePath);
        HttpRequest request = handler.getRequest();
        HttpDataFactory factory = getHttpDataFactory();
        HttpPostRequestEncoder bodyRequestEncoder = handler
                .wrapRequestData(factory);
        channel.write(request);
        if (bodyRequestEncoder.isChunked()) {
            channel.write(bodyRequestEncoder).awaitUninterruptibly();
        }
        bodyRequestEncoder.cleanFiles();
        channel.getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        factory.cleanAllHttpDatas();
    }
    /**
     * 生成缩略图
     * @param filePath 文件服务器存储的文件路径（相对路径）
     * @param userName
     * @param pwd
     * @return
     * @author:landyChris
     */
    public static boolean createThumbPicture(String filePath, String userName,
                                             String pwd) {
        DocClientPipelineFactory clientPipelineFactory = new DocClientPipelineFactory();
        ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
        createThumbPicture(bootstrap, DocClientContainer.getInstance().getHost(),
                DocClientContainer.getInstance().getPort(), filePath, userName, pwd);
        Result result = clientPipelineFactory.getResult();
        if ((result != null) && (result.isCode())) {
            return result.isCode();
        }
        return false;
    }
    /**
     * 生成缩略图
     * @param filePath 文件服务器存储的文件路径（相对路径）
     * @return
     * @author:landyChris
     */
    public static boolean createThumbPicture(String filePath) {
        return createThumbPicture(filePath,
                DocClientContainer.getInstance().getUserName(),
                DocClientContainer.getInstance().getPassword());
    }

}
