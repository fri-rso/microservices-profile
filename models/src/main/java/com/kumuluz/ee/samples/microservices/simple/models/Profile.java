package com.kumuluz.ee.samples.microservices.simple.models;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "profiles")
@NamedQuery(name = "Profile.findAll", query = "SELECT p FROM Profile p")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String surname;

    private Integer phone;

    private String email;

    private String adress;

    private Integer postnumb;

    /*Getters - Setters*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Integer getPostnumb() {
        return postnumb;
    }

    public void setPostnumb(Integer postnumb) {
        this.postnumb = postnumb;
    }
}