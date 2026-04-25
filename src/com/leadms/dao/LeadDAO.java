package com.leadms.dao;

import com.leadms.model.Lead;
import com.leadms.utils.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

public class LeadDAO {
    private List<Lead> leads;
    private FileHandler fileHandler;
    
    public LeadDAO() {
        this.leads = new ArrayList<>();
        this.fileHandler = new FileHandler();
    }
    
    public boolean addLead(Lead lead) {
        // Check for duplicate email
        if (findByEmail(lead.getEmail()) != null) {
            return false;
        }
        leads.add(lead);
        return true;
    }
    
    public List<Lead> getAllLeads() {
        return new ArrayList<>(leads);
    }
    
    public Lead findById(int id) {
        return leads.stream()
            .filter(lead -> lead.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    public Lead findByEmail(String email) {
        return leads.stream()
            .filter(lead -> lead.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }
    
    public List<Lead> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return leads.stream()
            .filter(lead -> 
                lead.getName().toLowerCase().contains(lowerKeyword) ||
                lead.getEmail().toLowerCase().contains(lowerKeyword) ||
                lead.getCompany().toLowerCase().contains(lowerKeyword) ||
                (lead.getPhone() != null && lead.getPhone().contains(keyword))
            )
            .collect(Collectors.toList());
    }
    
    public boolean updateLead(Lead lead) {
        int index = -1;
        for (int i = 0; i < leads.size(); i++) {
            if (leads.get(i).getId() == lead.getId()) {
                index = i;
                break;
            }
        }
        
        if (index != -1) {
            leads.set(index, lead);
            return true;
        }
        return false;
    }
    
    public boolean deleteLead(int id) {
        Lead lead = findById(id);
        if (lead != null) {
            leads.remove(lead);
            return true;
        }
        return false;
    }
    
    public List<Lead> getLeadsByStatus(String status) {
        return leads.stream()
            .filter(lead -> lead.getStatus().equals(status))
            .collect(Collectors.toList());
    }
    
    public double getAverageScore() {
        return leads.stream()
            .mapToInt(Lead::getScore)
            .average()
            .orElse(0.0);
    }
    
    public int getHighestScore() {
        return leads.stream()
            .mapToInt(Lead::getScore)
            .max()
            .orElse(0);
    }
    
    public void loadLeads() {
        List<Lead> loadedLeads = fileHandler.loadLeads();
        if (!loadedLeads.isEmpty()) {
            this.leads = loadedLeads;
            // Update nextId
            int maxId = leads.stream().mapToInt(Lead::getId).max().orElse(0);
            Lead.setNextId(maxId + 1);
        }
    }
    
    public void saveLeads() {
        fileHandler.saveLeads(leads);
    }
    
    public int getTotalLeads() {
        return leads.size();
    }
    
    public int countByStatus(String status) {
        return (int) leads.stream()
            .filter(lead -> lead.getStatus().equals(status))
            .count();
    }
}