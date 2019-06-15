package org.fortune.doc.client.support;

import org.fortune.doc.common.domain.Result;
import org.fortune.doc.common.utils.JsonUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: landy
 * @date: 2019/5/30 23:21
 * @description: 客户端文件处理核心句柄
 */
public class DocClientSupportHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocClientSupportHandler.class);

    private boolean readingChunks;
    private final StringBuilder responseContent = new StringBuilder();
    private Result result;

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        if (!this.readingChunks) {
            HttpResponse response = (HttpResponse) e.getMessage();
            LOGGER.info("STATUS: " + response.getStatus());
            if ((response.getStatus().getCode() == 200)
                    && (response.isChunked())) {
                this.readingChunks = true;
            } else {
                ChannelBuffer content = response.getContent();
                if (content.readable())
                    this.responseContent.append(content
                            .toString(CharsetUtil.UTF_8));
            }
        } else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
            if (chunk.isLast()) {
                this.readingChunks = false;
                this.responseContent.append(chunk.getContent().toString(
                        CharsetUtil.UTF_8));

                String json = this.responseContent.toString();
                this.result = JsonUtil.parseObject(json, Result.class);
            } else {
                this.responseContent.append(chunk.getContent().toString(
                        CharsetUtil.UTF_8));
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        LOGGER.error("Occurs an unexpected exception:", e);
        e.getChannel().close();
    }

    public Result getResult() {
        return this.result;
    }

}
