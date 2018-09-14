package com.gwg.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.List;

public class FileUtils {
	static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	private static final String ROOT = "microservice-payment-service/";

	public static boolean isFileExist(String filePath) {
		try {
			File file = new File(filePath);
			return file.exists();
		} catch (Exception e) {
			return false;
		}
	}

	public static void copyFile(File file, String copy) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		PrintWriter writer = new PrintWriter(copy, "UTF-8");
		String line;
		while ((line = reader.readLine()) != null) {
			writer.println(line);
		}
		writer.close();
		reader.close();
	}

	public static void createDirs(String path) throws Exception {
		File file = new File(path);
		file.mkdirs();
	}

	public static String getProjectDir() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		return path.substring("file:".length(), path.indexOf("bin/"));
	}

	public static String getClassDir() {
		return getProjectDir() + "src/main/java/";
	}

	public static String getRootDir() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		return path.substring("file:".length(), path.indexOf(ROOT));
	}

	public static String getResourceDir(String file) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(file);
		if (url != null) {
			String path = url.toString();
			return path.substring("file:".length(), path.indexOf(file));
		}
		return null;
	}

	/**
	 * 获取程序代码工作目录
	 * 
	 * @return
	 */
	public static String getWorkPath() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("");

		if (url != null) {
			String path = url.toString();
			return path.substring("file:".length());
		}
		return "";
	}

	/**
	 * 获取资源所在目录
	 * 
	 * @return
	 */
	public static String getResourcePath(String file) {
		// 获取当前目录
		URL url = FileUtils.class.getResource(file);
		// url =
		// FileUtils.class.getProtectionDomain().getCodeSource().getLocation();
		if (url != null) {
			String path = url.toString();
			if (path.startsWith("jar:file:")) {
				return path.substring("jar:file:".length());
			}
			if (path.endsWith("/classes/main/")) {
				// idea
				path = path.substring("file:".length(), path.indexOf("build"));
				path = path + "build/resources/main/";
				return path;
			} else if (path.endsWith("/classes/test/")) {
				// junit test
				path = path.substring("file:".length(), path.indexOf("build"));
				path = path + "build/resources/main/";
				return path;
			} else {
				// eclipse + 线上
				return path.substring("file:".length());
			}
		}
		return "";
	}

	public static String getMainResourceDir() {
		return getProjectDir() + "src/main/resources/";
	}

	public static String getTestResourceDir() {
		return getProjectDir() + "src/test/resources/";
	}

	public static String getRelativeProjectDir() {
		return "";
	}

	public static String getRelativeClassDir() {
		return getRelativeProjectDir() + "src/main/java/";
	}

	public static String getRelativeMainResourceDir() {
		return getRelativeProjectDir() + "src/main/resources/";
	}

	public static String getRelativeTestResourceDir() {
		return getRelativeProjectDir() + "src/test/resources/";
	}

	public static void searchFile(File file, String pathContains, List<File> result) {
		if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			if (fileList != null) {
				for (File sudFile : fileList) {
					searchFile(sudFile, pathContains, result);
				}
			}
		} else if (file.getAbsolutePath().contains(pathContains)) {
			result.add(file);
		}
	}

	public static String readFileContent(File file) throws Exception {
		StringBuffer buf = new StringBuffer();

		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		for (String line : lines) {
			buf.append(line);
		}
		return buf.toString();
	}

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	public static boolean createFile(File file) throws IOException {
		if (!file.exists()) {
			makeDir(file.getParentFile());
		}
		return file.createNewFile();
	}
}
