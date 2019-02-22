package due.demo.ctrl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class Login {
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("hello", "hello");
        return "/login/login";
    }
    @ResponseBody
    @PostMapping("/login")
    public String loginDo(String name, String pwd) {
        return name + pwd;
    }

    @GetMapping("/uid")
    @ResponseBody
    public String uid(HttpSession session) {
        UUID uuid = (UUID) session.getAttribute("uid");
        if (uuid != null) {
            uuid = UUID.randomUUID();
        }
        session.setAttribute("uid", uuid);
        return session.getId();
    }
}
