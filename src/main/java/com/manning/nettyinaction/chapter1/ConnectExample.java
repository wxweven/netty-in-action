package com.manning.nettyinaction.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Listing 1.3 and 1.4 of <i>Netty in Action</i>
 *
 * @author <a href="mailto:norman.maurer@googlemail.com">Norman Maurer</a>
 */
public class ConnectExample {

    public static void connect(Channel channel) {
        // Does not block
        ChannelFuture future = channel.connect(
                new InetSocketAddress("192.168.0.1", 25));
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                ByteBuf buffer = Unpooled.copiedBuffer(
                        "Hello", Charset.defaultCharset());
                ChannelFuture wf = future1.channel().writeAndFlush(buffer);
            } else {
                Throwable cause = future1.cause();
                cause.printStackTrace();
            }
        });

    }
}
