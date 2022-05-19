package de.illigalspigot.neoprotect.spigot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.illigalspigot.neoprotect.cpscounter.CPSCounter;
import de.illigalspigot.neoprotect.filemanagement.FileManagement;
import de.illigalspigot.neoprotect.firewall.Firewall;
import de.illigalspigot.neoprotect.spigot.consolefilter.OldEngine;
import de.illigalspigot.neoprotect.spigot.haproxyreverser.HAProxyReverser;
import de.illigalspigot.neoprotect.spigot.manager.DirectConnectManager;
import de.illigalspigot.neoprotect.vulnerability.Type;
import de.illigalspigot.neoprotect.vulnerability.Vulnerability;

public class NeoProtect extends JavaPlugin {
	
	private static NeoProtect instance;
	private FileManagement fileManagement;
	public ArrayList<String> filterConsoleMessages = new ArrayList<>();
	
	public void onLoad() {
		instance = this;
		new HAProxyReverser();
	}
	
	public void onEnable() {
		fileManagement = new FileManagement(1);
		Firewall.updateFirewall(de.illigalspigot.neoprotect.firewall.Type.WHITELIST, fileManagement);
		filterConsoleMessages.add("Failed to initialize a channel. Closing: ");
		new OldEngine(instance).hideConsoleMessages();
		new Thread(() -> {
			while(true) {
				DirectConnectManager.updateIpList();
				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		if(!Bukkit.getServer().getOnlineMode()) {
			Vulnerability.addVulnerability(Type.CRACKED, NeoProtect.getInstance().getFileManagement());
		}
	}
	
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
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

	public static NeoProtect getInstance() {
		return instance;
	}

	public FileManagement getFileManagement() {
		return fileManagement;
	}

}
