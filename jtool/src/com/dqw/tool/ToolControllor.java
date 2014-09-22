package com.dqw.tool;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by dqw on 9/23/2014.
 */
public class ToolControllor {
    public TextField address;
    public TextField port;
    public TextArea inputText;
    public TextArea logText;
    public Button connectBut;

    private SocketChannel channel;

    private Bootstrap bs;

    public void init() {
        bs = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ToolControllor.this.channel = ch;
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if (msg instanceof ByteBuf)
                                {
                                    ByteBuf buf = (ByteBuf) msg;
                                    byte b[] = new byte[buf.readableBytes()];
                                    buf.readBytes(b);
                                    String text = new String(b);
                                    Platform.runLater(() -> {
                                        logText.appendText(text);
                                    });
                                    buf.release();
                                }
                            }
                        });
                    }
                });
    }

    public void send(ActionEvent actionEvent) {
        ByteBuf buffer = channel.alloc().buffer();
        buffer.writeBytes(inputText.getText().getBytes());
        channel.writeAndFlush(buffer);
    }

    public void clear(ActionEvent actionEvent) {
        logText.clear();
    }

    public void connect(ActionEvent actionEvent) {

        if(channel != null && channel.isActive()) {
            channel.close();
            connectBut.setText("Connect");
        }
        else {

            ChannelFuture future = bs.connect(address.getText(), Integer.valueOf(port.getText()));
            future.syncUninterruptibly();
            if (future.isSuccess())
                connectBut.setText("Disconnect");
            //bs.connect("127.0.0.1", 8080).syncUninterruptibly();
        }
    }

}
