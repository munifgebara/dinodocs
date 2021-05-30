package br.com.munif.tools.dinodocs;

import br.com.munif.tools.dinodocs.model.DinoProject;
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

        DinoProject dinoProject = new DinoProject();
        dinoProject.addClassFolder("/home/mgebara/pags/pix-notification-service/build/classes/java/main");
        dinoProject.addClassFolder("/home/mgebara/pags/pix-notification-service/build/classes/kotlin/main");
        dinoProject.addJarFolder("/home/mgebara/particular/pix-notification-service-0.0.1/BOOT-INF/lib");
        try {
            DinoAnalyzer dinoAnalyzer = new DinoAnalyzer(dinoProject);
            writeJson(dinoProject, fileName);
            DinoDot dinoDot = new DinoDot(dinoProject);
            dinoDot.annotated();

        } catch (Exception e) {

            e.printStackTrace();
        }

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
            file.write(s);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
