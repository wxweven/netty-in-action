/*
 * Copyright 2013-2018 Lilinfeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phei.netty.frame401.correct;

import com.phei.netty.constant.CommonConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
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
public class TimeClient {
    public static final Logger LOGGER = LoggerFactory.getLogger(TimeClient.class);
    private static final int MAX_LENGTH = 1024;

    public static void main(String[] args) throws Exception {
        new TimeClient().connect(CommonConstants.HOST, CommonConstants.PORT);
    }

    /**
     * 建立服务端连接，并引导客户端
     *
     * @param host 服务端主机host
     * @param port 服务端主机port
     * @throws Exception 异常
     */

    public void connect(String host, int port) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch)
                         throws Exception {
                     // 添加基于行的帧解码器，将收到的消息按行分割
                     ch.pipeline().addLast(new LineBasedFrameDecoder(MAX_LENGTH));

                     // 添加字符串解码器，直接将收到的字节流解码为字符串
                     ch.pipeline().addLast(new StringDecoder());

                     // 添加客户端自定义的handler
                     ch.pipeline().addLast(new TimeClientHandler());
                 }
             });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
