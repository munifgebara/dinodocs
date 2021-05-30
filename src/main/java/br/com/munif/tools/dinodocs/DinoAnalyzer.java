package br.com.munif.tools.dinodocs;

import br.com.munif.tools.dinodocs.model.DinoProject;
import br.com.munif.tools.dinodocs.model.Inheritance;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;

import javax.persistence.Entity;

public class DinoAnalyzer {

	private Set<Class> generated;

	private DinoProject dinoProject;

	public DinoAnalyzer(DinoProject dinoProject) throws IOException {
		this.dinoProject = dinoProject;
		URLClassLoader urlClassLoader = GebUtils.createClassLoader(this.dinoProject.getClassFolders(),
				this.dinoProject.getJarFolders());

		List<Class<?>> allClasses = GebUtils.scanClasses(urlClassLoader);
		this.dinoProject.getClasses().addAll(allClasses);
		Map<Package, Set<Class>> packages = this.dinoProject.getPackages();
		Set<Inheritance> inheritances = this.dinoProject.getInheritances();
		for (Class c:allClasses){
			if (c.getSuperclass()==null){
				continue;
			}
			if (c.getSuperclass().getName().startsWith(this.dinoProject.getFiltrosParaElementos())){

				inheritances.add(new Inheritance(c.getSuperclass(),c));

			}
			Arrays.asList(c.getDeclaredAnnotations()).forEach(aaa->this.dinoProject.getAnnotations().add(aaa.annotationType().getName()));




			Package aPackage = c.getPackage();
			Set<Class> cs;
			if (packages.containsKey(aPackage)){
				cs=packages.get(aPackage);
			}
			else{
				cs=new HashSet<>();
				packages.put(aPackage,cs);
			}
			cs.add(c);
		}

		Set<Class<? extends Annotation>> anottattions = (Set<Class<? extends Annotation>>) dinoProject.annotadeds
				.keySet();

		for (Class annotation : anottattions) {
			List<Class<?>> clazzes = filterByAnnotation(annotation, allClasses);
			dinoProject.getAnnotadeds().get(annotation).addAll(clazzes);
		}

		for (String s : dinoProject.getInterestClasses()) {
			try {
				Class<?> oneClass = urlClassLoader.loadClass(s);
				List<Class<?>> clazzes = GebUtils.geraLinkedTo(GebUtils.geraLinkedTo(GebUtils.geraLinkedTo(oneClass, allClasses), allClasses), allClasses);
				System.out.println(oneClass.getSimpleName() + " " + clazzes.size() + " " + clazzes);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	private List<Class<?>> filterByAnnotation(Class<? extends Annotation> anotation, List<Class<?>> allClasses) {
		List<Class<?>> toReturn = new ArrayList<>();
		for (Class<?> c : allClasses) {
			if (c.isAnnotationPresent(anotation)) {
				toReturn.add(c);
			}
		}
		return toReturn;
	}



}
