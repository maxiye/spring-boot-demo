package due.demo;

import due.demo.repository.UserRepository;
import due.demo.services.HelloService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DemoApplicationTests {
    @Autowired
    private WebApplicationContext web;
    private MockMvc mvc;

    @Before
    public void before() {
        //mvc = MockMvcBuilders.standaloneSetup(new Hello(helloService, userRepository)).build();//非构造注入的bean (/hello/hello) 报错：nullpointer
        mvc = MockMvcBuilders.webAppContextSetup(web).build();
    }
    @Test
    @Rollback
    public void contextLoads() throws Exception {
        String res = mvc.perform(MockMvcRequestBuilders.put("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(res);

        mvc.perform(MockMvcRequestBuilders.get("/hello/hello").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("helloport:8081;env:dev")));

        mvc.perform(MockMvcRequestBuilders.put("/hello/put").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"due\", \"age\": 10}"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":0,\"name\":\"due\",\"age\":11}")));

        mvc.perform(MockMvcRequestBuilders.post("/hello/post").accept(MediaType.APPLICATION_JSON).contentType(MediaType.MULTIPART_FORM_DATA).param("d", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[10]")));

        mvc.perform(MockMvcRequestBuilders.delete("/hello/delete").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"due\", \"age\": 10}"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":0,\"name\":\"deleting\",\"age\":0}")));

        mvc.perform(MockMvcRequestBuilders.patch("/hello/patch").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"due\", \"age\": 10}"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"name\":\"due\",\"age\":10}")));
    }
}
