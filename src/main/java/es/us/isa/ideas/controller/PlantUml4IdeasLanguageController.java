package es.us.isa.ideas.controller;

import es.us.isa.ideas.module.common.AppResponse;
import es.us.isa.ideas.module.controller.BaseLanguageController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/ideas-plantuml-language/language")
public class PlantUml4IdeasLanguageController extends BaseLanguageController {

    @Override
    public AppResponse executeOperation(String id, String content, String fileUri, String auxArg0, HttpServletRequest httpServletRequest) {
        return new AppResponse();
    }

    @Override
    public AppResponse checkLanguage(String id, String content, String fileUri, HttpServletRequest httpServletRequest) {

        // Validar sintaxis

        AppResponse appResponse = new AppResponse();

        boolean problems = false;

        appResponse.setFileUri(fileUri);

        if (problems)
            appResponse.setStatus(AppResponse.Status.OK_PROBLEMS);
        else
            appResponse.setStatus(AppResponse.Status.OK);

        return appResponse;
    }

    @Override
    public AppResponse convertFormat(String currentFormat, String desiredFormat, String fileUri, String content, HttpServletRequest httpServletRequest) {
        return new AppResponse();
    }
}
