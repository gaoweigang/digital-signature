package com.gwg.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PropertiesFile extends PropertiesMap {

	public PropertiesFile(String fileName) {
		InputStream in = null;
		BufferedReader read = null;
		try {
			in = PropertiesFile.class.getResourceAsStream(fileName);
			read = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = read.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0 || line.startsWith("#")) {
					continue;
				}
				int pos = line.indexOf("=");
				String key = pos >= 0 ? line.substring(0, pos).trim() : line;
				String value = pos >= 0 ? line.substring(pos + 1).trim() : "";
				properties.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (read != null) {
				try {
					read.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}
