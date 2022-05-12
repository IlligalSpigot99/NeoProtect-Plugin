package de.illigalspigot.neoprotect.bungee.events;


import de.illigalspigot.neoprotect.bungee.NeoProtect;
import de.illigalspigot.neoprotect.bungee.manager.DirectConnectManager;
import de.illigalspigot.neoprotect.cpscounter.CPSCounter;
import de.illigalspigot.neoprotect.vulnerability.Type;
import de.illigalspigot.neoprotect.vulnerability.Vulnerability;
import net.md_5.bungee.api.event.ClientConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnectEvent implements Listener {
	
	@EventHandler
	public void onServerConnected(ClientConnectEvent event) {
		String address = event.getSocketAddress().toString().split(":")[0].replaceAll("/", "");
		if(!DirectConnectManager.isValidIP(address)) {
			event.setCancelled(true);
			Vulnerability.addVulnerability(Type.DIRECT_IP, NeoProtect.getInstance().getFileManagement());
			CPSCounter.add();
			if(CPSCounter.get() >= 25) {
				Vulnerability.addVulnerability(Type.DIRECT_ATTACK, NeoProtect.getInstance().getFileManagement());
			}
		}
		
	}

}
