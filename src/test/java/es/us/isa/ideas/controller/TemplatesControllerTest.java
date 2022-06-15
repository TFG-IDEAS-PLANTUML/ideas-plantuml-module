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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TemplatesController.class)
@ActiveProfiles("junit")
public class TemplatesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_PATH = "/ideas-plantuml-language/template";

    @Test
    public void getTemplateDocuments() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/document"))
                .andExpect(status().isOk()).andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    public void testGetTemplateDocuments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/document/object.puml"))
                .andExpect(status().isOk()).andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
    
}