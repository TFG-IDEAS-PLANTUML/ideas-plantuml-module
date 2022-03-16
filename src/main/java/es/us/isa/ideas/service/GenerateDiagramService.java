package es.us.isa.ideas.service;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class GenerateDiagramService {

    private Logger logger = LoggerFactory.getLogger(GenerateDiagramService.class);

    /**
     * public Diagram createEmptyDiagram(UmlDiagramType umlDiagramType){
     * <p>
     * Diagram result = null;
     * <p>
     * SkinParam skinParam = SkinParam.create(umlDiagramType);
     * <p>
     * UmlSource umlSource = new UmlSource(new ArrayList<>(), true);
     * <p>
     * switch (umlDiagramType){
     * case CLASS:
     * ClassDiagram classDiagram = new ClassDiagramFactory().createEmptyDiagram(umlSource, skinParam);
     * result = classDiagram;
     * break;
     * case SEQUENCE:
     * SequenceDiagram diagram = new SequenceDiagramFactory().createEmptyDiagram(umlSource, skinParam);
     * result = diagram;
     * break;
     * default:
     * break;
     * }
     * <p>
     * return result;
     * }
     **/

    public void generateDiagramFromObject(Diagram diagram, String fileLocation) {

        try (OutputStream os = new FileOutputStream(fileLocation + FileFormat.PNG.toString().toLowerCase())) {
            ImageData imageData = diagram.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
        } catch (IOException e) {
            logger.error("Exception thrown when creating diagram {} " +
                            "in location {} " +
                            "with error {}", diagram.toString(), fileLocation,
                    e.getCause());
        }
    }

    public ByteArrayOutputStream generateDiagramFromString(String diagram) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try  {
            logger.info("Printing file for diagram {} in location {}", diagram);
            SourceStringReader sourceStringReader = new SourceStringReader(diagram);
            sourceStringReader.outputImage(os);

        } catch (IOException e) {
            logger.error("Exception thrown when creating diagram {} " +
                            "with error {}", diagram,
                    e.getCause());
        }

        return os;
    }
}
