package com.ar.meditation.model;
public class SignupModels {
    private String name;
    private String age;
    private String cnic;
    private String diseases;
    private String pass;

    // Default constructor
    public SignupModels() {
        this.name = "";
        this.age = "";
        this.cnic = "";
        this.diseases = "";
        this.pass = "";
    }

    // Parameterized constructor
    public SignupModels(String name, String age, String cnic, String diseases, String pass) {
        this.name = name;
        this.age = age;
        this.cnic = cnic;
        this.diseases = diseases;
        this.pass = pass;
    }

    // Getter and Setter methods
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
