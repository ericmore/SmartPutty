package Control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SmartPuttyVersion {
	private static String version;


	public static String getSmartPuttyVersion() {
		if (version == null) {
			ReadVersion();
		}
		return version;
	}

	public static void ReadVersion() {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("VERSION"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			version = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
