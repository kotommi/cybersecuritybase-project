package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sec.project.domain.Account;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private AccountRepository ar;

    @RequestMapping("*")
    public String defaultMapping() {
        return "index";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        if (name.length() < 1 || address.length() < 1)
            return "redirect:/form";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = (User) auth.getPrincipal();
        Account a = ar.findByUsername(u.getUsername());
        Signup s = new Signup(name, address);
        s.setAccount(a);
        signupRepository.save(s);
        System.out.println(u);
        return "done";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listView(Model model) {
        model.addAttribute("signups", signupRepository.findAll());
        return "list";
    }

    @RequestMapping(value = "/signups/{id}", method = RequestMethod.POST)
    public String deleteSignup(@PathVariable Long id, @ModelAttribute Account account) {
        signupRepository.delete(id);
        return "list";
        // return "redirect:/list";
    }
}
