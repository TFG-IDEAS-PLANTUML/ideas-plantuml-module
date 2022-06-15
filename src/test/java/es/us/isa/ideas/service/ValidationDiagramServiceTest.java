package es.us.isa.ideas.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ValidationDiagramService.class})
@ActiveProfiles("junit")
public class ValidationDiagramServiceTest {

    @Autowired
    private ValidationDiagramService validationDiagramService;

    @Test
    public void validateFromStringSuccess() {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        Assert.assertFalse("Result returned not expected", this.validationDiagramService.validateFromString(source).isError());

    }


    @Test
    public void validateFromStringFail() {

        String source = "@startuml\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        Assert.assertTrue("Result returned not expected", this.validationDiagramService.validateFromString(source).isError());

    }


}