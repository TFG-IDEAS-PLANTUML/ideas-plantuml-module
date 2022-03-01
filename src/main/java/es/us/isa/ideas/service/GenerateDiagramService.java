package es.us.isa.ideas.service;

import net.sourceforge.plantuml.*;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

@Service
public class GenerateDiagramService {

    private Logger logger = LoggerFactory.getLogger(GenerateDiagramService.class);

    public Diagram createEmptyDiagram(UmlDiagramType umlDiagramType){

        Diagram result = null;

        SkinParam skinParam = SkinParam.create(umlDiagramType);

        UmlSource umlSource = new UmlSource(new ArrayList<>(), true);

        switch (umlDiagramType){
            case CLASS:
                ClassDiagram classDiagram = new ClassDiagramFactory().createEmptyDiagram(umlSource, skinParam);
                result = classDiagram;
                break;
            case SEQUENCE:
                SequenceDiagram diagram = new SequenceDiagramFactory().createEmptyDiagram(umlSource, skinParam);
                result = diagram;
                break;
            default:
                break;
        }

        return result;
    }

    public void generateDiagramFromObject(Diagram diagram, String fileLocation){

        try (OutputStream os = new FileOutputStream(fileLocation + FileFormat.PNG.toString().toLowerCase())) {
            ImageData imageData = diagram.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
        } catch (IOException e) {
            logger.error("Exception thrown when creating diagram {} " +
                            "in location {} " +
                            "with error {}", diagram.toString(), fileLocation,
                    e.getCause());
        }
    }

    public void generateDiagramFromString(String diagram, String fileLocation) {

        try (OutputStream os = new FileOutputStream(fileLocation + FileFormat.PNG.toString().toLowerCase())){
            logger.info("Printing file for diagram {} in location {}", diagram, fileLocation);
            SourceStringReader sourceStringReader = new SourceStringReader(diagram);
            sourceStringReader.outputImage(os);
        } catch (IOException e) {
            logger.error("Exception thrown when creating diagram {} " +
                    "in location {} " +
                    "with error {}", diagram, fileLocation,
                    e.getCause());
        }

    }
}
