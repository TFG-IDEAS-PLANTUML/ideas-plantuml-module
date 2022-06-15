package es.us.isa.ideas.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GenerateDiagramService.class})
@ActiveProfiles("test")
public class GenerateDiagramServiceTest {

    @Autowired
    private GenerateDiagramService generateDiagramService;

    @Test
    public void generateDiagramFromStringSuccess() {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        Assert.assertTrue("Result returned not expected", this.generateDiagramService.generateDiagramFromString(source).size() > 0);

    }


    @Test
    public void generateDiagramFromStringFail() {

        String source = "@startuml\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        Assert.assertNotNull("Result returned not expected", this.generateDiagramService.generateDiagramFromString(source));

    }


}