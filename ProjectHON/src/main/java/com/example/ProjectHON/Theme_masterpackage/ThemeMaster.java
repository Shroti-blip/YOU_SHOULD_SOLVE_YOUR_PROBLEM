package com.example.ProjectHON.Theme_masterpackage;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class ThemeMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeId;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL)
    List <PostMaster> theme;

    private String themeName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String themeStatus;

    public ThemeMaster(Long themeId, String themeName, LocalDate startDate, LocalDate endDate, String themeStatus) {
        this.themeId = themeId;
        this.themeName = themeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.themeStatus = themeStatus;
    }

    public ThemeMaster() {
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getThemeStatus() {
        return themeStatus;
    }

    public void setThemeStatus(String themeStatus) {
        this.themeStatus = themeStatus;
    }
}
