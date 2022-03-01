package es.us.isa.ideas.service;

import net.sourceforge.plantuml.syntax.SyntaxChecker;
import net.sourceforge.plantuml.syntax.SyntaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ValidationDiagramService {

    private Logger logger = LoggerFactory.getLogger(ValidationDiagramService.class);

    public SyntaxResult validateFromString(String source) {
        Assert.notNull(source, "Source can not be null");
        logger.info("Validating entry source {} ", source);
        SyntaxResult syntaxResult = SyntaxChecker.checkSyntax(source);
        logger.info("Syntax result object created from source {}", source);
        return syntaxResult;
    }

}
