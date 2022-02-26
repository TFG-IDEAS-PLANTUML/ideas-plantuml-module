package es.us.isa.ideas.service;

import net.sourceforge.plantuml.syntax.SyntaxChecker;
import net.sourceforge.plantuml.syntax.SyntaxResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ValidationDiagramService {

    public SyntaxResult validate(String source) {
        Assert.notNull(source, "Source can not be null");
        return SyntaxChecker.checkSyntax(source);
    }

}
