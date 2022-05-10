package de.illigalspigot.neoprotect.spigot.haproxyreverser.netty.versions;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import de.illigalspigot.neoprotect.spigot.haproxyreverser.haproxy.HAProxyMessage;
import de.illigalspigot.neoprotect.spigot.haproxyreverser.haproxy.HAProxyMessageDecoder;
import de.illigalspigot.neoprotect.spigot.manager.DirectConnectManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.minecraft.server.v1_13_R1.NetworkManager;

public class NettyChannelInitializer1_13_R1 extends ChannelInitializer<SocketChannel> {

	
	private ChannelInitializer<SocketChannel> oldChildHandler;
	private Method oldChildHandlerMethod;
	
	public NettyChannelInitializer1_13_R1(ChannelInitializer<SocketChannel> oldChildHandler) throws Exception {
		this.oldChildHandler = oldChildHandler;
		this.oldChildHandlerMethod = this.oldChildHandler.getClass().getDeclaredMethod("initChannel", Channel.class);
		this.oldChildHandlerMethod.setAccessible(true);
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		String ip = channel.remoteAddress().getAddress().getHostAddress();
		if(!DirectConnectManager.isValidIP(ip)) {
			channel.close();
			return;
		}
		this.oldChildHandlerMethod.invoke(this.oldChildHandler, channel);

		if(channel.pipeline().get("haproxy-decoder") != null) {
			channel.pipeline().remove("haproxy-decoder");
		}
		if(channel.pipeline().get("haproxy-handler") != null) {
			channel.pipeline().remove("haproxy-handler");
		}
		channel.pipeline().addAfter("timeout", "haproxy-decoder", new HAProxyMessageDecoder());
		channel.pipeline().addAfter("haproxy-decoder", "haproxy-handler", new ChannelInboundHandlerAdapter(){
        	@Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        		String ip = ctx.channel().remoteAddress().toString();
        		if (msg instanceof HAProxyMessage) {
                    HAProxyMessage message = (HAProxyMessage) msg;
                    String realaddress = message.sourceAddress();
                    int realport = message.sourcePort();
                    
                    SocketAddress socketaddr = new InetSocketAddress(realaddress, realport);

                    NetworkManager networkmanager = (NetworkManager) channel.pipeline().get("packet_handler");
            		networkmanager.l = socketaddr;
                } else {
                    super.channelRead(ctx, msg);
                }
            }
		});
	}

}