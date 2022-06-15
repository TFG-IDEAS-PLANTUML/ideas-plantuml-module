package es.us.isa.ideas.service;

import net.sourceforge.plantuml.syntax.SyntaxChecker;
import net.sourceforge.plantuml.syntax.SyntaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ValidationDiagramService {

    public SyntaxResult validateFromString(String source) {
        Assert.notNull(source, "Source can not be null");
        SyntaxResult syntaxResult = SyntaxChecker.checkSyntax(source);
        return syntaxResult;
    }

}
