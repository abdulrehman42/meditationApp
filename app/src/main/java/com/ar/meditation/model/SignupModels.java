package com.ar.meditation.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "signup_table")
public class SignupModels {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String age;
    private String cnic;
    private String diseases;
    private String pass;

    // Default constructor
    public SignupModels() {
    }

    // Parameterized constructor
    public SignupModels(int id, String name, String age, String cnic, String diseases, String pass) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.cnic = cnic;
        this.diseases = diseases;
        this.pass = pass;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
