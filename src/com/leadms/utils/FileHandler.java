package com.leadms.utils;

import com.leadms.model.Lead;
import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final String FILE_NAME = DATA_DIR + File.separator + "leads.txt";
    
    public FileHandler() {
        createDataDirectory();
    }
    
    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdir();
            if (created) {
                System.out.println("📁 Created data directory: " + DATA_DIR);
            }
        }
    }
    
    public void saveLeads(List<Lead> leads) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Lead lead : leads) {
                writer.println(lead.toFileString());
            }
            System.out.println("💾 Saved " + leads.size() + " leads to file.");
        } catch (IOException e) {
            System.err.println("❌ Error saving leads: " + e.getMessage());
        }
    }
    
    public List<Lead> loadLeads() {
        List<Lead> leads = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            System.out.println("📝 No existing data found. Creating sample data...");
            createSampleData();
            file = new File(FILE_NAME);
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        leads.add(Lead.fromFileString(line));
                    } catch (Exception e) {
                        System.err.println("⚠️ Error parsing line: " + line);
                    }
                }
            }
            System.out.println("📂 Loaded " + leads.size() + " leads from file.");
        } catch (IOException e) {
            System.err.println("❌ Error loading leads: " + e.getMessage());
        }
        
        return leads;
    }
    
    private void createSampleData() {
        List<Lead> sampleLeads = new ArrayList<>();
        
        Lead lead1 = new Lead("John Doe", "john.doe@techcorp.com", "Tech Corp");
        lead1.setStatus("CONTACTED");
        lead1.setPhone("+1 (555) 123-4567");
        sampleLeads.add(lead1);
        
        Lead lead2 = new Lead("Jane Smith", "jane.smith@datasystems.com", "Data Systems Inc");
        lead2.setStatus("QUALIFIED");
        lead2.setPhone("+1 (555) 987-6543");
        sampleLeads.add(lead2);
        
        Lead lead3 = new Lead("Bob Johnson", "bob.johnson@startup.io", "Startup.io");
        lead3.setStatus("NEW");
        lead3.setPhone("+1 (555) 456-7890");
        sampleLeads.add(lead3);
        
        Lead lead4 = new Lead("Alice Williams", "alice.williams@enterprise.com", "Enterprise Solutions");
        lead4.setStatus("NEGOTIATION");
        lead4.setPhone("+1 (555) 789-0123");
        sampleLeads.add(lead4);
        
        Lead lead5 = new Lead("Charlie Brown", "charlie@ai-tech.ai", "AI Tech Solutions");
        lead5.setStatus("NEW");
        lead5.setPhone("+1 (555) 321-0987");
        sampleLeads.add(lead5);
        
        saveLeads(sampleLeads);
        System.out.println("✅ Created 5 sample leads for demonstration!");
    }
}
