package com.example.project.controllers;

import com.example.project.entity.*;
import com.example.project.repo.ScoreRepository;
import com.example.project.repo.StudentCourseRepository;
import com.example.project.repo.TeacherRepository;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class TeacherController {
    @Autowired
    public StudentCourseRepository studentCourseRepository;
    @Autowired
    public TeacherRepository teacherRepository;
    @Autowired
    public ScoreRepository scoreRepository;

    protected static Teacher staticTeacher;
    public static void setStaticTeacher(Teacher teacher){
        staticTeacher = teacher;
    }
    public static Teacher getStaticTeacher(){return staticTeacher;}

    @GetMapping("/teacherMainPage")
    public String teacherMPGet(Model model, @ModelAttribute("teacher") Users user) {
        var teachList = teacherRepository.findAll();
        for (var item:teachList) {
            if(staticTeacher == null || item.getUsers().getId().equals(user.getId())) {
                setStaticTeacher(item);
            }
        }
        var fullList = studentCourseRepository.findAll();
        var studCourList = new ArrayList<StudentCourse>();
        for (var item :fullList) {
            if (item.getCourse().getTeacher().getId().equals(getStaticTeacher().getId())){
                studCourList.add(item);
            }
        }
        model.addAttribute("studentCourseList", studCourList);
        model.addAttribute("score", new Score());
        return "teacherMainPage";
    }
    @PostMapping("/teacherMainPage")
    public String teacherMPPost (@ModelAttribute("score") Score score){
        var scoreList = (ArrayList<Score>)scoreRepository.findAll();
        if (scoreList.size() == 0) {
            scoreRepository.save(score);
        }
        else {
            var count = 0;
            for (var item : scoreList) {
                if (item.getStudentCourse().equals(score.getStudentCourse()) && item.getTask().equals(score.getTask())) {
                    count++;
                }
            }
            if (count == 0) {
                scoreRepository.save(score);
            }
        }
        return "redirect:/teacherMainPage";
    }
}
