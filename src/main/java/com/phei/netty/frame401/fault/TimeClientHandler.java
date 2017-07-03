/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.phei.netty.frame401.fault;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeClientHandler.class);
    private static final int LOOP = 100;

    private int counter;
    private byte[] req;

    public TimeClientHandler() {
        req = ("time" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf message;
        for (int i = 0; i < LOOP; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        LOGGER.info("Now is : {}; the counter is : {}", body, ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 释放资源
        LOGGER.info("Unexpected exception from downstream : {}", cause.getMessage());
        ctx.close();
    }
}
