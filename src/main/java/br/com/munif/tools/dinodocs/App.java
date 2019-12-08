package br.com.munif.tools.dinodocs;

import java.io.File;

public class App {

    public static void main(String[] args) {
        DinoProject dinoProject = new DinoProject();
        dinoProject.addJarFolder(args[0]);//   "/home/munif/projetos/libs");
        dinoProject.addClassFolder(args[1] );// "/mnt/linuxdata/projetos/apache-tomcat-8.5.49-src/output");
        dinoProject.setFiltrosParaElementos(args[2]);
        dinoProject.name =args[3]  ;  //"Apache Tomcat"
        
        System.out.println(dinoProject.toString());
       
        
       Gerador gerador=new Gerador(dinoProject);

    }

}


///home/munif/tomcat-build-libs /mnt/linuxdata/projetos/apache-tomcat-8.5.49-src/output/classes/org/apache/tomcat org.apache  ApacheTomcat

///home/munif/objective/docs/dewa-4.1.6/WEB-INF /home/munif/objective/docs/dewa-4.1.6/WEB-INF br.com Teste


///home/munif/projetos/lib /home/munif/projetos/vicente br.com.munif Vicente
