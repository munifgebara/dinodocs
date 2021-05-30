package br.com.munif.tools.dinodocs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author munif
 */
public class GebUtils {

	public static List<Field> getAllFields(Class<?> c) {
		ArrayList<Field> arrayList = new ArrayList<Field>();
		if (!c.isEnum()&& c.getSuperclass() != Object.class) {
			arrayList.addAll(getAllFields(c.getSuperclass()));
		}
		arrayList.addAll(Arrays.asList(c.getDeclaredFields()));
		return arrayList;
	}

	public static String removeNotNumbers(String numero) {
		if (numero == null) {
			return null;
		}
		return numero.replaceAll("[\\D]", "");
	}

	public static void removeNumbersFromAtrributes(Object obj, String... atributos) {
		Class clazz = obj.getClass();
		for (String attr : atributos) {
			try {
				Field att = clazz.getDeclaredField(attr);
				att.setAccessible(true);
				att.set(obj, removeNotNumbers((String) att.get(obj)));
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
				Logger.getLogger(GebUtils.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public static Class<?> inferGenericType(Class<?> clazz) {
		return inferGenericType(clazz, 0);
	}

	public static Class<?> inferGenericType(Class<?> clazz, int index) {
		Type superClass = clazz.getGenericSuperclass();
		return (Class<?>) ((ParameterizedType) superClass).getActualTypeArguments()[index];
	}

	public static String firstTiny(String s) {
		String first = s.substring(0, 1).toLowerCase();
		return first + s.substring(1);

	}

	public static String windowsSafe(String s) {
		return s.replaceAll("\\\\", "/");
	}

	private static Set<File> scanFolders(File startFolder, String startExpression, String endExpression) {
		Set<File> selectedFiles = new HashSet<File>();
		if (startFolder != null && startFolder.listFiles() != null) {
			File[] listFiles = startFolder.listFiles();
			for (File f : listFiles) {
				if (f.isFile() && f.getName().startsWith(startExpression) && f.getName().endsWith(endExpression)) {
					selectedFiles.add(f);
				} else if (f.isDirectory()) {
					Set<File> scanFolders = scanFolders(f, startExpression, endExpression);
					selectedFiles.addAll(scanFolders);
				}
			}
		}
		return selectedFiles;
	}

	public static URLClassLoader createClassLoader(Collection<File> classesFolders, Collection<File> jarFolders) {
		return createClassLoader(classesFolders.toArray(new File[0]), jarFolders.toArray(new File[0]));

	}

	public static URLClassLoader createClassLoader(File[] classesFolders, File[] jarFolders) {
		List<File> elementos = new ArrayList<File>();
		for (File jarFolder : jarFolders) {
			Set<File> folders = scanFolders(jarFolder, "", "jar");
			elementos.addAll(folders);
		}
		elementos.addAll(Arrays.asList(classesFolders));
		List<URL> checkedURLs = new ArrayList<>();

		for (File elemento : elementos) {
			try {
				URL url = elemento.toURI().toURL();
				checkedURLs.add(url);
			} catch (Exception ex) {
				System.out.println(elemento.toString() + " invalido " + ex.toString());
			}
		}
		URL[] runtimeUrls = checkedURLs.toArray(new URL[0]);
		return new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
	}

	public static List<Class<?>> scanClasses(URLClassLoader urlClassLoader) {
		List<Class<?>> toReturn = new ArrayList<>();
		try {
			Enumeration<URL> resources = urlClassLoader.getResources("");
			while (resources.hasMoreElements()) {
				File file = new File(resources.nextElement().getFile());
				System.out.println(file);
				toReturn.addAll(scanFolder(file, file, urlClassLoader));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return toReturn;
	}

	private static String getClassName(File f, File baseFolder) {
		String compleName = GebUtils.windowsSafe(f.getAbsolutePath());
		String baseFolderName = GebUtils.windowsSafe(baseFolder.getAbsolutePath());
		return compleName.replaceFirst(baseFolderName, "").replace(".class", "").replaceAll("/", ".").replaceFirst(".",
				"");
	}

	public static List<Class<?>> scanFolder(File folder, File baseFolder, URLClassLoader classLoader) {
		List<Class<?>> toReturn = new ArrayList<>();
		File[] fs = folder.listFiles();
		if (fs == null) {
			return toReturn;
		}
		for (File f : fs) {
			if (f.isDirectory()) {
				toReturn.addAll(scanFolder(f, baseFolder, classLoader));
			} else {
				String name = f.getName();
				if (name.endsWith(".class")) {
					String className = getClassName(f, baseFolder);
					try {
						toReturn.add(classLoader.loadClass(className));
					} catch (ClassNotFoundException ex) {
						Logger.getLogger(GebUtils.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}
		return toReturn;
	}

	public static void fillColectionsWithEmpty(Object obj) {
		try {
			List<Field> fields = getAllFields(obj.getClass());
			for (Field f : fields) {
				if (List.class.isAssignableFrom(f.getType())) {
					f.setAccessible(true);
					if (f.get(obj) == null) {
						f.set(obj, Collections.EMPTY_LIST);
					}
				} else if (Set.class.isAssignableFrom(f.getType())) {
					f.setAccessible(true);
					if (f.get(obj) == null) {
						f.set(obj, Collections.EMPTY_SET);
					}
				} else if (Map.class.isAssignableFrom(f.getType())) {
					f.setAccessible(true);
					if (f.get(obj) == null) {
						f.set(obj, Collections.EMPTY_MAP);
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String firstCapital(String s) {
		String first = s.substring(0, 1).toUpperCase();
		return first + s.substring(1);
	}

	public static List<Class<?>> geraLinkedTo(List<Class<?>> interest, List<Class<?>> all) {
		Set<Class<?>> founded = new HashSet<>();
		founded.addAll(interest);
		interest.forEach(c -> {
			founded.add(c.getSuperclass() != Object.class ? c.getSuperclass() : c);
			Field[] declaredFields = c.getDeclaredFields();
			for (Field f : declaredFields) {
				Class<?> fieldType = f.getType();
				if (all.contains(fieldType)) {
					founded.add(fieldType);
				} else if (Collection.class.isAssignableFrom(fieldType)) {
			        ParameterizedType type = (ParameterizedType) f.getGenericType();
	                for (Type typeArgument:type.getActualTypeArguments()) {
	                	if (all.contains(typeArgument)) {
	    				founded.add((Class<?>) typeArgument);
	    				}   	
	                }
				}
			}
		});
		return new ArrayList<Class<?>>(founded);

	}

	public static List<Class<?>> geraLinkedTo(Class<?> interest, List<Class<?>> all) {
		List<Class<?>> arrayList = new ArrayList<Class<?>>();
		arrayList.add(interest);
		return geraLinkedTo(arrayList, all);
	}

	private static boolean isOnlyClass(Class clazz) {
		return clazz.isAnnotation() || clazz.isAnonymousClass() || clazz.isArray() || clazz.isEnum()
				|| clazz.isInterface() || clazz.isLocalClass() || clazz.isMemberClass() || clazz.isPrimitive()
				|| clazz.isSynthetic();

	}

}
