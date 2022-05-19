package de.illigalspigot.neoprotect.spigot.haproxyreverser.netty;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.illigalspigot.neoprotect.spigot.haproxyreverser.ReflectionUtils;
import de.illigalspigot.neoprotect.spigot.haproxyreverser.netty.versions.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyInitializerLoader {
	
	private static String channelFieldName;
	
	public static void inject() {
		
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
		channelFieldName = getChannelFieldName(version);
		if(channelFieldName == null){
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Unknown server version " + version + ", please see if there are any updates avaible");
			return;
		} else {
			Bukkit.getServer().getLogger().info("Detected server version " + version);
		}
		try {
			Bukkit.getServer().getLogger().info("Injecting NettyHandler...");
			
			Method serverGetHandle = Bukkit.getServer().getClass().getDeclaredMethod("getServer");
			Object minecraftServer = serverGetHandle.invoke(Bukkit.getServer());
			
			Method serverConnectionMethod = null;
			for(Method method : minecraftServer.getClass().getSuperclass().getDeclaredMethods()) {
				if(!method.getReturnType().getSimpleName().equals("ServerConnection")) {
					continue;
				}
				serverConnectionMethod = method;
				break;
			}
			Object serverConnection = serverConnectionMethod.invoke(minecraftServer);
			System.out.println(serverConnection.getClass().getSimpleName());
			List<ChannelFuture> channelFutureList = ReflectionUtils.getPrivateField(serverConnection.getClass(), serverConnection, List.class, channelFieldName);
			
			for(ChannelFuture channelFuture : channelFutureList) {
				ChannelPipeline channelPipeline = channelFuture.channel().pipeline();
				ChannelHandler serverBootstrapAcceptor = channelPipeline.first();
				ChannelInitializer<SocketChannel> oldChildHandler = ReflectionUtils.getPrivateField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, ChannelInitializer.class, "childHandler");
				
				switch (version){
					case "v1_18_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_18_R1(oldChildHandler));
						break;
					case "v1_17_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_17_R1(oldChildHandler));
						break;
					case "v1_16_R3":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_16_R3(oldChildHandler));
						break;
					case "v1_16_R2":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_16_R2(oldChildHandler));
						break;
					case "v1_16_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_16_R1(oldChildHandler));
						break;
					case "v1_15_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_15_R1(oldChildHandler));
						break;
					case "v1_14_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_14_R1(oldChildHandler));
						break;
					case "v1_13_R2":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_13_R2(oldChildHandler));
						break;
					case "v1_13_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_13_R1(oldChildHandler));
						break;
					case "v1_12_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_12_R1(oldChildHandler));
						break;
					case "v1_11_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_11_R1(oldChildHandler));
						break;
					case "v1_10_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_10_R1(oldChildHandler));
						break;
					case "v1_9_R2":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_9_R2(oldChildHandler));
						break;
					case "v1_9_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_9_R1(oldChildHandler));
						break;
					case "v1_8_R2":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_8_R2(oldChildHandler));
						break;
					case "v1_8_R3":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_8_R3(oldChildHandler));
						break;
					case "v1_8_R1":
						ReflectionUtils.setFinalField(serverBootstrapAcceptor.getClass(), serverBootstrapAcceptor, "childHandler", new NettyChannelInitializer1_8_R1(oldChildHandler));
						break;
				}
			}
			
			Bukkit.getServer().getLogger().info("Injection successful!");
		} catch (Exception e) {
			Bukkit.getServer().getLogger().info("Injection netty handler failed!");
			e.printStackTrace();
		}
		
	}
	
	public static String getChannelFieldName(String version){
		String name = null;
		switch (version){
			case "v1_18_R1":
				name = "listeningChannels";
				break;
			case "v1_17_R1":
				name = "listeningChannels";
				break;
			case "v1_16_R3":
				name = "listeningChannels";
				break;
			case "v1_16_R2":
				name = "listeningChannels";
				break;
			case "v1_16_R1":
				name = "listeningChannels";
				break;
			case "v1_15_R1":
				name = "listeningChannels";
				break;
			case "v1_14_R1":
				name = "f";
				break;
			case "v1_13_R2":
				name = "f";
				break;
			case "v1_13_R1":
				name = "g";
				break;
			case "v1_12_R1":
				name = "g";
				break;
			case "v1_11_R1":
				name = "g";
				break;
			case "v1_10_R1":
				name = "g";
				break;
			case "v1_9_R2":
				name = "g";
				break;
			case "v1_9_R1":
				name = "g";
				break;
			case "v1_8_R2":
				name = "g";
				break;
			case "v1_8_R3":
				name = "g";
				break;
			case "v1_8_R1":
				name = "f";
				break;
			case "v1_7_R4":
				name = "e";
				break;
		}
		return name;
	}

}
