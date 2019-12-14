package br.com.munif.tools.dinodocs;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

public class SVGAdapter {

	private DinoProject dinoProject;

	public SVGAdapter(DinoProject dinoProject) throws IOException {
		this.dinoProject = dinoProject;
		URLClassLoader urlClassLoader = GebUtils.createClassLoader(this.dinoProject.getClassFolders(),
				this.dinoProject.getJarFolders());

		List<Class<?>> allClasses = GebUtils.scanClasses(urlClassLoader);
		System.out.println("All classes " + allClasses.size());
		Set<Class<? extends Annotation>> anottattions = (Set<Class<? extends Annotation>>) dinoProject.annotadeds
				.keySet();

		for (Class annotation : anottattions) {
			List<Class<?>> clazzes = filterByAnnotation(annotation, allClasses);
			dinoProject.getAnnotadeds().get(annotation).addAll(clazzes);
			System.out.println(annotation.getSimpleName() + " " + clazzes.size() + " " + clazzes);
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
