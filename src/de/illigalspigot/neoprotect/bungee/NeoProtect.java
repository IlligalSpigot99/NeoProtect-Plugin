package de.illigalspigot.neoprotect.bungee;

import de.illigalspigot.neoprotect.bungee.events.ConnectEvent;
import de.illigalspigot.neoprotect.bungee.manager.DirectConnectManager;
import de.illigalspigot.neoprotect.filemanagement.FileManagement;
import net.md_5.bungee.api.plugin.Plugin;

public class NeoProtect extends Plugin {
	
	private static NeoProtect instance;
	private FileManagement fileManagement;
	
	@Override
	public void onEnable() {
		instance = this;
		fileManagement = new FileManagement(true);
		this.getProxy().getPluginManager().registerListener(this, new ConnectEvent());
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
