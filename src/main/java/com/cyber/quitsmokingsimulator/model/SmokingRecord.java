package com.cyber.quitsmokingsimulator.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "smoking_records")
public class SmokingRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime smokingTime;
    
    @Column(nullable = false)
    private String userAgent;
    
    @Column
    private String ipAddress;
    
    @Column
    private Double smokingIntensity;
    
    public SmokingRecord() {
        this.smokingTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getSmokingTime() { return smokingTime; }
    public void setSmokingTime(LocalDateTime smokingTime) { this.smokingTime = smokingTime; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public Double getSmokingIntensity() { return smokingIntensity; }
    public void setSmokingIntensity(Double smokingIntensity) { this.smokingIntensity = smokingIntensity; }
} 