package com.example.project.entity;

import javax.persistence.*;

@Entity
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", unique = true, nullable = false)
    protected Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", unique = true, nullable = false)
    protected Course course;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "StudentCourse{" +
                "id=" + id +
                ", student=" + student +
                ", course=" + course +
                '}';
    }
}
