package com.example.jmp;

import java.sql.Blob;

public class User {

    int id;
    String name;
    String address;
    String gender;
    String phone;
    String city;
    byte[] photo;

    public User(){
        
    }

    public User(String name, String address, String gender, String phone, String city, byte[] photo) {

        this.name =name;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
        this.city = city;
        this.photo = photo;
    }


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) { this.city = city; }

    public byte[] getPhoto() { return photo; }

    public void setPhoto (byte[] photo) {this.photo = photo;}

}

