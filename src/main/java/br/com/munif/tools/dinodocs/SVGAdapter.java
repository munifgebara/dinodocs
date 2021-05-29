package br.com.munif.tools.dinodocs;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

public class SVGAdapter {

	private Set<Class> generated;

	private DinoProject dinoProject;

	public SVGAdapter(DinoProject dinoProject) throws IOException {
		this.dinoProject = dinoProject;
		URLClassLoader urlClassLoader = GebUtils.createClassLoader(this.dinoProject.getClassFolders(),
				this.dinoProject.getJarFolders());

		List<Class<?>> allClasses = GebUtils.scanClasses(urlClassLoader);

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

	private void writeHeader(Writer fileWriter) throws IOException {
        fileWriter.write("//Gerado automaticamente\n\n"
                + ""
                + "digraph G{\n"
                + "fontname = \"Bitstream Vera Sans\"\n"
                + "fontsize = 8\n\n"
                + "node [\n"
                + "        fontname = \"Bitstream Vera Sans\"\n"
                + "        fontsize = 8\n"
                + "        shape = \"record\"\n"
                + "]\n\n"
                + "edge [\n"
                + "        fontname = \"Bitstream Vera Sans\"\n"
                + "        fontsize = 8\n"
                + "]\n\n");
    }


    private List<String> createEntity(Class be, StringWriter fw) throws Exception {
        List<String> associations = new ArrayList<String>();
        if (generated.contains(be)) {
            return associations;
        }
        generated.add(be);
        Class entity = be.getClass();

        String cor = "";
        fw.write(be.getId() + " [" + cor + "label = \"{" + entity.getSimpleName() + " (" + be.getId() + ") |");
        Field[] attrs = entity.getDeclaredFields();

        for (Field f : attrs) {
            if ((f.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            f.setAccessible(true);
            String attrName = f.getName();
            if (f.getType().equals(List.class) || f.getType().equals(Set.class) || f.getType().equals(Map.class)) {
                if (f.isAnnotationPresent(ManyToMany.class) || f.isAnnotationPresent(OneToMany.class)) {
                    Hibernate.initialize(f.get(be));
                    Collection<BaseEntity> others = ((Collection) f.get(be));
                    if (others != null) {
                        for (BaseEntity other : others) {
                            StringWriter sw = new StringWriter();
                            associations.addAll(createEntity(other, sw));
                            associations.add(sw.toString());
                            associations.add("edge [arrowhead = \"none\" ] " + be.getId() + " -> " + other.getId() + " [label = \"" + attrName + "\"]");
                        }
                    }
                }

            } else if (f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class)) {
                BaseEntity other = ((BaseEntity) f.get(be));
                if (other != null) {
                    StringWriter sw = new StringWriter();
                    associations.addAll(createEntity(other, sw));
                    associations.add(sw.toString());
                    associations.add("edge [arrowhead = \"none\"  ] " + be.getId() + " -> " + other.getId() + " [label = \"" + attrName + "\"]");
                }
            } else {
                String vString = "" + f.get(be);
                vString = vString.substring(0, Math.min(vString.length(), 15));
                fw.write(attrName + ":" + vString + "\\l");
            }
        }
        fw.write("}\"]\n");
        return associations;
    }



	public void escreve() throws IOException{
		StringWriter fw = new StringWriter();
		
		this.writeHeader(fw);

		System.out.println(fw.toString());




	}




}
