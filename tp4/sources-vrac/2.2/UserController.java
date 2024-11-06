package fr.univlyon1.m1if.m1if03.resas_users.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/hello")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="inconnu") String name, Model model) {
        model.addAttribute("name", name);
        return "hello"; // nom du template Thymeleaf
    }

    @PostMapping("/connect")
    public ModelAndView greeting(@RequestParam(name="name", required=false, defaultValue="inconnu") String name)  {
        Map<String, Object> model = new ModelMap("name");
        model.put("name", name);
        return new ModelAndView("hello", model);
    }

    @GetMapping("/reload")
    public String reload(HttpSession session, Model model) {
        model.addAttribute("name", session.getAttribute("name"));
        return "hello";
    }

    @RequestMapping("/update")
    @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED, reason = "Not implemented yet, coming soon...")
    public void update() {
    }
}
