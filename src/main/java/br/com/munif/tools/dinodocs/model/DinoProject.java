package br.com.munif.tools.dinodocs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.*;

public class DinoProject implements Serializable {

	public String name = "projeto";

	private String destinationFolder = "/home/mgebara/particular/saida";

	private List<File> jarFolders = new ArrayList<File>();

	private List<File> classFolders = new ArrayList<File>();

	private List<Class> classes = new ArrayList<Class>();

	private List<String> interestClasses = new ArrayList<>();

	public String filtrosParaElementos = "br.com.uol";

	@JsonIgnore
	public URLClassLoader classLoader;

	public Map<Package, Set<Class>> packages = new HashMap<Package, Set<Class>>();

	public Map<Package, Set<String>> links = new HashMap<Package, Set<String>>();

	public Map<Class<?extends Annotation> , Set<Class<?>>> annotadeds = new HashMap<>();

	public Set<Inheritance> inheritances = new HashSet<>();

	public Set<String> annotations=new TreeSet<>();

	public DinoProject() {
		annotadeds.put(javax.persistence.Entity.class, new HashSet<Class<?>>());
		annotadeds.put(org.springframework.web.bind.annotation.RestController.class, new HashSet<Class<?>>());
		annotadeds.put(org.springframework.stereotype.Service.class, new HashSet<Class<?>>());
		annotadeds.put(org.springframework.stereotype.Component.class, new HashSet<Class<?>>());
		annotadeds.put(org.springframework.stereotype.Repository.class, new HashSet<Class<?>>());
		annotadeds.put(org.springframework.stereotype.Controller.class, new HashSet<Class<?>>());
		annotadeds.put(org.springframework.stereotype.Indexed.class, new HashSet<Class<?>>());
	}

	public boolean addJarFolder(String folder) {
		return addJarFolder(new File(folder));
	}

	public boolean addJarFolder(File folder) {
		return add(jarFolders, folder);
	}

	public boolean addClassFolder(String folder) {
		return addClassFolder(new File(folder));
	}

	public boolean addClassFolder(File folder) {
		return add(classFolders, folder);
	}

	public boolean addClass(Class clazz) {
		return add(classes, clazz);
	}

	private boolean add(List list, Object object) {
		if (!list.contains(object)) {
			list.add(object);
			return true;
		}
		return false;
	}

	public Set<Inheritance> getInheritances() {
		return inheritances;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the destinationFolder
	 */
	public String getDestinationFolder() {
		return destinationFolder;
	}

	/**
	 * @param destinationFolder the destinationFolder to set
	 */
	public void setDestinationFolder(String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	/**
	 * @return the jarFolders
	 */
	public List<File> getJarFolders() {
		return jarFolders;
	}

	/**
	 * @param jarFolders the jarFolders to set
	 */
	public void setJarFolders(List<File> jarFolders) {
		this.jarFolders = jarFolders;
	}

	/**
	 * @return the classFolders
	 */
	public List<File> getClassFolders() {
		return classFolders;
	}

	/**
	 * @param classFolders the classFolders to set
	 */
	public void setClassFolders(List<File> classFolders) {
		this.classFolders = classFolders;
	}

	/**
	 * @return the classes
	 */
	public List<Class> getClasses() {
		return classes;
	}

	/**
	 * @param classes the classes to set
	 */
	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

	/**
	 * @return the interestClasses
	 */
	public List<String> getInterestClasses() {
		return interestClasses;
	}

	/**
	 * @param interestClasses the interestClasses to set
	 */
	public void setInterestClasses(List<String> interestClasses) {
		this.interestClasses = interestClasses;
	}

	/**
	 * @return the filtrosParaElementos
	 */
	public String getFiltrosParaElementos() {
		return filtrosParaElementos;
	}

	/**
	 * @param filtrosParaElementos the filtrosParaElementos to set
	 */
	public void setFiltrosParaElementos(String filtrosParaElementos) {
		this.filtrosParaElementos = filtrosParaElementos;
	}

	/**
	 * @return the classLoader
	 */
	public URLClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * @param classLoader the classLoader to set
	 */
	public void setClassLoader(URLClassLoader classLoader) {
		this.classLoader = classLoader;
	}


	/**
	 * @return the packages
	 */
	public Map<Package, Set<Class>> getPackages() {
		return packages;
	}

	/**
	 * @param packages the packages to set
	 */
	public void setPackages(Map<Package, Set<Class>> packages) {
		this.packages = packages;
	}

	/**
	 * @return the links
	 */
	public Map<Package, Set<String>> getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(Map<Package, Set<String>> links) {
		this.links = links;
	}

	/**
	 * @return the annotadeds
	 */
	public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotadeds() {
		return annotadeds;
	}

	/**
	 * @param annotadeds the annotadeds to set
	 */
	public void setAnnotadeds(Map<Class<? extends Annotation>, Set<Class<?>>> annotadeds) {
		this.annotadeds = annotadeds;
	}

	@Override
	public String toString() {
		return "DinoProject{" + "name=" + name + ", destinationFolder=" + destinationFolder + ", jarFolders="
				+ jarFolders + ", classFolders=" + classFolders + ", classes=" + classes + ", interestClasses="
				+ interestClasses + ", filtrosParaElementos=" + filtrosParaElementos + ", classLoader=" + classLoader
				+  ", packages=" + packages + ", links=" + links + ", annotadeds="
				+ annotadeds + '}';
	}

	public Set<String> getAnnotations() {
		return annotations;
	}
}
