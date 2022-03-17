package es.us.isa.ideas.controller;

import es.us.isa.ideas.module.common.AppAnnotations;
import es.us.isa.ideas.module.common.AppResponse;
import es.us.isa.ideas.module.controller.BaseLanguageController;
import es.us.isa.ideas.service.GenerateDiagramService;
import es.us.isa.ideas.service.ValidationDiagramService;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.syntax.SyntaxResult;
import net.sourceforge.plantuml.theme.ThemeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ideas-plantuml-language/language")
public class PlantUml4IdeasLanguageController extends BaseLanguageController {

    private Logger logger = LoggerFactory.getLogger(PlantUml4IdeasLanguageController.class);

    @Autowired
    private ValidationDiagramService validationDiagramService;

    @Autowired
    private GenerateDiagramService generateDiagramService;

    @PostMapping(value = "/operation/{id}/execute")
    @ResponseBody
    @Override
    public AppResponse executeOperation(String id, String content, String fileUri, String username, HttpServletRequest request) {

        AppResponse appResponse = new AppResponse();

        content = content.replace("\r", "");

        if (id.equals("generate_file")) {
            ByteArrayOutputStream result = this.generateDiagramService.generateDiagramFromString(content);
            appResponse.setStatus(AppResponse.Status.OK);
            String base64 = Base64.getEncoder().encodeToString(result.toByteArray());
            appResponse.setData(base64);
            appResponse.setHtmlMessage("<p> File generated !</p>");
        } else if (id.equals("apply_theme")){
            appResponse.setStatus(AppResponse.Status.OK);
        } else if (id.equals("generate_console")){
            ByteArrayOutputStream result = this.generateDiagramService.generateDiagramFromString(content);
            appResponse.setStatus(AppResponse.Status.OK);
            String base64 = Base64.getEncoder().encodeToString(result.toByteArray());
            appResponse.setData(base64);
            appResponse.setHtmlMessage("<div id='base64PumlDiagram'><p> Diagram generated </p>" +
                    "<img style='display:block; width:100%;height:100%;' src ='data:image/png;base64," + base64 + "' /></div>");
        }

        return appResponse;
    }

    @PostMapping(value = "/format/{format}/checkLanguage")
    @ResponseBody
    @Override
    public AppResponse checkLanguage(String id, String content, String fileUri, HttpServletRequest httpServletRequest) {

        AppResponse appResponse = new AppResponse();

        content = content.replace("\r", "");

        SyntaxResult syntaxResult = validationDiagramService.validateFromString(content);

        boolean problems = !content.isEmpty() ? syntaxResult.isError() : true;

        appResponse.setFileUri(fileUri);

        if (problems) {

            appResponse.setStatus(AppResponse.Status.OK_PROBLEMS);

            List<String> errors = new ArrayList<>(syntaxResult.getErrors());

            AppAnnotations[] appAnnotations = new AppAnnotations[errors.size()];

            LineLocation lineLocation = syntaxResult.getLineLocation();

            IntStream.range(0, errors.size()).forEach(x -> {

                AppAnnotations appAnnotation = new AppAnnotations();
                appAnnotation.setText(WordUtils.wrap(errors.get(x), 10));
                appAnnotation.setRow(String.valueOf(lineLocation.getPosition()));
                appAnnotation.setType("error");
                appAnnotations[x] = appAnnotation;
            });

            appResponse.setAnnotations(appAnnotations);

        } else
            appResponse.setStatus(AppResponse.Status.OK);

        return appResponse;
    }

    @GetMapping(value = "/operation/{id}/javascript")
    @ResponseBody
    public AppResponse getJavascriptFile(@PathVariable(value = "id") String id, HttpServletResponse response) {

        AppResponse appResponse = new AppResponse();


        try {

            InputStream jsFile = new ClassPathResource("operations/" + id + ".js").getInputStream();
            OutputStream os = response.getOutputStream();
            os.write(IOUtils.toByteArray(jsFile));
            response.setContentType("application/javascript");
            os.close();

        } catch (IOException e) {
            logger.error("An IO exception happened {}", e);
        }

        return appResponse;
    }

    @PostMapping(value = "/convert")
    @ResponseBody
    @Override
    public AppResponse convertFormat(String currentFormat, String desiredFormat, String fileUri, String content, HttpServletRequest httpServletRequest) {
        return new AppResponse();
    }
}
