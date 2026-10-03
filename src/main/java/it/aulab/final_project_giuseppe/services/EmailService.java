package it.aulab.final_project_giuseppe.services;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
}