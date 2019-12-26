package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sec.project.config.CustomUserDetailsService;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

@Controller
public class RegisterController {

    @Autowired
    private AccountRepository ar;
    @Autowired
    private CustomUserDetailsService cuds;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String password) {
        if (name.length() < 1 || password.length() < 1)
            return "redirect:/register";
        Account a = ar.findByUsername(name);
        if (a != null) {
            return "redirect:/register";
        }
        a = new Account();
        a.setUsername(name);
        a.setPassword(password);
        cuds.save(a);
        return "redirect:/login";
    }

}
