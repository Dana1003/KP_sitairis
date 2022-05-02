package com.example.project.controllers;

import com.example.project.entity.Course;
import com.example.project.entity.Quest;
import com.example.project.entity.Student;
import com.example.project.entity.Users;
import com.example.project.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.zip.CheckedOutputStream;

@Controller
public class StudentController {

    @Autowired
    public QuestRepository questRepository;
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public UsersRepository usersRepository;
    @Autowired
    public CourseRepository courseRepository;
    @Autowired
    public TeacherRepository teacherRepository;

    protected static Users staticUser;
    protected static Student staticStudent;

    public static void setStaticUser(Users user){
        staticUser = user;
    }
    public static void setStaticStudent(Student student){
        staticStudent = student;
    }

    public static Users getStaticUser(){return staticUser;}
    public static Student getStaticStudent(){return staticStudent;}

    @GetMapping("/studentMainPage")
    public String studentMP(Model model, @ModelAttribute("user") Users user, @ModelAttribute("student") Student student) {
        if(getStaticUser() == null || user.getId() != null && !getStaticUser().getId().equals(user.getId())){
            setStaticUser(user);
        }
        var studList = studentRepository.findAll();
        for (var item:studList) {
            if(staticUser.getId().equals(item.getUsers().getId())) {
                setStaticStudent(item);
            }
        }
        var coursesList = courseRepository.findAll();
        model.addAttribute("courses", coursesList);
        var teacherList = teacherRepository.findAll();
        model.addAttribute("teachers", teacherList);
        return "studentMainPage";
    }
    @GetMapping("/questionnairePage")
    public String questPageGet(Model model) {
        model.addAttribute("quest", new Quest());
        return "questionnairePage";
    }
    @PostMapping("/questionnairePage")
    public String questPagePost(@ModelAttribute("quest") Quest quest) {
        var studList = studentRepository.findAll();
        for (var item:studList){
            if (item.getUsers().getId().equals(getStaticUser().getId()) && item.getQuest()!=null) {
                var newObj = questRepository.findById(item.getQuest().getId()).get();
                newObj.setStudentLevel(quest.getStudentLevel());
                newObj.setLanguages(quest.getLanguages());
                newObj.setEnglishLevel(quest.getEnglishLevel());
                newObj.setGoals(quest.getGoals());
                newObj.setSex(quest.getSex());
                newObj.setAge(quest.getAge());
                newObj.setPhoneNumber(quest.getPhoneNumber());
                questRepository.save(newObj);
            }
            if (item.getUsers().getId().equals(getStaticUser().getId()) && item.getQuest() == null) {
                questRepository.save(quest);
                item.setQuest(quest);
                studentRepository.save(item);
            }
        }
        return "redirect:/studentMainPage";
    }
    @GetMapping("/student/studentProfile/{id}")
    public String studProfileGet(Model model, @PathVariable("id") int id){
        model.addAttribute("currentStudent", staticStudent);
        return "studentProfile";
    }
    @PatchMapping("/student/studentProfile/{id}")
    public String studProfilePatch(Model model, @ModelAttribute("currentStudent") Student staticStudent) {
        var newObj1 = studentRepository.findById(staticStudent.getId()).get();
        newObj1.setName(staticStudent.getName());
        newObj1.setLastName(staticStudent.getLastName());
        newObj1.setEmail(staticStudent.getEmail());
        studentRepository.save(newObj1);
        var newObj2 = usersRepository.findById(staticStudent.getUsers().getId());
        System.out.println(newObj2);
        var a =1;
        return "studentProfile";
    }

}
