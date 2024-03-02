package com.example.fitzoneadmin;

public class ExercisesItemList {
    private String name;
    private String body;
    private String imageUrl;

    public ExercisesItemList() {
        // Empty constructor needed for Firestore
    }

    public ExercisesItemList(String name, String body, String imageUrl) {
        this.name = name;
        this.body = body;
        this.imageUrl = imageUrl;
    }

    // Getters and setters for the properties
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
