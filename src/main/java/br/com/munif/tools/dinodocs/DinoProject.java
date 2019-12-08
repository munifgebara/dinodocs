package br.com.munif.tools.dinodocs;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DinoProject {

    public String name;

    private String destinationFolder = "/home/munif/dinodocs/";

    private List<File> jarFolders = new ArrayList<File>();

    private List<File> classFolders = new ArrayList<File>();

    private List<Class> classes = new ArrayList<Class>();

    public String filtrosParaElementos = "br.com";

    public URLClassLoader classLoader;

    public HashSet<Class> entidades = new HashSet<>();

    public Map<Package, Set<Class>> packages = new HashMap<Package, Set<Class>>();

    public Map<Package, Set<String>> links = new HashMap<Package, Set<String>>();

    public Map<Class, Set<Class>> annotadeds = new HashMap<Class, Set<Class>>();

    public DinoProject() {
        annotadeds.put(javax.persistence.Entity.class, new HashSet<Class>());
        annotadeds.put(org.springframework.web.bind.annotation.RestController.class, new HashSet<Class>());
        annotadeds.put(org.springframework.stereotype.Service.class, new HashSet<Class>());
        annotadeds.put(org.springframework.stereotype.Component.class, new HashSet<Class>());;
        annotadeds.put(org.springframework.stereotype.Repository.class, new HashSet<Class>());;
        annotadeds.put(org.springframework.stereotype.Controller.class, new HashSet<Class>());;
        annotadeds.put(org.springframework.stereotype.Indexed.class, new HashSet<Class>());;
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

    public String getNome() {
        return name;
    }

    public List<File> getJarFolders() {
        return jarFolders;
    }

    public List<File> getClassFolders() {
        return classFolders;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public HashSet<Class> getEntidades() {
        return entidades;
    }

    public void setEntidades(HashSet<Class> entidades) {
        this.entidades = entidades;
    }

    public Map<Package, Set<Class>> getPackages() {
        return packages;
    }

    public void setPackages(Map<Package, Set<Class>> packages) {
        this.packages = packages;
    }

    public Map<Package, Set<String>> getLinks() {
        return links;
    }

    public void setLinks(Map<Package, Set<String>> links) {
        this.links = links;
    }

    public Map<Class, Set<Class>> getAnnotadeds() {
        return annotadeds;
    }

    public void setAnnotadeds(Map<Class, Set<Class>> annotadeds) {
        this.annotadeds = annotadeds;
    }

    public String getFiltrosParaElementos() {
        return filtrosParaElementos;
    }

    public void setFiltrosParaElementos(String filtrosParaElementos) {
        this.filtrosParaElementos = filtrosParaElementos;
    }

    @Override
    public String toString() {
        return "DinoProject{\n" + "name=" + name + ",\n destinationFolder=" + destinationFolder + ",\n jarFolders=" + jarFolders + ",\n classFolders=" + classFolders + ",\n classes=" + classes + ",\n filtrosParaElementos=" + filtrosParaElementos + ",\n classLoader=" + classLoader + ",\n entidades=" + entidades + ",\n packages=" + packages + ",\n links=" + links + ",\n annotadeds=" + annotadeds + '}';
    }

}
