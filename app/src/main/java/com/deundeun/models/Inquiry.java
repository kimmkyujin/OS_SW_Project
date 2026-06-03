package com.deundeun.models;

import com.google.gson.annotations.SerializedName;

public class Inquiry {
    private String id;
    
    @SerializedName("user_id")
    private String userId;
    
    private String category;
    private String title;
    private String content;
    private String status;
    private String answer;
    
    @SerializedName("answered_at")
    private String answeredAt;

    public Inquiry() {
        this.status = "Pending";
    }

    public Inquiry(String id, String userId, String category, String title, String content, String status, String answer) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.title = title;
        this.content = content;
        this.status = status;
        this.answer = answer;
    }

    public void submit() {
        this.status = "Pending";
    }

    public void addAnswer(String answer) {
        this.answer = answer;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US);
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        this.answeredAt = sdf.format(new java.util.Date());
        updateStatus("Completed");
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public String getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(String answeredAt) { this.answeredAt = answeredAt; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
