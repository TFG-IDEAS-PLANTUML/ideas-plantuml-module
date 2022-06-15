
package es.us.isa.ideas.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Based on Latex module
@Controller
@RequestMapping("/ideas-plantuml-language/template")
public class TemplatesController {

    private String extension = ".puml";

    private org.slf4j.Logger logger = LoggerFactory.getLogger(TemplatesController.class);

    private static final String SEARCHING_FOR = "Searching for template documents on {}";

    @GetMapping("/document")
    @ResponseBody
    public List<TemplateDocument> getTemplateDocuments(HttpServletRequest request, HttpServletResponse response) {

        List<TemplateDocument> result = new ArrayList<>();
        ServletContext sc = request.getSession().getServletContext();
        String realPath = sc.getRealPath("/WEB-INF/classes/repo/templates/documents");
        logger.info(SEARCHING_FOR, realPath);
        File repo = new File(realPath);

        if (repo.exists() && repo.isDirectory()) {

            File[] templates = repo.listFiles();

            result = Arrays.stream(templates).filter(file -> file.getName().endsWith(extension))
                    .map(file -> new TemplateDocument(file.getName()))
                    .collect(Collectors.toList());
        }

        return result;
    }

    @RequestMapping("/document/{name:.+}")
    @ResponseBody
    public String getTemplateDocuments(@PathVariable("name") String name, HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        ServletContext sc = request.getSession().getServletContext();
        String realPath = sc.getRealPath("/WEB-INF/classes/repo/templates/documents/" + name);
        try {
            result = FileUtils.readFileToString(new File(realPath));
        } catch (IOException ex) {
            logger.error("There was an exception while retrieving template documents with message {} and exception {}", ex.getMessage(), ex.getCause());
        }
        return result;
    }

    class TemplateDocument {
        String name;

        public TemplateDocument(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @RequestMapping("/project")
    @ResponseBody
    public List<TemplateProject> getTemplateProjects(HttpServletRequest request, HttpServletResponse response) {
        List<TemplateProject> result = new ArrayList<>();
        ServletContext sc = request.getSession().getServletContext();
        String realPath = sc.getRealPath("/WEB-INF/classes/repo/templates/projects");
        logger.info(SEARCHING_FOR, realPath);
        File repo = new File(realPath);

        if (repo.exists() && repo.isDirectory()) {
            File[] templates = repo.listFiles();
            for (File f : templates) {
                if (f.isDirectory()) {
                    result.add(new TemplateProject(f.getName(), ""));
                }
            }
        }
        return result;
    }

    class TemplateProject {
        String name;
        String description;

        public TemplateProject(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
}
