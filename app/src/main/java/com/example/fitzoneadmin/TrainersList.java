package com.example.fitzoneadmin;

public class TrainersList {
    private String tname, timage,review,id;
    private String temail, tnumber, tgender, tboi, taddress,specialization, experience, tjoidate ;

    public TrainersList(String tname, String experience, String timage,String specialization,String id) {
        this.tname = tname;
        this.experience =experience;
        this.timage=timage;
        this.id=id;
        this.specialization=specialization;
    }


        public String getTname() {return tname;}
        public String getReview() {return review;}
    public String getTimage() {return timage;}
    public String getSpecialization() {return specialization;}

    public String getId() {
        return id;
    }

    public String getTnumber() {return tnumber;}
    public String getTgender() {return tgender;}
    public String getTboi() {return tboi;}
    public String getTaddress() {return taddress;}
    public String getExperience() {return experience;}
    public String getTjoidate() {return tjoidate;}

}