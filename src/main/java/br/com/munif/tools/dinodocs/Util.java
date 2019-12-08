package br.com.munif.tools.dinodocs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {

    public final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String primeiraMaiuscula(String s) {
        return s.substring(0, 1).toUpperCase().concat(s.substring(1));
    }

    public static String primeiraMinuscula(String s) {
        return s.substring(0, 1).toLowerCase().concat(s.substring(1));
    }

    static String transformaEmNomeDeClasse(String nomeArquivo, String inicio) {
        String nomeCompleto = Util.windowsSafe(nomeArquivo);
        nomeCompleto = nomeCompleto.replaceFirst(inicio, "");
        nomeCompleto = nomeCompleto.replace(".class", "");
        nomeCompleto = nomeCompleto.replaceAll("/", ".");
        return nomeCompleto;
    }

    public static String transformaEmCaminhoClasse(String nomeJava) {
        String nomeCompleto = Util.windowsSafe(nomeJava);
        nomeCompleto = nomeCompleto.replaceAll("\\.", "/");
        return nomeCompleto;
    }

    public static List<Field> getTodosAtributosMenosIdAutomatico(Class classe) {
        List<Field> todosAtributos = getTodosAtributosNaoEstaticos(classe);
        Field aRemover = null;
        Field aRemoverOi = null;
        Field aRemoverVersion = null;
        Field aRemoverGumgaCustomFields = null;
        for (Field f : todosAtributos) {
            if ("id".equals(f.getName())) {
                aRemover = f;
            }
            if ("oi".equals(f.getName())) {
                aRemoverOi = f;
            }
            if ("version".equals(f.getName())) {
                aRemoverVersion = f;
            }
            if ("gumgaCustomFields".equals(f.getName())) {
                aRemoverGumgaCustomFields = f;
            }
        }
        if (aRemover != null) {
            todosAtributos.remove(aRemover);
        }
        if (aRemoverOi != null) {
            todosAtributos.remove(aRemoverOi);
        }
        if (aRemoverVersion != null) {
            todosAtributos.remove(aRemoverVersion);
        }
        if (aRemoverGumgaCustomFields != null) {
            todosAtributos.remove(aRemoverGumgaCustomFields);
            todosAtributos.add(aRemoverGumgaCustomFields);
            System.out.println(todosAtributos);
        }

        return todosAtributos;
    }

    public static List<Field> getTodosAtributosNaoEstaticos(Class classe) throws SecurityException {
        List<Field> aRetornar = new ArrayList<Field>();
        List<Field> aRemover = new ArrayList<Field>();
        if (!classe.getSuperclass().equals(Object.class)) {
            aRetornar.addAll(getTodosAtributosNaoEstaticos(classe.getSuperclass()));
        }
        aRetornar.addAll(Arrays.asList(classe.getDeclaredFields()));
        for (Field f : aRetornar) {
            if (Modifier.isStatic(f.getModifiers())) {
                aRemover.add(f);
            }
            if (f.getName().equals("version")) {
                aRemover.add(f);
            }
            if (f.getName().equals("oi")) {
                aRemover.add(f);
            }
        }
        aRetornar.removeAll(aRemover);
        return aRetornar;
    }

    public static Field primeiroAtributo(Class classe) {
        return getTodosAtributosMenosIdAutomatico(classe).get(0);
    }

    public static Class getTipoGenerico(Field atributo) {
        Class tipoGenerico;
        if (atributo.getGenericType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) atributo.getGenericType();
            Type[] typeArguments = type.getActualTypeArguments();
            tipoGenerico = (Class) typeArguments[atributo.getType().equals(Map.class) ? 1 : 0];
        } else {
            tipoGenerico = atributo.getType();
        }
        return tipoGenerico;
    }

    public static String windowsSafe(String s) {
        return s.replaceAll("\\\\", "/");
    }

    public static String todosAtributosSeparadosPorVirgula(Class classeEntidade) {
        StringBuilder sb = new StringBuilder();
        for (Field f : getTodosAtributosMenosIdAutomatico(classeEntidade)) {
            sb.append(f.getName() + ",");

        }
        sb.setLength(sb.length() - 1);

        return sb.toString().replace("oi,", "");
    }

    public static String hoje() {
        return sdf.format(new Date());
    }

    public static void escreveCabecario(FileWriter fw) throws IOException {
        fw.write("/*\n"
                + "* Gerado automaticamente por DinoDocs em " + hoje() + "\n"
                + "*/\n"
                + "\n");
    }

    public static String dependenciasSeparadasPorVirgula(Set<Class> dependencias, String sufixo, boolean apostrofe) {
        StringBuilder sb = new StringBuilder();
        for (Class clazz : dependencias) {
            sb.append(", " + (apostrofe ? "'" : "") + clazz.getSimpleName() + sufixo + (apostrofe ? "'" : ""));
        }
        return sb.toString();
    }

}
