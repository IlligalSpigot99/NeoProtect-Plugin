package de.illigalspigot.neoprotect.firewall;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

import de.illigalspigot.neoprotect.filemanagement.FileManagement;

public class Firewall {
	
	private static ArrayList<Insert> firewallWhitelist = new ArrayList<>();
	private static ArrayList<Insert> firewallBlacklist = new ArrayList<>();
	
	public static void addFirewall(Type type, FileManagement fileManagement, String ip) {
		try {
			String text = "https://api.neoprotect.net/v1/firewall/" + fileManagement.getServerID() + "/" + type.toString().toUpperCase();
			URL url = new URL(text);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
			conn.setRequestProperty("Authorization", "Bearer " + fileManagement.getAPIKey());
			conn.setRequestProperty("content-type", "application/json");
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			HashMap<String, Object> vulnerability = new HashMap<>();
			vulnerability.put("ipv4", ip);
			Gson gson = new Gson();
			osw.write(gson.toJson(vulnerability));
			osw.flush();
			osw.close();
			conn.getResponseCode();
			conn.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateFirewall(Type type, FileManagement fileManagement) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String string3;
            String text = "https://api.neoprotect.net/v1/firewall/" + fileManagement.getServerID() + "/" + type.toString().toUpperCase();
            URL url = new URL(text);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
			conn.setRequestProperty("content-type", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + fileManagement.getAPIKey());
			conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 502) return;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((string3 = in.readLine()) != null) {
                stringBuilder.append(string3);
            }
            in.close();
            JsonElement jsonElement = new JsonParser().parse(String.valueOf(stringBuilder));
			Gson gson = new Gson();
			ArrayList<LinkedTreeMap<String, Object>> firewalls = gson.fromJson(jsonElement, ArrayList.class);
			firewallWhitelist.clear();
			firewallBlacklist.clear();
			for(int i = 0; i < firewalls.size(); i++) {
				if(Type.valueOf(firewalls.get(i).get("type") + "") == Type.BLACKLIST) {
					firewallBlacklist.add(new Insert(Integer.valueOf(firewalls.get(i).get("id") + ""), Type.valueOf(firewalls.get(i).get("type") + ""), firewalls.get(i).get("ip") + "", firewalls.get(i).get("createdAt") + ""));
					
				}else if(Type.valueOf(firewalls.get(i).get("type") + "") == Type.WHITELIST) {
					firewallWhitelist.add(new Insert(Integer.valueOf(firewalls.get(i).get("id") + ""), Type.valueOf(firewalls.get(i).get("type") + ""), firewalls.get(i).get("ip") + "", firewalls.get(i).get("createdAt") + ""));
					
				}
				
			}
            in.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static void removeFirewall(Type type, FileManagement fileManagement, String ip) {
		try {
			String id = "";
			if(type == Type.BLACKLIST) {
				for(Insert insert : firewallBlacklist) {
					if(insert.getIp() == ip) {
						id = "" + insert.getId();
					}
				}
			}else if(type == Type.WHITELIST) {
					for(Insert insert : firewallWhitelist) {
						if(insert.getIp() == ip) {
							id = "" + insert.getId();
						}
					}
				}
			
			if(id == "") return;
			String text = "https://api.neoprotect.net/v1/firewall/" + fileManagement.getServerID() + "/" + id;
			URL url = new URL(text);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
			conn.setRequestProperty("Authorization", "Bearer " + fileManagement.getAPIKey());
			conn.setRequestMethod("DELETE");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
