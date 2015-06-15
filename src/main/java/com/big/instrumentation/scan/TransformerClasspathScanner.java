package com.big.instrumentation.scan;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.objectweb.asm.ClassReader;

/**
 * The purpose of this class is to scan the whole classpath for .class-files and
 * apply {@link TransformerClassVisitor} to them. The itself implementation is
 * heavily based on Arno Haase's JAXenter article <a href=
 * "https://jaxenter.de/classpath-scan-im-eigenbau-aus-der-java-trickkiste-12963"
 * >Classpath-Scan im Eigenbau [Aus der Java-Trickkiste]</a>.<br/>
 *
 * Created by patrick.kleindienst on 11.06.2015.
 * 
 * @author patrick.kleindienst
 */
public class TransformerClasspathScanner {

	// #####################################################
	// # STATIC METHODS #
	// #####################################################

	/**
	 * Step through every URL and check if it points to a JAR or a file.
	 */
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

	/**
	 * Step through available classloader hierarchy and collect their classpath
	 * URLs.
	 * 
	 * @return {@link List} containing every classloader's classpath URLs.
	 */
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

	/**
	 * Check if a certain {@link File} ends with .class, i.e. is a class file
	 * containing bytecode. If so, create a {@link FileInputStream} and dispatch
	 * it to {@link TransformerClasspathScanner#handleClass(InputStream)}. If
	 * <code>file</code> references a directory, the method is called
	 * recursively.
	 * 
	 * @param file
	 *            {@link File} a certain classpath URL points to
	 */
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

	/**
	 * Step through the content of a JAR file referenced by <code>url</code>. If
	 * an entry inside the JAR is actually a class file, dispatch the JARs
	 * {@link JarInputStream} to
	 * {@link TransformerClasspathScanner#handleClass(InputStream)}.
	 * 
	 * @param url
	 *            {@link URL} pointing to a JAR
	 */
	private static void visitJAR(URL url) {
		try {
			JarInputStream jarInputStream = new JarInputStream(url.openStream());
			JarEntry jarEntry;
			while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
				if (jarEntry.getName().endsWith(".class")) {
					handleClass(jarInputStream);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method takes an {@link InputStream} as a parameter and applies the
	 * {@link TransformerClassVisitor} to every class file that can be read by
	 * this <code>inputStream</code>.
	 * 
	 * @param inputStream
	 *            {@link InputStream} reading in class files
	 * @throws IOException
	 */
	private static void handleClass(InputStream inputStream) throws IOException {
		TransformerClassVisitor classVisitor = new TransformerClassVisitor();
		new ClassReader(inputStream).accept(classVisitor, 0);
	}
}
