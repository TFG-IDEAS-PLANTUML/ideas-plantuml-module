import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.ant.PlantUmlTask;
import net.sourceforge.plantuml.api.PlantumlUtils;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.graphic.UDrawable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.*;

@SpringBootApplication
@ComponentScan("es.us.isa.ideas")
public class PlantUml4IdeasModuleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PlantUml4IdeasModuleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        OutputStream png = new FileOutputStream("src/main/resources/image.png");

        String source = "@startuml\n";
        source += "Bob -> Alice : hello\n";
        source += "@enduml\n";

        SourceStringReader sourceStringReader = new SourceStringReader(source);

        sourceStringReader.outputImage(png);

    }
}
