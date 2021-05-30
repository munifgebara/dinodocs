package br.com.munif.tools.dinodocs;

import br.com.munif.tools.dinodocs.model.DinoProject;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.util.*;
import java.io.*;

public class DinoDot {

    DinoProject dinoProject;

    Configuration cfg;

    public DinoDot(DinoProject dinoProject) throws IOException {
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

    public void annotated() throws MalformedTemplateNameException, ParseException, IOException, TemplateException {
        Template template = cfg.getTemplate("anotacao.ftl");
        Writer out = new OutputStreamWriter(System.out);
        template.process(this.dinoProject, out);
    }

}
