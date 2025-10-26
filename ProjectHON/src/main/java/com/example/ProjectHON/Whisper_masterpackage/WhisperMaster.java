package com.example.ProjectHON.Whisper_masterpackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class WhisperMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long whisperId;

    @ManyToMany
    @JoinTable(
            name = "whisper_sender", // Join table name for the sender
            joinColumns = @JoinColumn(name = "whisper_id"), // Foreign key for the WhisperMaster
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key for the UserMaster (sender)
    )
    private List<UserMaster> sender;

    @ManyToMany
    @JoinTable(
            name = "whisper_receiver", // Join table name for the receiver
            joinColumns = @JoinColumn(name = "whisper_id"), // Foreign key for the WhisperMaster
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key for the UserMaster (receiver)
    )
    private List<UserMaster> receiver;

    private String message;

    private LocalDateTime whisperTime;

    public WhisperMaster(Long whisperId, List<UserMaster> sender, List<UserMaster> receiver, String message, LocalDateTime whisperTime) {
        this.whisperId = whisperId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.whisperTime = whisperTime;
    }

    public WhisperMaster() {
    }

    public Long getWhisperId() {
        return whisperId;
    }

    public void setWhisperId(Long whisperId) {
        this.whisperId = whisperId;
    }

    public List<UserMaster> getSender() {
        return sender;
    }

    public void setSender(List<UserMaster> sender) {
        this.sender = sender;
    }

    public List<UserMaster> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<UserMaster> receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getWhisperTime() {
        return whisperTime;
    }

    public void setWhisperTime(LocalDateTime whisperTime) {
        this.whisperTime = whisperTime;
    }
}
