package com.example.ProjectHON.Post_Report;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="")
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String contentType;

    String reason;

    LocalDate timestamp;

    String comment;


//reporters id and offenders id

    // reporter FK
    @ManyToOne
    @JoinColumn(name = "reporter_user_id", referencedColumnName = "userId")
    private UserMaster reporter;

    // offender FK(owner of the post)
    @ManyToOne
    @JoinColumn(name = "offender_user_id", referencedColumnName = "userId")
    private UserMaster offender;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostMaster postreport;


    public PostReport() {
    }

    public PostReport(int id, String contentType, String reason, LocalDate timestamp, String comment, UserMaster reporter, UserMaster offender) {
        this.id = id;
        this.contentType = contentType;
        this.reason = reason;
        this.timestamp = timestamp;
        this.comment = comment;
        this.reporter = reporter;
        this.offender = offender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public UserMaster getReporter() {
        return reporter;
    }

    public void setReporter(UserMaster reporter) {
        this.reporter = reporter;
    }

    public UserMaster getOffender() {
        return offender;
    }

    public void setOffender(UserMaster offender) {
        this.offender = offender;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PostMaster getPostreport() {
        return postreport;
    }

    public void setPostreport(PostMaster postreport) {
        this.postreport = postreport;
    }
}
