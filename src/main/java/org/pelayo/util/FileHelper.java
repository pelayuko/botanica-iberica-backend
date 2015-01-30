package org.pelayo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileHelper {

	private static Random rnd = new Random();

	public static File randomFileInHierarchy(String path) {
		File folder = new File(path);
		if (!folder.isDirectory()) {
			throw new RuntimeException("Is not a directory!: " + path);
		}
		return randomFileInHierarchy(folder);
	}

	public static File randomFileInHierarchy(File directory) {
		List<File> directories = new ArrayList<File>();
		directories.add(directory);
		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				directories.add(f);
			}
		}

		List<File> files = new ArrayList<File>();
		boolean found = false;
		int retries = 0;
		while (found == false) {
			Integer randomDir = rnd.nextInt(directories.size());
			File dir = directories.get(randomDir);
			for (File f : dir.listFiles()) {
				if (isPhoto(f)) {
					files.add(f);
					found = true;
				} else if (f.isDirectory()) {
					directories.add(f);
				} else {
					retries++;
					if (retries == 300) {
						throw new RuntimeException("Max retries reached!!");
					}
				}
			}
		}
		Integer randomFile = rnd.nextInt(files.size());
		File file = files.get(randomFile);
		return file;
	}

	public static File firstFileInHierarchy(String path) {
		File folder = new File(path);
		return firstFileInHierarchy(folder);
	}

	private static File firstFileInHierarchy(File diectory) {
		File[] listOfFiles = diectory.listFiles();

		if (listOfFiles.length > 0) {
			if (listOfFiles[0].isFile()) {
				return listOfFiles[0];
			} else if (listOfFiles[0].isDirectory()) {
				return firstFileInHierarchy(listOfFiles[0]);
			} else {
				throw new RuntimeException(
						"File is not file neither directory!");
			}
		}

		return null;
	}

	private static boolean isPhoto(File f) {
		return f.isFile() && f.getName().endsWith("jpg");
	}
}
