package com.example.fitzoneadmin;

import java.lang.reflect.Member;

public class MemberList {
        private String name;
        private String email;

        public MemberList(String name, String email) {
            this.name = name;
            this.email = email;
        }

    public String getName() {return name;}
    public String getEmail() {return email;}
}
