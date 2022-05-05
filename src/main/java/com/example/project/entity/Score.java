package com.example.project.entity;

import javax.persistence.*;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_course_id", nullable = false)
    protected StudentCourse studentCourse;

    @Column(name = "task", nullable = false)
    protected Integer task;

    @Column(name = "grade", nullable = false)
    protected Integer grade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", studentCourse=" + studentCourse +
                ", task=" + task +
                ", grade=" + grade +
                '}';
    }
}
