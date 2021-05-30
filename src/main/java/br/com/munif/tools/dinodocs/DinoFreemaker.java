package br.com.munif.tools.dinodocs;

import br.com.munif.tools.dinodocs.model.ClassForFreemaker;
import br.com.munif.tools.dinodocs.model.DinoProject;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.persistence.Entity;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

public class DinoFreemaker {

    DinoProject dinoProject;

    Configuration cfg;

    public DinoFreemaker(DinoProject dinoProject) throws IOException {
        this.dinoProject = dinoProject;
        cfg = new Configuration(Configuration.VERSION_2_3_29);

        cfg.setClassForTemplateLoading(this.getClass(), "/templates");

        // From here we will set the settings recommended for new projects. These
        // aren't the defaults for backward compatibilty.
        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

        // Wrap unchecked exceptions thrown during template processing into TemplateException-s:
        cfg.setWrapUncheckedExceptions(true);

        // Do not fall back to higher scopes when reading a null loop variable:
        cfg.setFallbackOnNullLoopVariable(false);
    }

    public void createClassDiagram() throws MalformedTemplateNameException, ParseException, IOException, TemplateException {

        for (Class frameworkType : dinoProject.getAnnotadeds().keySet()) {
            Map<Package, Set<ClassForFreemaker>> somePackages = new HashMap<>();
            Set<Class<?>> elementos = dinoProject.getAnnotadeds().get(frameworkType);
            for (Class elemento : elementos) {
                if (!somePackages.keySet().contains(elemento.getPackage())) {
                    somePackages.put(elemento.getPackage(), new HashSet<>());
                }
                somePackages.get(elemento.getPackage()).add(new ClassForFreemaker(elemento));
            }
            Map<String, Object> dinoProjectForFreemaker = new HashMap<>();
            dinoProjectForFreemaker.put("originalProject", this.dinoProject);
            dinoProjectForFreemaker.put("entityPackages", somePackages);

            StringWriter sw = new StringWriter();
            Template template = cfg.getTemplate("classDiagram.ftl");
            template.process(dinoProjectForFreemaker, sw);

            Files.write(Paths.get("/home/mgebara/particular/saida/"+frameworkType.getSimpleName()+".plt"), sw.toString().getBytes());

            SourceStringReader reader = new SourceStringReader(sw.toString());
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            DiagramDescription dd = reader.outputImage(os, new FileFormatOption(FileFormat.SVG));
            System.out.println(dd.getDescription());
            // The XML is stored into svg
            final String out = new String(os.toByteArray(), Charset.forName("UTF-8"));
            os.close();
            Files.write(Paths.get("/home/mgebara/particular/saida/"+frameworkType.getSimpleName()+".svg"), out.getBytes());
        }

    }

}
