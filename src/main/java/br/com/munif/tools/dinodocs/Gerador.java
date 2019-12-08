package br.com.munif.tools.dinodocs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.web.bind.annotation.RestController;

public class Gerador {

    private static int exec = 0;
    private DinoProject dinoProject;

    public Gerador(DinoProject dinoProject) {
        this.dinoProject = dinoProject;
        Set<String> classFolders = new HashSet<>();
        for (File classFolder : dinoProject.getClassFolders()) {
            final Set<File> possilbeClassFIles = procuraArquivosRecursivamente(classFolder, "class");
            for (File f : possilbeClassFIles) {
                String absolutePath = f.getAbsolutePath();
                String replaced = dinoProject.filtrosParaElementos.replaceAll("\\.", "/");

                int pos = absolutePath.indexOf(replaced);
                if (pos >= 0) {
                    classFolders.add(absolutePath.substring(0, pos));
                }
            }
        }
        try {
            final List<File> elements = new ArrayList<File>();
            for (File jarFolder : dinoProject.getJarFolders()) {
                elements.addAll(procuraArquivosRecursivamente(jarFolder, "jar"));
            }
            for (String ca : classFolders) {
                elements.add(new File(ca));
            }
            URL[] runtimeUrls = new URL[elements.size()];
            for (int i = 0; i < elements.size(); i++) {
                URL url = elements.get(i).toURI().toURL();
                runtimeUrls[i] = url;
            }
            this.dinoProject.classLoader = new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (String classPath : classFolders) {
            Set<File> classFiles = procuraArquivosRecursivamente(new File(classPath), "class");
            for (File classFile : classFiles) {
                try {
                    String nomeClasse = Util.transformaEmNomeDeClasse(classFile.getAbsolutePath(), classPath);
                    if (nomeClasse.startsWith(dinoProject.filtrosParaElementos)) {
                        Class clazz = dinoProject.classLoader.loadClass(nomeClasse);
                        for (Class anotation : dinoProject.annotadeds.keySet()) {
                            if (clazz.isAnnotationPresent(anotation)) {
                                dinoProject.annotadeds.get(anotation).add(clazz);
                            }
                        }
                        Package package1 = clazz.getPackage();
                        if (!dinoProject.getPackages().containsKey(package1)) {
                            dinoProject.getPackages().put(package1, new HashSet<Class>());
                        }
                        Set<Class> classes = dinoProject.getPackages().get(package1);
                        classes.add(clazz);
                    }

                } catch (ClassNotFoundException e) {
                    System.out.println(e.toString().replaceAll("/", "."));

                } catch (NoClassDefFoundError e) {
                    System.out.println(e.toString().replaceAll("/", "."));

                }
            }

        }

        File destination = new File(dinoProject.getDestinationFolder()+"/"+dinoProject.name);
        System.out.println("---->" + destination.getAbsolutePath());
        destination.mkdirs();
        for (Package p : dinoProject.packages.keySet()) {
            String pacote = p.getName();
            Set<String> associcaos = new HashSet<String>();
            dinoProject.links.put(p, associcaos);
            try {
                File pacoteDot = new File(dinoProject.getDestinationFolder(), pacote.replaceAll("\\.", "_") + ".dot");
                FileWriter fw = new FileWriter(pacoteDot, false);
                escreveCabecalho(fw);
                fw.write("subgraph cluster" + pacote.replaceAll("\\.", "_") + "\n{\n");
                fw.write("label=\"" + pacote + "\";\n");
                for (Class entidade : dinoProject.packages.get(p)) {
                    associcaos.addAll(criaClasse(entidade, fw));
                }
                fw.write("}\n\n");
                for (String s : associcaos) {
                    fw.write(s + "\n");
                }
                fw.write("\n}\n\n");
                fw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        

        
        for (Class c : dinoProject.annotadeds.get(RestController.class)) {
            try {
                geraParentes(c);
            } catch (Exception e) {
                System.out.println(c + ":" + e);
            }
        }

        geraArquivoDeLoteShSvg();

    }

    public void geraArquivoDeLoteShSvg() {
        try {
            File script = new File(dinoProject.getDestinationFolder(), "dot2svg" + (exec++ == 0 ? "" : "" + (exec)) + ".sh");
            // System.out.println(script.getAbsolutePath());
            FileWriter fw = new FileWriter(script, false);
            script.setExecutable(true);
            fw.write("#!/bin/sh\n");
            for (Package p : dinoProject.getPackages().keySet()) {
                String pacote = p.getName();
                fw.write("dot -T svg -o " + pacote.replaceAll("\\.", "_") + ".svg " + pacote.replaceAll("\\.", "_")
                        + ".dot\n");
            }
            fw.write("\n");
            for (Class c : dinoProject.annotadeds.get(RestController.class)) {
                String nome = c.getSimpleName() + "Arround.dot";
                fw.write("dot -T svg -o " + nome + ".svg " + nome + "\n");
            }
            fw.write("\n");
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void descobreClasses() {
        String c = dinoProject.getFiltrosParaElementos();

        for (File f : dinoProject.getClassFolders()) {
            String absolutePath = f.getAbsolutePath();
            int pos = absolutePath.indexOf(c);
            if (pos >= 0) {
                dinoProject.getClassFolders().add(new File(absolutePath.substring(0, pos)));
            }

            try {
                List<File> elementos = new ArrayList<File>();
                elementos.addAll(procuraArquivosRecursivamente(new File(c), "jar"));
                elementos.addAll(dinoProject.getClassFolders());
                URL[] runtimeUrls = new URL[elementos.size()];
                for (int i = 0; i < elementos.size(); i++) {
                    URL url = elementos.get(i).toURI().toURL();
                    runtimeUrls[i] = url;
                }
                dinoProject.classLoader = new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private Set<File> procuraArquivosRecursivamente(File pasta, String extencao) {
        Set<File> elementos = new HashSet<File>();
        if (pasta != null && pasta.listFiles() != null) {
            File[] listFiles = pasta.listFiles();
            for (File f : listFiles) {
                if (f.isFile() && f.getName().endsWith(extencao)) {
                    elementos.add(f);
                } else if (f.isDirectory()) {
                    elementos.addAll(procuraArquivosRecursivamente(f, extencao));
                }
            }
        }
        return elementos;
    }

    private void escreveCabecalho(FileWriter fileWriter) throws IOException {
        fileWriter.write("//Gerado automaticamente por plugin de www.munif.com.br munif@munifgebara.com.br\n\n" + ""
                + "digraph G{\n" + "fontname = \"Bitstream Vera Sans\"\n" + "fontsize = 8\n\n" + "node [\n"
                + "        fontname = \"Bitstream Vera Sans\"\n" + "        fontsize = 8\n"
                + "        shape = \"record\"\n" + "]\n\n" + "edge [\n" + "        fontname = \"Bitstream Vera Sans\"\n"
                + "        fontsize = 8\n" + "]\n\n");
    }

    private List<String> criaClasse(Class entidade, FileWriter fw) throws Exception {
        List<String> associacoes = new ArrayList<String>();
        String saida = "";
        try {
            if (entidade.getSuperclass() == null) {
                return associacoes;
            }

            if (entidade.getSimpleName().trim().length() == 0) {
                return associacoes;
            }

            String nomeJava = dinoProject.getFiltrosParaElementos();

            if (entidade.getSuperclass().getCanonicalName().startsWith(nomeJava)) {
                associacoes.add("edge [ arrowhead = \"empty\" headlabel = \"\" taillabel = \"\"] "
                        + entidade.getSimpleName() + " -> " + entidade.getSuperclass().getSimpleName());
            }

            String cor = "";
            saida += (entidade.getSimpleName() + " [" + cor + "label = \"{" + entidade.getSimpleName() + "|");
            Field atributos[] = {};

            atributos = entidade.getDeclaredFields();
            int i = 0;
            Set<String> metodosExcluidosDoDiagrama = new HashSet<String>();
            metodosExcluidosDoDiagrama.add("equals");
            metodosExcluidosDoDiagrama.add("hashCode");
            metodosExcluidosDoDiagrama.add("toString");

            for (Field f : atributos) {
                i++;
                Class tipoAtributo = f.getType();
                String tipo = tipoAtributo.getSimpleName();
                String nomeAtributo = f.getName();
                String naA = nomeAtributo.substring(0, 1).toUpperCase() + nomeAtributo.substring(1);
                metodosExcluidosDoDiagrama.add("set" + naA);
                metodosExcluidosDoDiagrama.add("get" + naA);
                if ((f.getModifiers() & Modifier.STATIC) != 0) {
                    continue;
                }
                if (f.getType().equals(List.class
                ) || f.getType().equals(Set.class
                ) || f.getType().equals(Map.class
                )) {
                    ParameterizedType type = (ParameterizedType) f.getGenericType();
                    Type[] typeArguments = type.getActualTypeArguments();
                    Class tipoGenerico = Object.class;

                    try {
                        tipoGenerico = (Class) typeArguments[f.getType().equals(Map.class) ? 1 : 0];
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    if (f.isAnnotationPresent(ManyToMany.class
                    )) {
                        ManyToMany mm = f.getAnnotation(ManyToMany.class);

                        if (!mm.mappedBy()
                                .isEmpty()) {
                            continue;
                        }
                        // System.out.println(entidade.getName() + " " + f.getName() + " " +
                        // tipoAtributo + " "+ tipoGenerico.getSimpleName() + " ManyToMany");

                        associacoes.add(
                                "edge [arrowhead = \"none\" headlabel = \"*\" taillabel = \"*@\"] "
                                + entidade.getSimpleName() + " -> " + tipoGenerico.getSimpleName() + " [label = \""
                                + nomeAtributo + "\"]");
                    } else if (f.isAnnotationPresent(OneToMany.class
                    )) {
                        OneToMany oo = f.getAnnotation(OneToMany.class);

                        if (!oo.mappedBy()
                                .isEmpty()) {
                            continue;
                        }
                        // System.out.println(entidade.getName() + " " + f.getName() + " " +
                        // tipoAtributo + " " + tipoGenerico.getSimpleName() + " OneToMany");

                        associacoes.add(
                                "edge [arrowhead = \"none\" headlabel = \"*\" taillabel = \"1@\"] "
                                + entidade.getSimpleName() + " -> " + tipoGenerico.getSimpleName() + " [label = \""
                                + nomeAtributo + "\"]");
                    }

                } else if (f.isAnnotationPresent(ManyToOne.class
                )) {
                    ManyToOne mo = f.getAnnotation(ManyToOne.class);

                    associacoes.add(
                            "edge [arrowhead = \"none\" headlabel = \"1\" taillabel = \"*@\"] "
                            + entidade.getSimpleName() + " -> " + tipo + " [label = \"" + nomeAtributo + "\"]");
                } else if (f.isAnnotationPresent(OneToOne.class
                )) {
                    OneToOne oo = f.getAnnotation(OneToOne.class);

                    if (!oo.mappedBy()
                            .isEmpty()) {
                        continue;
                    }

                    associacoes.add(
                            "edge [arrowhead = \"none\" headlabel = \"1\" taillabel = \"1@\"] "
                            + entidade.getSimpleName() + " -> " + tipo + " [label = \"" + nomeAtributo + "\"]");
                } else {
                    nomeJava = dinoProject.getFiltrosParaElementos();
                    String canonicalName = f.getType().getCanonicalName();
                    if (canonicalName == null) {
                        canonicalName = f.getType().toString();
                    }
                    boolean b = false;

                    b = b || (canonicalName.startsWith(nomeJava));
                    if (b) {
                        associacoes.add("edge [arrowhead = \"none\"  ] "
                                + entidade.getSimpleName() + " -> " + tipo + " [label = \"" + nomeAtributo + "\"]");

                    } else {
                        saida += (nomeAtributo + ":" + tipo + "\\l");
                    }
                }
            }

            saida += ("|");
            Method metodos[] = {};
            metodos = entidade.getDeclaredMethods();

            for (Method m : metodos) {
                if (!metodosExcluidosDoDiagrama.contains(m.getName())) {
                    saida += (m.getName() + ":" + m.getReturnType().getSimpleName() + "\\l");
                }
            }
            saida += ("}\"]\n");
            fw.write(saida);
        } catch (NoClassDefFoundError e) {

            System.out.println(entidade + " " + e);
        }

        return associacoes;

    }

    private void geraAnotated(Class anotation) {
        List<String> associcaos = new ArrayList<String>();
        try {
            File pacoteDot = new File(dinoProject.getDestinationFolder(), anotation.getSimpleName() + ".dot");
            FileWriter fw = new FileWriter(pacoteDot, false);
            escreveCabecalho(fw);
            fw.write("subgraph clusterentidades \n{\n");
            fw.write("label=\"entidade  \";\n");
            for (Class entidade : dinoProject.annotadeds.get(anotation)) {
                associcaos.addAll(criaClasse(entidade, fw));
            }
            fw.write("}\n\n");
            for (String s : associcaos) {
                fw.write(s + "\n");
            }
            fw.write("\n}\n\n");
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void geraParentes(Class classe) {
        naoVisitados = new ArrayList<String>();
        parentes = new HashSet<Class>();
        naoVisitados.add(classe.getCanonicalName());
        List<String> associcaos = new ArrayList<String>();
        try {
            File pacoteDot = new File(dinoProject.getDestinationFolder(), classe.getSimpleName() + "Arround.dot");
            FileWriter fw = new FileWriter(pacoteDot, false);
            escreveCabecalho(fw);
            fw.write("subgraph clusterentidades \n{\n");
            fw.write("label=\"" + classe.getSimpleName() + "\";\n");
            for (Class entidade : parentes) {
                associcaos.addAll(criaClasse(entidade, fw));
            }
            fw.write("}\n\n");
            for (String s : associcaos) {
                fw.write(s + "\n");
            }
            fw.write("\n}\n\n");
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    List<String> naoVisitados = new ArrayList<String>();
    Set<Class> parentes = new HashSet<Class>();

    private void rocuraRecursivo() {
        while (naoVisitados.size() > 0) {
            System.out.println("Nao---->" + naoVisitados);
            String proxima = naoVisitados.remove(0);
            try {
                Class clazz = dinoProject.classLoader.loadClass(proxima);
                Class superclass = clazz.getSuperclass();
                addParente(superclass);
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field f : declaredFields) {
                    Class tipoAtributo = f.getType();
                    if (f.getType().equals(List.class
                    ) || f.getType().equals(Set.class
                    ) || f.getType().equals(Map.class
                    )) {
                        ParameterizedType type = (ParameterizedType) f.getGenericType();
                        Type[] typeArguments = type.getActualTypeArguments();

                        try {
                            tipoAtributo = (Class) typeArguments[f.getType().equals(Map.class) ? 1 : 0];
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    addParente(tipoAtributo);
                }
            } catch (Throwable t) {
                System.out.println("----> problemas com " + proxima);
                t.printStackTrace();
            }
        }
    }

    public void addParente(Class tipoAtributo) {
        String nomeJava = dinoProject.getFiltrosParaElementos();

        if (tipoAtributo != null && !parentes.contains(tipoAtributo) && tipoAtributo.getCanonicalName().startsWith(nomeJava)) {
            naoVisitados.add(tipoAtributo.getCanonicalName());
            parentes.add(tipoAtributo);
        }

    }

}
