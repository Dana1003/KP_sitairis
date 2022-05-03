package com.example.project.entity;

import javax.persistence.*;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    protected Users users;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "last_name", nullable = false)
    protected String lastName;

    @Column(name = "experience", nullable = false)
    protected Integer experience;

    @Column(name = "additional_info")
    protected String additionalInfo;

    @Column(name = "rating")
    protected Integer rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", users=" + users +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", experience=" + experience +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", rating=" + rating +
                '}';
    }
}
