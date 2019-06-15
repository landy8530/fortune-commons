package org.fortune.doc.client.support;

import org.fortune.doc.common.domain.Result;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author: landy
 * @date: 2019/5/30 23:23
 * @description: 客户端处理
 */
public class DocClientPipelineFactory implements ChannelPipelineFactory {

    private DocClientSupportHandler clientHandler;

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        this.clientHandler = new DocClientSupportHandler();
        pipeline.addLast("codec", new HttpClientCodec());
        pipeline.addLast("inflater", new HttpContentDecompressor());
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("handler", this.clientHandler); //具体文件处理句柄
        return pipeline;
    }

    /**
     * 处理结果
     * @return
     * @author:landyChris
     */
    public Result getResult() {
        if (this.clientHandler != null) {
            return this.clientHandler.getResult();
        }
        return null;
    }

}
