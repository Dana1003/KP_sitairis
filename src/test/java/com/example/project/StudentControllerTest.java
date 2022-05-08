package com.example.project;

import com.example.project.controllers.StudentController;
import com.example.project.entity.Student;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class StudentControllerTest {
    @Test
    void setStaticStudent() {
        Student student = new Student();
        student.setId(28);
        student.setName("Богдана");
        student.setLastName("Радецкая");
        student.setEmail("danaz.radetsksaya@gmail.com");
        student.setUsers(student.getUsers());
        student.setQuest(student.getQuest());
        StudentController.setStaticStudent(student);
        Assert.assertEquals(student, StudentController.getStaticStudent());
    }

}
