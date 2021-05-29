package br.com.munif.tools.dinodocs;

public class DinoDot {
    /*

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


     */


}
