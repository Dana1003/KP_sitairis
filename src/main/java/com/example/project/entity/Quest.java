package com.example.project.entity;

import javax.persistence.*;

@Entity
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @Column(name = "student_level", nullable = false)
    protected String studentLevel;

    @Column(name = "languages")
    protected String languages;

    @Column(name = "english_level", nullable = false)
    protected String englishLevel;

    @Column(name = "goals")
    protected String goals;

    @Column(name = "sex", nullable = false)
    protected String sex;

    @Column(name = "age", nullable = false)
    protected Integer age;

    @Column(name = "phone_number")
    protected Integer phoneNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentLevel() {
        return studentLevel;
    }

    public void setStudentLevel(String studentLevel) {
        this.studentLevel = studentLevel;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id=" + id +
                ", studentLevel='" + studentLevel + '\'' +
                ", languages='" + languages + '\'' +
                ", englishLevel='" + englishLevel + '\'' +
                ", goals='" + goals + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
