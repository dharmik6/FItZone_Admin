package com.example.fitzoneadmin;

import java.lang.reflect.Member;

public class MemberList {
    private String name;
    private String email,number,gender,age,address,acticity,joidate;

    public MemberList(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getNumber() {return number;}
    public String getAge() {return age;}
    public String getGender() {return gender;}
    public String getAddress() {return address;}
    public String getActicity() {return acticity;}
    public String getJoidate() {return joidate;}

//    public void setName(String name){this.name=name;}
//    public void setEmail(String email) {this.email=email;}
//    public void setNumber(String number) {this.number= number;}
//    public void setAge(String age) {this.age= age;}
//    public void setGender(String gender) {this.gender= gender;}
//    public void setAddress(String address) {this.address= address;}
//    public void setActicity(String acticity) {this.acticity= acticity;}
//    public void setJoidate(String joidate) {this.joidate= joidate;}
}
