package de.illigalspigot.neoprotect.spigot.haproxyreverser.netty;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class NettyDecoderHandler extends MessageToMessageDecoder<Object> {

	private NettyInjectHandler handler;
	
	public NettyDecoderHandler(NettyInjectHandler handler) throws Exception {
		this.handler = handler;
	}

	@Override
	protected void decode(ChannelHandlerContext context, Object packet, List<Object> out) throws Exception {
		this.handler.packetReceived(this, context, packet);
		out.add(packet);
	}

}
