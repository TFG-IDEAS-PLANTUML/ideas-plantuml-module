package es.us.isa.ideas.util;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.*;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ExampleUtil {

    public static void generateClassDiagram() throws NoSuchColorException {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        ClassDiagramFactory classDiagramFactory = new ClassDiagramFactory();

        ClassDiagram classDiagram = classDiagramFactory.createEmptyDiagram(new UmlSource(new ArrayList<>(), true),
                SkinParam.create(UmlDiagramType.CLASS));

        // titulo de la clase
        Ident ident = classDiagram.buildLeafIdent("leafIdent");

        IEntity iLeaf = classDiagram.getOrCreateLeaf(ident, CodeImpl.of("code"),
                LeafType.ENTITY, USymbol.CARD);

        Display display = iLeaf.getDisplay().add("display");
        iLeaf.setDisplay(display);

        Bodier bodier = iLeaf.getBodier();
        bodier.addFieldOrMethod("new field");
        bodier.addFieldOrMethod("void method()");


        // titulo de la clase
        Ident ident2 = classDiagram.buildLeafIdent("leafIdent2");

        IEntity iLeaf2 = classDiagram.getOrCreateLeaf(ident2, CodeImpl.of("code2"),
                LeafType.ENTITY, USymbol.CARD);

        Display display2 = iLeaf2.getDisplay().add("display");
        iLeaf2.setDisplay(display2);

        Bodier bodier2 = iLeaf2.getBodier();
        bodier2.addFieldOrMethod("new field");
        bodier2.addFieldOrMethod("void method()");

        //Tag en la esquina derecha -> iLeaf.setGeneric("Generic")

        classDiagram.addLink(new Link(iLeaf2, iLeaf, new LinkType(LinkDecor.NONE, LinkDecor.AGREGATION),
                Display.create("display"), 1, new StyleBuilder(SkinParam.create(UmlDiagramType.CLASS))));

        classDiagram.makeDiagramReady();

        try (OutputStream os = new FileOutputStream("src/main/resources/classDiagram.png")) {
            ImageData imageData = classDiagram.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
            //System.out.println("generateFromApi: " + classDiagram.getDescription().getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void generateSequenceDiagram() {

        String source = "@startuml\n";
        source += "Bob -> Alice : hello\n";
        source += "@enduml\n";

        SequenceDiagramFactory f = new SequenceDiagramFactory();
        SequenceDiagram diagram = f.createEmptyDiagram(new UmlSource(new ArrayList<>(), true),
                SkinParam.create(UmlDiagramType.SEQUENCE));

        Display bobD = Display.getWithNewlines("Bob");
        Participant bobP = diagram.getOrCreateParticipant("Bob", bobD);
        bobP.setColors(Colors.empty().add(ColorType.BACK, HColorUtils.BLUE));

        Display aliceD = Display.getWithNewlines("Alice");
        Participant aliceP = diagram.getOrCreateParticipant("Alice", aliceD);

        Display label = Display.getWithNewlines("hello");
        ArrowConfiguration config = ArrowConfiguration.withDirectionNormal().withColor(HColorUtils.BLUE);

        Message msg = new Message(new StyleBuilder(SkinParam.create(UmlDiagramType.SEQUENCE)),
                bobP, aliceP, label, config, diagram.getNextMessageNumber());

        diagram.addMessage(msg);

        diagram.makeDiagramReady();
        try (OutputStream os = new FileOutputStream("src/main/resources/sequenceDiagram.png")) {
            ImageData imageData = diagram.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
            System.out.println("generateFromApi: " + diagram.getDescription().getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
