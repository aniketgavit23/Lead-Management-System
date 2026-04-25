package com.leadms;

import com.leadms.service.LeadService;
import com.leadms.model.Lead;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LeadService leadService = new LeadService();
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     CreativeLead CRM System");
        System.out.println("     Developed by Aniket");
        System.out.println("=".repeat(60));
        
        // Load existing leads
        leadService.loadLeads();
        System.out.println("✅ System ready! Loaded " + leadService.getTotalLeads() + " leads.");
        
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1" -> addLead();
                case "2" -> viewLeads();
                case "3" -> searchLeads();
                case "4" -> updateStatus();
                case "5" -> deleteLead();
                case "6" -> showStatistics();
                case "7" -> {
                    leadService.saveLeads();
                    System.out.println("\n👋 Thank you for using Lead Management System!");
                    System.out.println("📁 Data saved to: data/leads.txt");
                    System.out.println("⭐ Don't forget to star the project on GitHub!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please enter 1-7.");
            }
        }
    }
    
    private static void printMenu() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("1. ➕ Add New Lead");
        System.out.println("2. 📋 View All Leads");
        System.out.println("3. 🔍 Search Leads");
        System.out.println("4. 🔄 Update Lead Status");
        System.out.println("5. 🗑️ Delete Lead");
        System.out.println("6. 📈 View Statistics");
        System.out.println("7. 🚪 Exit System Safely");
        System.out.print("\nChoose option: ");
    }
    
    private static void addLead() {
        System.out.println("\n--- Add New Lead ---");
        
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("❌ Name is required!");
            return;
        }
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("❌ Email is required!");
            return;
        }
        
        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("❌ Invalid email format!");
            return;
        }
        
        System.out.print("Company: ");
        String company = scanner.nextLine().trim();
        if (company.isEmpty()) {
            System.out.println("❌ Company is required!");
            return;
        }
        
        System.out.print("Phone (optional): ");
        String phone = scanner.nextLine().trim();
        
        Lead lead = new Lead(name, email, company);
        if (!phone.isEmpty()) lead.setPhone(phone);
        
        if (leadService.addLead(lead)) {
            System.out.println("\n✅ Lead added successfully!");
            System.out.println("   📌 Lead ID: " + lead.getId());
            System.out.println("   ⭐ Lead Score: " + lead.getScore() + "/100");
            System.out.println("   📍 Status: " + lead.getStatus());
        } else {
            System.out.println("\n❌ A lead with this email already exists!");
        }
    }
    
    private static void viewLeads() {
        List<Lead> leads = leadService.getAllLeads();
        
        if (leads.isEmpty()) {
            System.out.println("\n📭 No leads found. Add some leads using option 1!");
            return;
        }
        
        System.out.println("\n📋 ALL LEADS");
        System.out.println("=".repeat(110));
        System.out.printf("%-5s %-22s %-28s %-22s %-8s %-12s%n", 
            "ID", "Name", "Email", "Company", "Score", "Status");
        System.out.println("-".repeat(110));
        
        for (Lead lead : leads) {
            System.out.printf("%-5d %-22s %-28s %-22s %-8d %-12s%n",
                lead.getId(),
                truncate(lead.getName(), 22),
                truncate(lead.getEmail(), 28),
                truncate(lead.getCompany(), 22),
                lead.getScore(),
                lead.getStatus()
            );
        }
        System.out.println("=".repeat(110));
        System.out.println("📊 Total Leads: " + leads.size());
    }
    
    private static void searchLeads() {
        System.out.print("\n🔍 Enter search term (name, email, or company): ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        
        if (keyword.isEmpty()) {
            System.out.println("❌ Please enter a search term.");
            return;
        }
        
        List<Lead> results = leadService.searchLeads(keyword);
        
        if (results.isEmpty()) {
            System.out.println("❌ No leads found matching '" + keyword + "'");
        } else {
            System.out.println("\n📋 Found " + results.size() + " lead(s):");
            System.out.println("-".repeat(80));
            for (Lead lead : results) {
                System.out.println(lead);
            }
            System.out.println("-".repeat(80));
        }
    }
    
    private static void updateStatus() {
        if (leadService.getAllLeads().isEmpty()) {
            System.out.println("\n📭 No leads to update.");
            return;
        }
        
        System.out.print("\nEnter Lead ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Lead lead = leadService.getLeadById(id);
            if (lead == null) {
                System.out.println("❌ Lead not found with ID: " + id);
                return;
            }
            
            System.out.println("\n📌 Current Lead: " + lead.getName());
            System.out.println("📊 Current Status: " + lead.getStatus());
            System.out.println("\nAvailable Statuses:");
            System.out.println("  1. NEW");
            System.out.println("  2. CONTACTED");
            System.out.println("  3. QUALIFIED");
            System.out.println("  4. WON");
            System.out.println("  5. LOST");
            
            System.out.print("\nSelect new status (1-5): ");
            String statusChoice = scanner.nextLine().trim();
            
            String newStatus = switch (statusChoice) {
                case "1" -> "NEW";
                case "2" -> "CONTACTED";
                case "3" -> "QUALIFIED";
                case "4" -> "WON";
                case "5" -> "LOST";
                default -> null;
            };
            
            if (newStatus == null) {
                System.out.println("❌ Invalid choice. Please select 1-5.");
                return;
            }
            
            if (leadService.updateLeadStatus(id, newStatus)) {
                System.out.println("✅ Status updated successfully!");
                System.out.println("   Lead: " + lead.getName());
                System.out.println("   New Status: " + newStatus);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID. Please enter a number.");
        }
    }
    
    private static void deleteLead() {
        if (leadService.getAllLeads().isEmpty()) {
            System.out.println("\n📭 No leads to delete.");
            return;
        }
        
        System.out.print("\nEnter Lead ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Lead lead = leadService.getLeadById(id);
            if (lead == null) {
                System.out.println("❌ Lead not found.");
                return;
            }
            
            System.out.println("\n⚠️  LEAD TO DELETE:");
            System.out.println("   " + lead);
            System.out.print("\nAre you sure? (yes/no): ");
            
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes") || confirm.equals("y")) {
                if (leadService.deleteLead(id)) {
                    System.out.println("✅ Lead deleted successfully!");
                }
            } else {
                System.out.println("❌ Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID. Please enter a number.");
        }
    }
    
    private static void showStatistics() {
        if (leadService.getAllLeads().isEmpty()) {
            System.out.println("\n📭 No leads to analyze. Add some leads first!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     📊 LEAD STATISTICS DASHBOARD");
        System.out.println("=".repeat(50));
        System.out.println("📈 Total Leads:     " + leadService.getTotalLeads());
        System.out.println("⭐ Average Score:   " + String.format("%.1f", leadService.getAverageScore()) + "/100");
        System.out.println("🏆 Highest Score:   " + leadService.getHighestScore() + "/100");
        
        System.out.println("\n📊 LEADS BY STATUS:");
        System.out.println("-".repeat(35));
        System.out.println("   • NEW:         " + leadService.countByStatus("NEW"));
        System.out.println("   • CONTACTED:   " + leadService.countByStatus("CONTACTED"));
        System.out.println("   • QUALIFIED:   " + leadService.countByStatus("QUALIFIED"));
        System.out.println("   • WON:         " + leadService.countByStatus("WON"));
        System.out.println("   • LOST:        " + leadService.countByStatus("LOST"));
        
        // Calculate conversion rate
        int totalPipeline = leadService.countByStatus("NEW") + 
                           leadService.countByStatus("CONTACTED") + 
                           leadService.countByStatus("QUALIFIED");
        int won = leadService.countByStatus("WON");
        double conversionRate = totalPipeline > 0 ? (double) won / totalPipeline * 100 : 0;
        
        System.out.println("\n📈 CONVERSION METRICS:");
        System.out.println("-".repeat(35));
        System.out.printf("   • Conversion Rate: %.1f%%%n", conversionRate);
        System.out.println("   • Pipeline Total:  " + totalPipeline);
        
        System.out.println("=".repeat(50));
    }
    
    private static String truncate(String str, int length) {
        if (str == null) return "N/A";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}        