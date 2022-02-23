package es.us.isa.ideas.service;

import net.sourceforge.plantuml.SourceStringReader;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class GenerateDiagramService {

    public void generateDiagram(String diagram, String fileLocation) throws IOException {

        OutputStream png = new FileOutputStream(fileLocation + ".png");

        SourceStringReader sourceStringReader = new SourceStringReader(diagram);

        sourceStringReader.outputImage(png);
    }
}
