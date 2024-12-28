package com.example.web_honeypot;

import com.example.web_honeypot.controller.VulnerableController;
import com.example.web_honeypot.service.AttackLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VulnerableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttackLogService attackLogService;

    @Test
    public void testSqlInjection() throws Exception {
        mockMvc.perform(post("/vulnerable/query")
                .param("username", "admin' OR '1'='1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("admin")));
    }

    @Test
    public void testXss() throws Exception {
        mockMvc.perform(post("/vulnerable/comment")
                .param("comment", "<script>alert('xss')</script>"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<script>")));
    }

    @Test
    public void testCommandInjection() throws Exception {
        mockMvc.perform(post("/vulnerable/execute")
                .param("command", "ls;cat /etc/passwd"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPathTraversal() throws Exception {
        mockMvc.perform(get("/vulnerable/download")
                .param("file", "../../../etc/passwd"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCsrf() throws Exception {
        mockMvc.perform(post("/vulnerable/transfer")
                .param("to", "attacker")
                .param("amount", "1000"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSsrf() throws Exception {
        mockMvc.perform(get("/vulnerable/fetch")
                .param("url", "http://localhost:8080/internal/api"))
                .andExpect(status().isOk());
    }

    @Test
    public void testXxe() throws Exception {
        String xxePayload = "<?xml version=\"1.0\"?><!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]><foo>&xxe;</foo>";
        mockMvc.perform(post("/vulnerable/parse-xml")
                .content(xxePayload)
                .contentType("application/xml"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthBypass() throws Exception {
        mockMvc.perform(get("/vulnerable/admin/data")
                .header("Role", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sensitive data accessed"));
    }
} 