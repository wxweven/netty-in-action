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
package com.phei.netty.frame401.delimiter;

import com.phei.netty.constant.CommonConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端程序
 *
 * @author wxweven
 * @version 1.0
 * @date 2014年2月14日
 */
public class EchoClient {
    public static final Logger LOGGER = LoggerFactory.getLogger(EchoClient.class);

    /**
     * 指定最大帧长度
     */
    private static final int MAX_FRAME_LENGTH = 1024;

    /**
     * 指定默认的分隔符
     */
    private static final String DELIMITER_STRING = "$_";

    public static void main(String[] args) throws Exception {
        new EchoClient().connect(CommonConstants.HOST, CommonConstants.PORT);
    }

    public void connect(String host, int port) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                     .channel(NioSocketChannel.class)
                     .option(ChannelOption.TCP_NODELAY, true)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         public void initChannel(SocketChannel ch)
                                 throws Exception {
                             ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER_STRING.getBytes());
                             // 添加分隔符解码器
                             ch.pipeline().addLast(new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, delimiter));
                             // 添加字符串解码器
                             ch.pipeline().addLast(new StringDecoder());
                             // 添加自定义解码器
                             ch.pipeline().addLast(new EchoClientHandler());
                         }
                     });

            // 发起异步连接操作
            ChannelFuture f = bootstrap.connect(host, port).sync();

            // 当代客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
