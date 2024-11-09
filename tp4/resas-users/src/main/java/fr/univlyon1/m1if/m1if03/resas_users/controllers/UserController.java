package fr.univlyon1.m1if.m1if03.resas_users.controllers;

import fr.univlyon1.m1if.m1if03.resas_users.classes.User;
import fr.univlyon1.m1if.m1if03.resas_users.daos.UserDao;
import fr.univlyon1.m1if.m1if03.resas_users.services.ConnectionManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.NameNotFoundException;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private ConnectionManager connectionManager;

    @Autowired
    private UserDao userDao;

    @GetMapping("/hello")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="inconnu") String name, Model model) {
        model.addAttribute("name", name);
        return "hello"; // nom du template Thymeleaf
    }

    @PostMapping("/connect")
    public ModelAndView greeting(@RequestParam(name="login") String login,
                                 @RequestParam(name="name", required=false, defaultValue="inconnu") String name,
                                 HttpServletRequest request,
                                 HttpServletResponse response)  {
        Map<String, Object> model = new ModelMap("name");
        User user = new User(login, name);
        connectionManager.connect(request, response, user);
        model.put("user", user);
        return new ModelAndView("hello", model);
    }

    @GetMapping("/reload")
    public String reload(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        return "hello";
    }

    @GetMapping("/update")
    public String update(@RequestParam String name,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) {
        if(connectionManager.isConnected(request)) {
            connectionManager.updateUser(request, response, name);
            try {
                User user = userDao.findByName(name);
                model.addAttribute("user", user);
            } catch (NameNotFoundException e) {
                //User not found
            }
        }
        return "hello";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        Collection<User> users = userDao.findAll();
        model.addAttribute("users", users);
        model.addAttribute("user", session.getAttribute("user"));
        return "list";
    }

    @GetMapping("/deco")
    public String disconnect(HttpServletRequest request) {
        connectionManager.disconnect(request);
        return "redirect:/";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if(status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);
            model.addAttribute("error", "Error " + statusCode);
            model.addAttribute("message", message != null ? message : "Vous devez vous connecter pour accéder au site.");

            if(statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
                return "error/401";
            }
        }

        return "error/error";
    }
}
