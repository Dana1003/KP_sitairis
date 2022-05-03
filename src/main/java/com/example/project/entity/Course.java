package com.example.project.entity;

import javax.persistence.*;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", unique = true, nullable = false)
    protected Teacher teacher;

    @Column(name = "course_name", nullable = false)
    protected String courseName;

    @Column(name = "language", nullable = false)
    protected String language;

    @Column(name = "duration", nullable = false)
    protected Integer duration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", courseName='" + courseName + '\'' +
                ", language='" + language + '\'' +
                ", duration=" + duration +
                '}';
    }
}
