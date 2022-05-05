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
            if(item.getUsers().getId().equals(user.getId())) {
                setStaticTeacher(item);
            }
        }
        /*
        var fullCSList = studentCourseRepository.findAll();
        var studCourList = new ArrayList<StudentCourse>();
        for (var item :fullCSList) {
            if (item.getCourse().getTeacher().getId().equals(getStaticTeacher().getId())){
                studCourList.add(item);
            }
        } // ищу курс и студентов конкретного учиетля , чья запись
        model.addAttribute("studentCourseList", studCourList);
        var fullScoreList = scoreRepository.findAll();
        var scoreList = new ArrayList<Score>();
        for (var item: fullScoreList) {
            for (var itemStudCourse: studCourList) {
                if (item.getStudentCourse().equals(itemStudCourse)) {
                    scoreList.add(item);
                }
            }
        } // а потом через этот лист оценки ищу и тоже в лист добавляю
        model.addAttribute("scoreList", scoreList);
        model.addAttribute("score", new Score());

         */
        var fullScoreList = scoreRepository.findAll();
        var sortedScores = new ArrayList<Score>();
        for(var item : fullScoreList){
            if(item.getStudentCourse().getCourse().getTeacher().getId().equals(getStaticTeacher().getId()))
                sortedScores.add(item);
        }
        var studCourseList = studentCourseRepository.findAll();
        var sortedCourse = new ArrayList<StudentCourse>();
        for (var item: studCourseList) {
            if(item.getCourse().getTeacher().getId().equals(getStaticTeacher().getId())) {
                sortedCourse.add(item);
            }
        }
        model.addAttribute("scores", sortedScores);
        model.addAttribute("newScore", new Score());
        model.addAttribute("studentCourse", sortedCourse);
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
