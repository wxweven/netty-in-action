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
package com.phei.netty.basic301;

import com.phei.netty.constant.CommonConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeClient {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new TimeClient().connect(CommonConstants.HOST, CommonConstants.PORT);
    }

    /**
     * 连接服务器
     *
     * @param host
     * @param port
     * @throws Exception
     */
    public void connect(String host, int port) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.group(group).channel(NioSocketChannel.class)
                  .option(ChannelOption.TCP_NODELAY, true)
                  .handler(new ChannelInitializer<SocketChannel>() {
                      @Override
                      public void initChannel(SocketChannel ch)
                              throws Exception {
                          ch.pipeline().addLast(new TimeClientHandler());
                      }
                  });

            // 发起异步连接操作
            ChannelFuture f = client.connect(host, port).sync();

            // 当代客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
