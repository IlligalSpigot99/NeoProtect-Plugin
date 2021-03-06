package de.illigalspigot.neoprotect.spigot.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import de.illigalspigot.neoprotect.cpscounter.CPSCounter;
import de.illigalspigot.neoprotect.spigot.NeoProtect;
import de.illigalspigot.neoprotect.vulnerability.Type;
import de.illigalspigot.neoprotect.vulnerability.Vulnerability;

public class DirectConnectManager {
	
	private static ArrayList<String> ips = new ArrayList<>();
	
	public static boolean isValidIP(String ip) {
		String ipMinimized = ip.replaceAll("/", "").split(":")[0];
		if(ips.contains(ipMinimized)) {
			return true;
		}
		Vulnerability.addVulnerability(Type.DIRECT_IP, NeoProtect.getInstance().getFileManagement());
		CPSCounter.add();
		if(CPSCounter.get() >= 25) {
			Vulnerability.addVulnerability(Type.DIRECT_ATTACK, NeoProtect.getInstance().getFileManagement());
		}
		return false;
	}
	
	public static void updateIpList() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String string3;
            String text = "https://api.neoprotect.net/v1/public/servers";
            URL url = new URL(text);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            if(conn.getResponseCode() == 502) return;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((string3 = in.readLine()) != null) {
                stringBuilder.append(string3);
            }
            in.close();
            JsonElement jsonElement = new JsonParser().parse(String.valueOf(stringBuilder));
            ips.clear();
            String ipss = jsonElement.toString();
            ipss = ipss.replaceAll(Pattern.quote("["), "").replaceAll(Pattern.quote("]"), "").replaceAll(Pattern.quote("\""), "").replaceAll(Pattern.quote(" "), "");
            String[] ipList = ipss.split(",");
            for(String ip : ipList) {
            	ips.add(ip);
            }
            in.close();
        } catch (Exception e) {
        }
	}

}
