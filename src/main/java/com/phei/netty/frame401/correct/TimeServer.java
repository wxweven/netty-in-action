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
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端程序
 *
 * @author wxweven
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeServer.class);
    private static final int MAX_LENGTH = 1024;

    public static void main(String[] args) throws Exception {
        new TimeServer().bind(CommonConstants.PORT);
    }

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                  .channel(NioServerSocketChannel.class)
                  .option(ChannelOption.SO_BACKLOG, 1024)
                  .childHandler(new ChildChannelHandler());

            // 绑定端口，同步等待成功
            ChannelFuture f = server.bind(port).sync();

            LOGGER.info("服务端启动成功，port：{}", port);

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            // 添加基于行的解码器，将接收到的消息按行分割
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(MAX_LENGTH));

            // 添加字符串解码器，将字节流解析为字符串
            socketChannel.pipeline().addLast(new StringDecoder());

            // 添加自定义的服务端handler
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }
}
