package com.example.project;

import com.example.project.controllers.AdminController;
import com.example.project.entity.Teacher;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminControllerTest {
    @Test
    void setTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(18);
        teacher.setName("Богдана");
        teacher.setLastName("Радецкая");
        teacher.setExperience(1);
        teacher.setUsers(teacher.getUsers());
        AdminController.setTeacher(teacher);
        Assert.assertEquals(teacher, AdminController.getTeacher());
    }

}
