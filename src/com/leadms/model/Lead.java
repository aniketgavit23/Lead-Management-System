package com.leadms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Lead implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    
    private int id;
    private String name;
    private String email;
    private String company;
    private String phone;
    private String status;
    private int score;
    private String createdAt;
    
    public Lead(String name, String email, String company) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
        this.company = company;
        this.status = "NEW";
        this.score = calculateScore();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    private int calculateScore() {
        int score = 50; // Base score
        
        // Company-based scoring
        if (company != null) {
            String companyLower = company.toLowerCase();
            if (companyLower.contains("tech") || companyLower.contains("software")) {
                score += 25;
            }
            if (companyLower.contains("ai") || companyLower.contains("data") || companyLower.contains("cloud")) {
                score += 20;
            }
            if (companyLower.contains("startup") || companyLower.contains("saas")) {
                score += 15;
            }
            if (companyLower.contains("enterprise") || companyLower.contains("corp")) {
                score += 10;
            }
        }
        
        // Name-based scoring
        if (name != null && name.length() > 0) {
            score += 10;
            if (name.length() > 10) score += 5;
        }
        
        // Email-based scoring
        if (email != null && email.contains("@")) {
            String domain = email.substring(email.indexOf("@"));
            if (domain.contains(".com") || domain.contains(".org") || domain.contains(".io")) {
                score += 5;
            }
            if (!domain.contains("gmail") && !domain.contains("yahoo") && !domain.contains("hotmail")) {
                score += 10; // Business email
            }
        }
        
        return Math.min(score, 100);
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public static void setNextId(int id) { nextId = id; }
    public static int getNextId() { return nextId; }
    
    public String toFileString() {
        return id + "|" + name + "|" + email + "|" + company + "|" + 
               (phone != null ? phone : "") + "|" + status + "|" + score + "|" + createdAt;
    }
    
    public static Lead fromFileString(String line) {
        String[] parts = line.split("\\|");
        Lead lead = new Lead(parts[1], parts[2], parts[3]);
        lead.id = Integer.parseInt(parts[0]);
        lead.phone = parts[4].isEmpty() ? null : parts[4];
        lead.status = parts[5];
        lead.score = Integer.parseInt(parts[6]);
        lead.createdAt = parts[7];
        return lead;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %-20s | %-25s | %-20s | Score: %3d | %s",
            id, name, email, company, score, status);
    }
}