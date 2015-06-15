package com.big.instrumentation.scan;

import com.big.instrumentation.annotation.Transformer;
import org.objectweb.asm.ClassReader;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by patrick.kleindienst on 11.06.2015.
 */
public class TransformerClasspathScanner {

	public static void loadTransformerClasses() {
		for (URL url : getRootURLs()) {
			File file = new File(url.getPath());
			if (file.isDirectory()) {
				visitFile(file);
			} else {
				visitJAR(url);
			}
		}

	}

	private static List<URL> getRootURLs() {
		List<URL> result = new ArrayList<>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		while (classLoader != null) {
			if (classLoader instanceof URLClassLoader) {
				URL[] urls = ((URLClassLoader) classLoader).getURLs();
				result.addAll(Arrays.asList(urls));
			}

			classLoader = classLoader.getParent();
		}
		return result;
	}

	private static void visitFile(File file) {
		if (file.isDirectory()) {
			final File[] children = file.listFiles();
			if (children != null) {
				for (File child : children) {
					visitFile(child);
				}
			}
		} else if (file.getName().endsWith(".class")) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				handleClass(fileInputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void visitJAR(URL url) {
		try {
			JarInputStream jarInputStream = new JarInputStream(url.openStream());
			JarEntry jarEntry;
			while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
				if (jarEntry.getName().endsWith(".class")) {
					// handleClass(new FileInputStream(new File(url.getPath() +
					// "!\\" + jarEntry.getName())));
					handleClass(jarInputStream);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleClass(InputStream inputStream) throws IOException {
		TransformerClassVisitor classVisitor = new TransformerClassVisitor();
		new ClassReader(inputStream).accept(classVisitor, 0);
	}
}
