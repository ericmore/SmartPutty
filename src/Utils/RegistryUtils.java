package Utils;

import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities to deal with Windows registry.
 * 
 * @author Carlos SS
 */
public class RegistryUtils {
	// Putty session to be used on SmartPutty:
	public static String SMARTPUTTY_SESSION = "SmartPutty_Session";

	static class StreamReader extends Thread {
		private InputStream is;
		private StringWriter sw = new StringWriter();

		public StreamReader(InputStream is) {
			this.is = is;
		}

		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {

			}
		}

		public String getResult() {
			return sw.toString();
		}
	}

	/**
	 * Create all needed Putty registry keys.
	 */
	public static void createPuttyKeys() {
		try {
			 initPuttyDefaultSettings();
			 for(Map.Entry<String, Object> e : readPuttyDefaultSettings().entrySet()){
				 System.out.println(e.getKey() + " : " + e.getValue());
			 }
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static void initPuttyDefaultSettings() {
		setRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "WarnOnClose", "REG_DWORD",
				"0");
		setRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "LineCodePage", "REG_SZ",
				"UTF-8");
		setRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "ScrollbackLines", "REG_DWORD",
				"5000");
		setRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "WinTitle", "REG_SZ", " ");
		setRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "NoRemoteWinTitle", "REG_DWORD",
				"1");
		setRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "BlinkCur", "REG_DWORD", "1");
	}

	public static HashMap<String, Object> readPuttyDefaultSettings() {
		HashMap<String, Object> ret = new HashMap<String, Object>();
		ret.put("WarnOnClose",
				readRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "WarnOnClose"));
		ret.put("LineCodePage",
				readRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "LineCodePage"));
		ret.put("ScrollbackLines",
				readRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "ScrollbackLines"));
		ret.put("WinTitle",
				readRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "WinTitle"));
		ret.put("NoRemoteWinTitle",
				readRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "NoRemoteWinTitle"));
		ret.put("BlinkCur",
				readRegistry("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions\\Default%20Settings", "BlinkCur"));
		return ret;
	}

	/**
	 * Get all Putty sessions.
	 * 
	 * @return
	 */

	public static List<String> getAllPuttySessions() {
		List<String> list = new ArrayList<String>();
		String[] locations = readRegistryList("HKCU\\Software\\SimonTatham\\PuTTY\\Sessions");
		for (String location : locations) {
			String[] pieces = location.split("\\\\");
			String name = pieces[pieces.length - 1].trim();
			if (!name.equals("Default%20Settings") && !name.equals(""))
				list.add(name);
		}
		return list;
	}

	/**
	
	 */
	public static final Object readRegistry(String location, String key) {
		Object obj = null;
		try {
			String cmd = String.format("reg query %s /v %s", location, key);
			Process process = Runtime.getRuntime().exec(cmd);
			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
			String output = reader.getResult();
			String[] parsed = output.trim().split("\\s+");

			

			if (parsed.length == 4) {
				switch (parsed[2]) {
				case "REG_SZ":
					obj = parsed[3];
					break;
				case "REG_DWORD":
					obj = Integer.decode(parsed[3]);
					break;
				}
			} else if (parsed.length == 3) {
				obj = "";
			} else {
				System.out.println("Error occur parsing registry: " + cmd);
				System.out.println(Arrays.deepToString(parsed));
			}
			
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		}
		return obj;
	}

	public static final void setRegistry(String location, String key, String type, String value) {

		try {
			String cmd = String.format("reg add %s /v %s /t %s /f /d %s", location, key, type, value);
			Process process = Runtime.getRuntime().exec(cmd);
			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static final String[] readRegistryList(String location) {
		try {
			String cmd = String.format("reg query %s ", location);
			Process process = Runtime.getRuntime().exec(cmd);
			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
			String output = reader.getResult();
			String[] parsed = output.trim().split("\\s+");
			return parsed;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
