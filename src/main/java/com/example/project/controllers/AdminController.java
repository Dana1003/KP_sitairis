package com.example.project.controllers;

import com.example.project.entity.Course;
import com.example.project.entity.Teacher;
import com.example.project.entity.Users;
import com.example.project.repo.CourseRepository;
import com.example.project.repo.QuestRepository;
import com.example.project.repo.TeacherRepository;
import com.example.project.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    @Autowired
    public TeacherRepository teacherRepository;
    @Autowired
    public UsersRepository usersRepository;
    @Autowired
    public CourseRepository courseRepository;
    @Autowired
    public QuestRepository questRepository;

    @GetMapping("/adminMainPage")
    public String adminMP(Model model) {
        var courseList = courseRepository.findAll();
        model.addAttribute("courses", courseList);
        var teacherList = teacherRepository.findAll();
        model.addAttribute("teachers", teacherList);
        var questionnaireList = questRepository.findAll();
        model.addAttribute("questionnaires", questionnaireList);
        return "adminMainPage";
    }
    @GetMapping("/adminAddTeacher")
    public String adminATGet(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("user", new Users());
        return "adminAddTeacher";
    }
    @PostMapping("/adminAddTeacher")
    public String adminATPost(@ModelAttribute("teacher") Teacher teacher, @ModelAttribute("user") Users user) {
        Boolean bool = false;

        user.setRole("teacher");

        var userList = usersRepository.findAll();
        for (var item:userList) {
            if (item.getLogin().equals(user.getLogin()) && item.getPassword().equals(user.getPassword())) {
                bool = true;
                break;
            }
        }
        if (bool) {
            return "adminAddTeacher";
        }
        else {
            var teacherList = teacherRepository.findAll();
            for (var item:teacherList) {
                if (item.getName().equals(teacher.getName()) && item.getLastName().equals(teacher.getLastName())) {
                    bool = true;
                    break;
                }
            }
            if (bool) {
                return "adminAddTeacher";
            }
            else {
                var addedUser = usersRepository.save(user);
                teacher.setUsers(addedUser);
                teacherRepository.save(teacher);
                return "redirect:/adminMainPage";
            }
        }
    }
    @GetMapping("/adminAddCourse")
    public String adminACGet(Model model) {
        model.addAttribute("course", new Course());
        var teachersList = teacherRepository.findAll();
        model.addAttribute("teachers", teachersList);
        return "adminAddCourse";
    }
    @PostMapping("/adminAddCourse")
    public String adminACPost(@ModelAttribute("course") Course course){
        Boolean bool = false;

        var courseList = courseRepository.findAll();
        for (var item:courseList) {
            if(item.getCourseName().equals(course.getCourseName())) {
                bool = true;
                break;
            }
        }
        if (bool) {
            return "adminAddCourse";
        }
        else {
            courseRepository.save(course);
        }
        return "redirect:/adminMainPage";
    }
    @GetMapping("/adminDeleteCourse")
    public String adminDCGet(Model model){
        var coursesList = courseRepository.findAll();
        model.addAttribute("courses", coursesList);
        return "adminDeleteCourse";
    }
    @GetMapping("/adminEditCourse")
    public String adminECGet(Model model){
        var coursesList = courseRepository.findAll();
        model.addAttribute("courses", coursesList);
        return "adminEditCourse";
    }

    @GetMapping(path = "/admin/deleteCourse/{id}")
    public String deleteCourse(Model model, @PathVariable("id") int id){
        model.addAttribute("course", courseRepository.findById(id).get());
        return "confirmDeleteCourse";
    }
    @GetMapping("/admin/editCourse/{id}")
    public String editCourse(Model model, @PathVariable("id") int id) {
        var teachersList = teacherRepository.findAll();
        model.addAttribute("teachers", teachersList);
        model.addAttribute("course", courseRepository.findById(id).get());
        return "editingCourse";
    }
    @PatchMapping("/admin/editCourse/{id}")
    public String editCourseById(Model model, @ModelAttribute("course") Course course) {
        var newObj = courseRepository.findById(course.getId()).get();
        newObj.setTeacher(course.getTeacher());
        newObj.setCourseName(course.getCourseName());
        newObj.setDuration(course.getDuration());
        newObj.setLanguage(course.getLanguage());
        courseRepository.save(newObj);
        return "redirect:/adminEditCourse";
    }

    @DeleteMapping(path = "/admin/deleteCourse/{id}")
    public String deleteCourseById(Model model, @ModelAttribute("course") Course course){
        courseRepository.delete(course);
        return "redirect:/adminDeleteCourse";
    }

    @GetMapping("/adminDeleteTeacher")
    public String adminDTGet(Model model){
        var teachersList = teacherRepository.findAll();
        model.addAttribute("teachers", teachersList);
        return "adminDeleteTeacher";
    }
    @GetMapping(path = "/admin/deleteTeacher/{id}")
    public String deleteTeacher(Model model, @PathVariable("id") int id){
        model.addAttribute("teacher", teacherRepository.findById(id).get());
        return "confirmDeleteTeacher";
    }
    @DeleteMapping(path = "/admin/deleteTeacher/{id}")
    public String deleteTeacherById(Model model, @ModelAttribute("teacher") Teacher teacher){
        teacherRepository.delete(teacher);
        return "redirect:/adminDeleteTeacher";
    }

    @GetMapping("/adminSortedCourses")
    public String sortdedCoursesGet(Model model) {
        var coursesList = (ArrayList<Course>) courseRepository.findAll();
        var sortedCourses = coursesList.stream().sorted(Comparator.comparing(Course::getDuration)).collect(Collectors.toList());
        model.addAttribute("sortedList", sortedCourses);
        return "adminSortedCourses";
    }
}
