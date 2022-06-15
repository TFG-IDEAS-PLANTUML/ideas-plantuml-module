package es.us.isa.ideas.service;

import net.sourceforge.plantuml.SourceStringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GenerateDiagramService {

    private Logger logger = LoggerFactory.getLogger(GenerateDiagramService.class);

    public ByteArrayOutputStream generateDiagramFromString(String diagram) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
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
