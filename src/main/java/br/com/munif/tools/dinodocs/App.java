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

     //   Gerador gerador = new Gerador(readJson);
        try {
			SVGAdapter adapter=new SVGAdapter(readJson);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //String s=svg.getEntitesSVG();
        //System.out.prinln (s);
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

