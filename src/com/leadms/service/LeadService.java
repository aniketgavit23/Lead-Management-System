package com.leadms.service;

import com.leadms.dao.LeadDAO;
import com.leadms.model.Lead;
import java.util.List;

public class LeadService {
    private LeadDAO leadDAO;
    
    public LeadService() {
        this.leadDAO = new LeadDAO();
    }
    
    public boolean addLead(Lead lead) {
        return leadDAO.addLead(lead);
    }
    
    public List<Lead> getAllLeads() {
        return leadDAO.getAllLeads();
    }
    
    public Lead getLeadById(int id) {
        return leadDAO.findById(id);
    }
    
    public List<Lead> searchLeads(String keyword) {
        return leadDAO.search(keyword);
    }
    
    public boolean updateLeadStatus(int id, String newStatus) {
        Lead lead = leadDAO.findById(id);
        if (lead != null) {
            lead.setStatus(newStatus);
            return leadDAO.updateLead(lead);
        }
        return false;
    }
    
    public boolean deleteLead(int id) {
        return leadDAO.deleteLead(id);
    }
    
    public double getAverageScore() {
        return leadDAO.getAverageScore();
    }
    
    public int getHighestScore() {
        return leadDAO.getHighestScore();
    }
    
    public int getTotalLeads() {
        return leadDAO.getTotalLeads();
    }
    
    public int countByStatus(String status) {
        return leadDAO.countByStatus(status);
    }
    
    public void loadLeads() {
        leadDAO.loadLeads();
    }
    
    public void saveLeads() {
        leadDAO.saveLeads();
    }
}