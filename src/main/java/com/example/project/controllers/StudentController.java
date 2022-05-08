package com.example.project.controllers;

import com.example.project.Mapper;
import com.example.project.Report;
import com.example.project.ReportList;
import com.example.project.entity.*;
import com.example.project.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    public ScoreRepository scoreRepository;

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
    public String studentMP(Model model, @ModelAttribute("user") Users user) throws IOException {
        if(getStaticUser() == null || user.getId() != null && !getStaticUser().getId().equals(user.getId())){
            setStaticUser(user);
        }
        var studList = studentRepository.findAll();
        for (var item:studList) {
            if(item.getUsers().getId().equals(user.getId())) {
                setStaticStudent(item);
            }
        }
        var coursesList = courseRepository.findAll();
        model.addAttribute("courses", coursesList);
        var teacherList = teacherRepository.findAll();
        model.addAttribute("teachers", teacherList);
        model.addAttribute("staticStudent", getStaticStudent());

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
    public String studAddCoursePost (Model model, @ModelAttribute("studCourse") StudentCourse studentCourse) {
        studentCourse.setStudent(staticStudent);
        var studCourList = (ArrayList<StudentCourse>)studentCourseRepository.findAll();
        if(studCourList.size() == 0) {
            studentCourseRepository.save(studentCourse);
        }
        else {
            var count = 0;
            for (var item : studCourList) {
                if (item.getCourse().getId().equals(studentCourse.getCourse().getId()) && item.getStudent().getId().equals(studentCourse.getStudent().getId())) {
                    count++;
                }
            }
            if (count == 0) {
                studentCourseRepository.save(studentCourse);
            }
        }

        return "redirect:/studentAddCourse";
    }

    @GetMapping("/studentRating")
    public String studRatingGet(Model model){
        var scoreList = scoreRepository.findAll();
        model.addAttribute("scoreL", scoreList);
        model.addAttribute("staticStudent", getStaticStudent());
        return "studentRating";
    }

    @GetMapping("/studentReports/Add")
    public String AddReport(Model model){
        model.addAttribute("report", new Report());
        model.addAttribute("staticStudent", getStaticStudent());
        return "studentReports";
    }

    @PostMapping("/studentReports/Add")
    public String WriteReport(@ModelAttribute("report") Report report) throws IOException {
        report.setLogin(getStaticUser().getLogin());
        File file = new File("reports.json");
        if(file.length() == 0){
            var objectMapper = Mapper.GetMapper();
            ReportList reportList = new ReportList();
            var l = new ArrayList<Report>();
            l.add(report);
            reportList.setList(l);
            OutputStream outputStream = new FileOutputStream("reports.json");
            objectMapper.writeValue(outputStream, reportList);
        }
        else {
            InputStream inputStream = new FileInputStream("reports.json");
            var objectMapper = Mapper.GetMapper();
            var a = objectMapper.readValue(inputStream, ReportList.class);
                a.getList().add(report);
                OutputStream outputStream = new FileOutputStream("reports.json");
                objectMapper.writeValue(outputStream, a);
        }
        return "studentReports";
    }
    @GetMapping("/studentRateTeachers")
    public String studRateTeachGet (Model model) {
        var teacherList = teacherRepository.findAll();
        model.addAttribute("teachers", teacherList);
        model.addAttribute("teach", new Teacher());
        model.addAttribute("staticStudent", getStaticStudent());
        return "studentRateTeachers";
    }
    @PatchMapping("/studentRateTeachers")
    public String studRateTeachersPatch(@ModelAttribute("teacher") Teacher teacher) {
        var newObj1 = teacherRepository.findById(teacher.getId()).get();
        if (newObj1.getRating() == null) {
            newObj1.setRating(0);
        }
        int average = teacher.getRating() + newObj1.getRating();
        newObj1.setRating(average);
        teacherRepository.save(newObj1);
        return "redirect:/studentRateTeachers";
    }

    @GetMapping ("/ratingDiagram")
    public String diagram (Model model) {
        ArrayList<StudentCourse> stCourseList = (ArrayList<StudentCourse>) studentCourseRepository.findAll();
        Map<Object, Long> count = stCourseList.stream().collect(Collectors.groupingBy(e -> e.getCourse().getCourseName(), Collectors.counting()));
        model.addAttribute("course",count);

        model.addAttribute("staticStudent", getStaticStudent());
        return "ratingDiagram";
    }
}
