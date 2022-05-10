package de.illigalspigot.neoprotect.spigot.haproxyreverser;

import de.illigalspigot.neoprotect.spigot.haproxyreverser.netty.NettyInitializerLoader;

public class HAProxyReverser {
	
	public HAProxyReverser() {
		NettyInitializerLoader.inject();
	}
	
}
