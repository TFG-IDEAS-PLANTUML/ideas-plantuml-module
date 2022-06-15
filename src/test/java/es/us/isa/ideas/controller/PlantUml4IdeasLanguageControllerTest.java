package es.us.isa.ideas.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PlantUml4IdeasLanguageController.class)
@ActiveProfiles("junit")
public class PlantUml4IdeasLanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_PATH = "/ideas-plantuml-language/language";

    @Test
    public void convertFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/convert")
                        .param("currentFormat", "no available")
                        .param("desiredFormat", "no available")
                        .param("fileUri", "no available")
                        .param("content", "no available"))
                .andExpect(status().isOk()).andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void checkLanguageSuccess() throws Exception {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/format/puml/checkLanguage")
                        .param("id", "no available")
                        .param("content", source)
                        .param("fileUri", "no available"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    public void checkLanguageFail() throws Exception {
        String source = "@startuml\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/format/puml/checkLanguage")
                        .param("id", "no available")
                        .param("content", source)
                        .param("fileUri", "no available"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK_PROBLEMS"));
    }

    @Test
    public void executeOperation_generate_file() throws Exception {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/operation/generate_file/execute")
                        .param("id", "generate_file")
                        .param("content", source)
                        .param("fileUri", "no available"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.htmlMessage").isNotEmpty());
    }


    @Test
    public void executeOperation_apply_theme() throws Exception {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/operation/apply_theme/execute")
                        .param("id", "apply_theme")
                        .param("content", source)
                        .param("fileUri", "no available"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.htmlMessage").isEmpty());

    }

    @Test
    public void executeOperation_generate_console() throws Exception {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/operation/generate_console/execute")
                        .param("id", "generate_console")
                        .param("content", source)
                        .param("fileUri", "no available"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.htmlMessage").isNotEmpty());
    }

    @Test
    public void executeOperation_none() throws Exception {

        String source = "@startuml\n";
        source += "class Person {\n";
        source += "String name\n";
        source += "int age\n";
        source += "int money\n";
        source += "String getName()\n";
        source += "void setName(String name)\n";
        source += "}\n";
        source += "@enduml\n";

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/operation/no available/execute")
                        .param("id", "no available")
                        .param("content", source)
                        .param("fileUri", "no available"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").doesNotExist());

    }

    @Test
    public void getJSFile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/operation/util/javascript")
                        .param("id", "util"))
                .andExpect(content().contentType("application/javascript"))
                .andExpect(status().isOk());
    }

}