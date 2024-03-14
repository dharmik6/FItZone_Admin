package com.example.fitzoneadmin;

public class NotificationItemList {
    private String feedback_name;
    private String feedback;

    public NotificationItemList(String feedback_name,String feedback) {
        this.feedback_name = feedback_name;
        this.feedback = feedback;
    }

    // Getters and setters for the properties
    public String getFeedback_name() {
        return feedback_name;
    }

    public void setFeedback_name(String feedback_name) {
        this.feedback_name = feedback_name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

}
