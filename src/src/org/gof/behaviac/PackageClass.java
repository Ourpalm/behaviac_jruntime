package org.gof.behaviac;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.gof.behaviac.utils.Utils;

/**
 * 通过给定的包名，来得到包下的全部CLASS类。
 */
public class PackageClass {
	private static String[] PATHS; // 类根目录地址
	// 忽略的类 有些类Class.forName()时会报错...
	private static List<String> ignoreClass = new ArrayList<>();

	static {
		// 初始化类根目录地址
		initPaths();
	}

	/**
	 * 获取包目录下的全部CLASS
	 * 
	 * @param packageNames
	 * @return
	 */
	public static Set<Class<?>> find() {
		// return find("");
		return getPackageClasses("org.gof");
	}


	/**
	 * 将从配置文件中读取的形如org.gof转成org/gof路径格式
	 * 
	 * @param packageName
	 * @return
	 */
	public static String packageToPath(String packageName) {
		return packageName.replaceAll("\\.", "/");
	}

	/**
	 * 忽略的类 有些类Class.forName()时会报错或有其他问题
	 * 
	 * @param ignoreClass
	 */
	public static void ignoreClass(List<Class<?>> ignoreClass) {
		PackageClass.ignoreClass.clear();
		for (Class<?> c : ignoreClass) {
			PackageClass.ignoreClass.add(c.getName());
		}
	}

	/**
	 * 获取符合包限制的文件
	 * 
	 * @return
	 */
	private static List<File> findFiles(String packageName) {
		List<File> result = new ArrayList<>();
		for (String path : PATHS) {
			// 包对应的文件目录
			File dir = new File(path, packageToPath(packageName));

			// 从指定的目录中查找目录和以.class结尾的文件
			File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if (pathname.isDirectory())
						return true;
					return pathname.getName().matches(".*\\.class$");
				}
			});
			if (files == null)
				files = new File[0];

			// 记录本地址下符合的类文件
			for (File f : files) {
				result.add(f);
			}
		}

		return result;
	}

	/**
	 * 初始化类根目录地址
	 * 
	 * @return
	 */
	private static void initPaths() {
		try {
			String pathStr = System.getProperty("java.class.path");
			if (Utils.isWindows()) {
				PATHS = pathStr.split(";");
			} else {
				PATHS = pathStr.split(":");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读取包内所有的类获取class对象，并根据指定的条件过滤
	 * 
	 * @param pname
	 * @return
	 */
	public static Set<Class<?>> getPackageClasses(String pname) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		String packageDirName = pname.replace('.', '/');

		try {
			Enumeration<URL> dirs = cl.getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();

				String protocol = url.getProtocol();

				if ("file".equals(protocol))
					findByFile(cl, pname, URLDecoder.decode(url.getFile(), "utf-8"), classes);
				else if ("jar".equals(protocol))
					findInJar(cl, pname, packageDirName, url, classes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 从文件获取java类
	 * 
	 * @param cl
	 * @param packageName
	 * @param filePath
	 * @param classes
	 */
	private static void findByFile(ClassLoader cl, String packageName, String filePath, Set<Class<?>> classes) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory())
			return;

		File[] dirFiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() || file.getName().endsWith(".class");
			}
		});

		for (File file : dirFiles) {
			if (file.isDirectory()) {
				findByFile(cl, packageName + "." + file.getName(), file.getAbsolutePath(), classes);
			} else {
				try {
					String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
					Class<?> clazz = Class.forName(className);
//					Class<?> clazz = cl.loadClass(packageName + "."
//							+ file.getName().substring(0, file.getName().length() - 6));
					classes.add(clazz);
				} catch (ExceptionInInitializerError e) {
					// 这个没关系 是无法初始化类
				} catch (NoClassDefFoundError e) {
					// 这个没关系 是无法初始化类
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取jar中的java类
	 * 
	 * @param cl
	 * @param pname
	 * @param packageDirName
	 * @param url
	 * @param classes
	 */
	private static void findInJar(ClassLoader cl, String pname, String packageDirName, URL url, Set<Class<?>> classes) {
		try {
			JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();

				if (entry.isDirectory())
					continue;

				String name = entry.getName();

				if (name.charAt(0) == '/')
					name = name.substring(0);

				if (name.startsWith(packageDirName) && name.contains("/") && name.endsWith(".class")) {
					name = name.substring(0, name.length() - 6).replace('/', '.');
					try {
						Class<?> clazz = cl.loadClass(name);

						classes.add(clazz);
					} catch (Throwable e) {
						System.out.println("无法直接加载的类：" + name);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}