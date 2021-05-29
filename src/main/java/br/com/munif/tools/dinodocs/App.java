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
        String fileName = "/home/mgebara/particular/last.json";

        DinoProject readJson = new DinoProject();
        readJson.addClassFolder("/home/mgebara/pags/pix-notification-service/build/classes/java/main");
        readJson.addClassFolder("/home/mgebara/pags/pix-notification-service/build/classes/kotlin/main");
        readJson.addJarFolder("/home/mgebara/particular/pix-notification-service-0.0.1/BOOT-INF/lib");
        try {
            SVGAdapter adapter = new SVGAdapter(readJson);
            adapter.escreve();

        } catch (IOException e) {

            e.printStackTrace();
        }
        writeJson(readJson, fileName);

    }

    private static DinoProject readJson(String fileName) {

        try (FileReader reader = new FileReader(fileName)) {
            DinoProject readValue = mapper.readValue(new File(fileName), DinoProject.class);

            return d;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    private static void writeJson(DinoProject dinoProject, String fileName) {

        try (FileWriter file = new FileWriter(fileName)) {
            String s = writer.writeValueAsString(dinoProject);
            file.write(s);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
