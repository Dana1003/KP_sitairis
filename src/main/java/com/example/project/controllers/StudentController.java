package com.example.project.controllers;

import com.example.project.entity.*;
import com.example.project.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
    @Autowired
    public StudentCourseRepository studentCourseRepository;

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
    public String studentMP(Model model, @ModelAttribute("user") Users user) {
        if(getStaticUser() == null || user.getId() != null && !getStaticUser().getId().equals(user.getId())){
            setStaticUser(user);
        }
        var studList = studentRepository.findAll();
        for (var item:studList) {
            if(staticStudent == null || staticUser.getId().equals(item.getUsers().getId())) {
                setStaticStudent(item);
            }
        }
        var coursesList = courseRepository.findAll();
        model.addAttribute("courses", coursesList);
        var teacherList = teacherRepository.findAll();
        model.addAttribute("teachers", teacherList);
        model.addAttribute("staticStudent", getStaticStudent());
        return "studentMainPage";
    }
    @GetMapping("/questionnairePage")
    public String questPageGet(Model model) {
        model.addAttribute("quest", new Quest());
        model.addAttribute("staticStudent", getStaticStudent());
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
        model.addAttribute("staticStudent", getStaticStudent());
        return "studentProfile";
    }
    @PatchMapping("/student/studentProfile/{id}")
    public String studProfilePatch(Model model, @ModelAttribute("statStudent") Student student) {
        var newObj1 = studentRepository.findById(student.getId()).get();
        newObj1.setName(student.getName());
        newObj1.setLastName(student.getLastName());
        newObj1.setEmail(student.getEmail());
        studentRepository.save(newObj1);

        var newObj2 = usersRepository.findById(newObj1.getUsers().getId()).get();
        newObj2.setLogin(student.getUsers().getLogin());
        newObj2.setPassword(student.getUsers().getPassword());
        usersRepository.save(newObj2);

        staticStudent.setName(newObj1.getName());
        staticStudent.setLastName(newObj1.getLastName());
        staticStudent.setEmail(newObj1.getEmail());
        staticStudent.setUsers(newObj2);
        model.addAttribute("staticStudent", getStaticStudent());
        return "studentMainPage";
    }
    @GetMapping("/studentAddCourse")
    public String studAddCourseGet (Model model) {
        var coursesList = courseRepository.findAll();
        model.addAttribute("courses", coursesList);
        model.addAttribute("studCourse", new StudentCourse());
        model.addAttribute("staticStudent", getStaticStudent());
        return "studentAddCourse";
    }
    @PostMapping("/studentAddCourse")
    public String studAddCoursePost (@ModelAttribute("studCourse") StudentCourse studentCourse) {
       studentCourse.setStudent(staticStudent);
       var studCourList = (ArrayList<StudentCourse>)studentCourseRepository.findAll();
       if(studCourList.size() == 0) {
           studentCourseRepository.save(studentCourse);
       }
       else {
           for (var item : studCourList) {
               if (item.getStudent().equals(staticStudent) && item.getCourse().equals(studentCourse.getCourse())) {
                   break;
               } else {
                   studentCourseRepository.save(studentCourse);
               }
           }
       }
        return "redirect:/studentAddCourse";
    }
}
