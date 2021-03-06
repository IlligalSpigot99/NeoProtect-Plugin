package de.illigalspigot.neoprotect.bungee;

import de.illigalspigot.neoprotect.bungee.events.ConnectEvent;
import de.illigalspigot.neoprotect.bungee.manager.DirectConnectManager;
import de.illigalspigot.neoprotect.cpscounter.CPSCounter;
import de.illigalspigot.neoprotect.filemanagement.FileManagement;
import de.illigalspigot.neoprotect.vulnerability.Type;
import de.illigalspigot.neoprotect.vulnerability.Vulnerability;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class NeoProtect extends Plugin {
	
	private static NeoProtect instance;
	private FileManagement fileManagement;
	
	@Override
	public void onEnable() {
		instance = this;
		fileManagement = new FileManagement(true);
		new Thread(() -> {
			while(true) {
				DirectConnectManager.updateIpList();
				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		CPSCounter.clear();
		new Thread(() -> {
			while(true) {
				CPSCounter.clear();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}).start();
		this.getProxy().getPluginManager().registerListener(this, new ConnectEvent());
		if(BungeeCord.getInstance().getConfigurationAdapter().getBoolean("online_mode", false) == false) {
			Vulnerability.addVulnerability(Type.CRACKED, NeoProtect.getInstance().getFileManagement());
		}
	}
	
	@Override
	public void onDisable() {
		
	}

	public static NeoProtect getInstance() {
		return instance;
	}

	public FileManagement getFileManagement() {
		return fileManagement;
	}

}
