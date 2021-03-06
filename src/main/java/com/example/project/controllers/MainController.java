package com.example.project.controllers;

import com.example.project.Mapper;
import com.example.project.Report;
import com.example.project.ReportList;
import com.example.project.entity.Student;
import com.example.project.entity.Users;
import com.example.project.repo.CourseRepository;
import com.example.project.repo.StudentRepository;
import com.example.project.repo.TeacherRepository;
import com.example.project.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.ArrayList;

@Controller
public class MainController {

    @Autowired
    public UsersRepository usersRepository;
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public CourseRepository courseRepository;
    @Autowired
    public TeacherRepository teacherRepository;

    @GetMapping("/mainPage")
    public String mainGet(Model model) throws IOException {
/*        Users user = new Users();
        user.setLogin("admin");
        user.setPassword("admin");
        user.setRole("admin");
        usersRepository.save(user);*/
        var coursesList = courseRepository.findAll();
        model.addAttribute("courses", coursesList);
        File file = new File("reports.json");
        if(file.length() == 0)
            model.addAttribute("list", new ReportList());
        else {
            InputStream inputStream = new FileInputStream("reports.json");
            var objectMapper = Mapper.GetMapper();
            var a = objectMapper.readValue(inputStream, ReportList.class);
            if(a.getList().size() > 3) {
                var cutList = new ArrayList<Report>();
                for (var i = a.getList().size() - 3; i < a.getList().size(); i++) {
                    cutList.add(a.getList().get(i));
                }
                model.addAttribute("list", cutList);
            }
            else {
                model.addAttribute("list", a.getList());
            }
        }
        return "mainPage";
    }

    @GetMapping("/authorizationPage")
    public String autorizGet(Model model) {
        model.addAttribute("user", new Users());
        return "authorizationPage";
    }

    @PostMapping("/authorizationPage")
    public String autorizPost(@ModelAttribute("user") Users user, RedirectAttributes redirectAttributes) {
        var userList = usersRepository.findAll();
        for (var item:userList) {
            if (item.getLogin().equals(user.getLogin()) && item.getPassword().equals(user.getPassword())) {
                if (item.getRole().equals("admin")) {
                    return "redirect:/adminMainPage";
                }
                if (item.getRole().equals("student")) {
                    redirectAttributes.addFlashAttribute("user", item);
                    return "redirect:/studentMainPage";
                }
                if (item.getRole().equals("teacher")) {
                    redirectAttributes.addFlashAttribute("teacher", item);
                    return "redirect:/teacherMainPage";
                }
            }
        }
        return "redirect:/authorizationPage";
    }
    @GetMapping("/registrationPage")
    public String regisGet(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("user", new Users());
        return "registrationPage";
    }
    @PostMapping("/registrationPage")
    public String regisPost(@ModelAttribute("student") Student student, @ModelAttribute("user") Users user, RedirectAttributes redirectAttributes) {
        Boolean bool = false;

        user.setRole("student");

        var userList = usersRepository.findAll();
        for (var item:userList) {
            if (item.getLogin().equals(user.getLogin()) && item.getPassword().equals(user.getPassword())) {
                bool = true;
                break;
            }
        }
        if (bool) {
            return "registrationPage";
        }
        else {
            var studentList = studentRepository.findAll();
            for (var item:studentList) {
                if (item.getEmail().equals(student.getEmail())) {
                    bool = true;
                    break;
                }
            }
            if (bool) {
                return "registrationPage";
            }
            else {
                var addedUser = usersRepository.save(user);
                student.setUsers(addedUser);
                studentRepository.save(student);
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/studentMainPage";
            }
        }
    }
}
