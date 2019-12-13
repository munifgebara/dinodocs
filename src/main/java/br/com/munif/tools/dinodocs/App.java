package br.com.munif.tools.dinodocs;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class App {

    public static ObjectMapper mapper;
    public static ObjectWriter writer;

    public static void main(String[] args) {
        mapper = new ObjectMapper();
        writer = mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter()).writerWithDefaultPrettyPrinter();
        String fileName = "/home/munif/defaultDinDocs.json";
        //writeJson(dinoProject, fileName);
        DinoProject readJson = readJson(fileName);

        Gerador gerador = new Gerador(readJson);
    }

    private static DinoProject readJson(String fileName) {

        try (FileReader reader = new FileReader(fileName)) {
            DinoProject readValue = mapper.readValue(new File(fileName), DinoProject.class);
            return readValue;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    private static void writeJson(DinoProject dinoProject, String fileName) {

        try (FileWriter file = new FileWriter(fileName)) {
            String s = writer.writeValueAsString(dinoProject);
            System.out.println(s);
            file.write(s);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

///home/munif/tomcat-build-libs /mnt/linuxdata/projetos/apache-tomcat-8.5.49-src/output/classes/org/apache/tomcat org.apache  ApacheTomcat
///home/munif/objective/docs/dewa-4.1.6/WEB-INF /home/munif/objective/docs/dewa-4.1.6/WEB-INF br.com Teste


///home/munif/projetos/lib /home/munif/projetos/vicente br.com.munif Vicente
