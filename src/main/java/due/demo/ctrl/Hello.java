package due.demo.ctrl;

import due.demo.bean.Config;
import due.demo.model.User;
import due.demo.repository.StudentMapper;
import due.demo.repository.UserRepository;
import due.demo.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/hello")
@ResponseBody
public class Hello {
    @Autowired
    //mockmvc standaloneSetup 测试 触发 NullPointer
    private Config config;

    @Autowired
    private StudentMapper studentMapper;

    private final UserRepository userRepository;
    private final HelloService helloService;

    @Autowired
    public Hello(HelloService helloService, UserRepository userRepository) {
        this.helloService = helloService;
        this.userRepository = userRepository;
    }

    @RequestMapping("")
    public HashMap<String, Object> index() {
        HashMap<String, Object> ret = new HashMap<>(3);
        ret.put("code", 2000);
        ret.put("msg", "success");
        ret.put("data", userRepository.findByNameLike("due%"));
        return ret;
    }
    @RequestMapping("/hello")//同 hello
    @Cacheable(value = "hello", key = "'world'")
    public String hello() {
        return helloService.hello() + "port:" + config.port + ";env:" + config.env;
    }
    @GetMapping("get")
    public HashMap<String, String> get(String name, Integer age) {
        User user = userRepository.findByNameAndAge(name, age);
        HashMap<String, String> ret = new HashMap<>(2);
        ret.put("name", user.getName());
        ret.put("age", user.getAge() + "");
        return ret;
    }

    @PostMapping("post")
    public int[] post(int d) {
        return new int[]{d};
    }
    @PutMapping("put")
    //必须使用application/json mediaType
    public User put(@RequestBody User user)
    {
        user.setName(user.getName());
        user.setAge(user.getAge() + 1);
        return user;
    }
    @DeleteMapping("delete")
    public User delete(@RequestBody User user)
    {
        user.setName("deleting");
        user.setAge(0);
        return user;
    }
    @PatchMapping("patch")
    //{"name":"dd","age":11, "son": {"name":"son", "age":5}}}
    public HashMap<String, Object> patch(@RequestBody HashMap<String, Object> params) {
        return params;
    }

    @RequestMapping("userList")
    public List<HashMap<String, Object>> userList() {
        HashMap<String, String> params = new HashMap<>(2);
        params.put("uId", "(121,122,123,124)");
        return studentMapper.getUserList(params);
    }
}
