package com.example.project.repo;

import com.example.project.entity.StudentCourse;
import org.springframework.data.repository.CrudRepository;

public interface StudentCourseRepository extends CrudRepository<StudentCourse, Integer> {
}
