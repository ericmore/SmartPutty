package com.sp.control;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
			InputStreamReader in = new InputStreamReader(new ClassPathResource("VERSION").getInputStream());
			br = new BufferedReader(in);
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
