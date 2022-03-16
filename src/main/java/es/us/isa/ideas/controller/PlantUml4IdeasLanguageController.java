package es.us.isa.ideas.controller;

import es.us.isa.ideas.module.common.AppAnnotations;
import es.us.isa.ideas.module.common.AppResponse;
import es.us.isa.ideas.module.controller.BaseLanguageController;
import es.us.isa.ideas.service.GenerateDiagramService;
import es.us.isa.ideas.service.ValidationDiagramService;
import net.sourceforge.plantuml.syntax.SyntaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ideas-plantuml-language/language")
public class PlantUml4IdeasLanguageController extends BaseLanguageController {

    @Autowired
    private ValidationDiagramService validationDiagramService;

    @Autowired
    private GenerateDiagramService generateDiagramService;

    @PostMapping(value = "/operation/{id}/execute")
    @ResponseBody
    @Override
    public AppResponse executeOperation(String id, String content, String fileUri, String username, HttpServletRequest request) {

        AppResponse appResponse = new AppResponse();

        content = cleanContent(content);

        if (id.equals("generate_diagram")) {
            ByteArrayOutputStream result = this.generateDiagramService.generateDiagramFromString(content);
            appResponse.setStatus(AppResponse.Status.OK);
            String base64 = Base64.getEncoder().encodeToString(result.toByteArray());
            appResponse.setData(base64);
            appResponse.setHtmlMessage("<p> Diagram generated </p>" +
                    "<img style='display:block; width:100px;height:100px;' src ='data:image/png;base64,"+ base64 +"' />");
        }

        return appResponse;
    }

    @PostMapping(value = "/format/{format}/checkLanguage")
    @ResponseBody
    @Override
    public AppResponse checkLanguage(String id, String content, String fileUri, HttpServletRequest httpServletRequest) {

        AppResponse appResponse = new AppResponse();

        content = cleanContent(content);

        SyntaxResult syntaxResult = validationDiagramService.validateFromString(content);

        boolean problems = !content.isEmpty() ? syntaxResult.isError() : true;

        appResponse.setFileUri(fileUri);

        if (problems) {

            appResponse.setStatus(AppResponse.Status.OK_PROBLEMS);

            List<String> errors = new ArrayList<>(syntaxResult.getErrors());

            AppAnnotations[] appAnnotations = new AppAnnotations[errors.size()];

            IntStream.range(0, errors.size()).forEach(x -> {
                AppAnnotations appAnnotation = new AppAnnotations();
                appAnnotation.setText(WordUtils.wrap(errors.get(x), 10) + "\n" + syntaxResult.getLineLocation().toString());
                appAnnotation.setType("error");
                appAnnotations[x] = appAnnotation;
            });

            appResponse.setAnnotations(appAnnotations);

        } else
            appResponse.setStatus(AppResponse.Status.OK);

        return appResponse;
    }

    @PostMapping(value = "/convert")
    @ResponseBody
    @Override
    public AppResponse convertFormat(String currentFormat, String desiredFormat, String fileUri, String content, HttpServletRequest httpServletRequest) {
        return new AppResponse();
    }

    private String cleanContent(String content) {
        return content.replace("\r", "");
    }
}
