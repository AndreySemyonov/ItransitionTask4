package com.itransitionTasks.itransitionTask_4.controller;

import com.itransitionTasks.itransitionTask_4.entity.ListWrapper;
import com.itransitionTasks.itransitionTask_4.entity.User;
import com.itransitionTasks.itransitionTask_4.repositary.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/")
    public String homePage(){
        return "start";
    }

    @RequestMapping("/main")
    public String mainPage(Model model){

        User user = userRepository.findByUsername
                (SecurityContextHolder.getContext().getAuthentication().getName());

        if(user == null||user.getStatus().equals("banned")){
            SecurityContextHolder.clearContext();
            return "redirect:/";
        }

        List<User> users = userRepository.findAll();
        ListWrapper listWrapper = new ListWrapper(new ArrayList<>());

        model.addAttribute("users", users);
        model.addAttribute("listWrapper", listWrapper);

        return "main";
    }

    @RequestMapping("/loginhandler")
    public String loginHandler(){
        User user = userRepository.findByUsername(SecurityContextHolder
                .getContext().getAuthentication().getName());
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        user.setLastLogin(dateFormat.format(date));
        userRepository.save(user);
        return "redirect:/main";
    }

    @RequestMapping("/registration")
    public String registration(Model model){

        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/saveuser")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        User user1 = userRepository.findByUsername(user.getUsername());
        if(!(user1==null)){
            ObjectError usernameExistsError = new ObjectError("usernameExistsError","This username is not vacant!");
            bindingResult.addError(usernameExistsError);
        }
        if (bindingResult.hasErrors())
            return "/registration";
        else {
            String bcrPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            user.setPassword(bcrPassword);
            user.setRole("USER");
            user.setRegDate(dateFormat.format(date));
            user.setStatus("notbanned");
            userRepository.save(user);
            return "redirect:/";
        }

    }

    @Transactional
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@ModelAttribute("list") ListWrapper list){

            ArrayList<Long> longs = (ArrayList<Long>) list.getList();

            if (!(longs.isEmpty())) {
                for (Long id : longs) {
                    userRepository.deleteUserById(id);
                }
            }
            return "redirect:/main";
    }
    @RequestMapping(value = "/block", method = RequestMethod.POST)
    public String block(@ModelAttribute("list") ListWrapper list){

        ArrayList<Long> longs = (ArrayList<Long>) list.getList();

        if (!(longs.isEmpty())) {
            for (Long id : longs) {
                User user = userRepository.getReferenceById(id);
                user.setStatus("banned");
                userRepository.save(user);
            }
        }
        return "redirect:/main";
    }
    @RequestMapping(value = "/unblock", method = RequestMethod.POST)
    public String unblock(@ModelAttribute("list") ListWrapper list){

        ArrayList<Long> longs = (ArrayList<Long>) list.getList();

        if (!(longs.isEmpty())) {
            for (Long id : longs) {
                User user = userRepository.getReferenceById(id);
                user.setStatus("notbanned");
                userRepository.save(user);
            }
        }
        return "redirect:/main";
    }

}
