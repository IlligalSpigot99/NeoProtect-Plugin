package de.illigalspigot.neoprotect.filemanagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.conf.Configuration;
import net.md_5.bungee.conf.YamlConfig;
import net.md_5.bungee.config.ConfigurationProvider;

public class FileManagement {
	
	private String apiKey = "";
	private String serverID = "";
	
	public FileManagement(int a) {
		File folder = new File("plugins/NeoProtect");
		if(!folder.exists()) folder.mkdir();
		File file = new File("plugins/NeoProtect/config.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
				yml.set("API-Key", "YOUR KEY");
				yml.set("ServerID", "Server ID");
				yml.save(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
			apiKey = yml.getString("API-Key");
			serverID = yml.getString("ServerID");
			
		}
	}
	
	public FileManagement(boolean a) {
		File folder = new File("plugins/NeoProtect");
		if(!folder.exists()) folder.mkdir();
		File file = new File("plugins/NeoProtect/config.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				net.md_5.bungee.config.Configuration yml = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(file);
				yml.set("API-Key", "YOUR APIKEY");
				yml.set("ServerID", "Server ID");
				ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).save(yml, file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			try {
				net.md_5.bungee.config.Configuration yml = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(file);
				apiKey = yml.getString("API-Key");
				serverID = yml.getString("ServerID");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getAPIKey() {
		return apiKey;
	}
	
	public String getServerID() {
		return serverID;
	}

}
