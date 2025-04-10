package com.qingming.jobserver.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "job_seeker")
@Data
public class JobSeeker {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "enum('male','female','other')")
    private Gender gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status", columnDefinition = "enum('seeking','employed','inactive')")
    private JobStatus jobStatus = JobStatus.SEEKING;

    @Column(name = "expect_position", length = 100)
    private String expectPosition;

    @Column(name = "work_years")
    private Integer workYears;

    @Column(name = "skill", columnDefinition = "text")
    private String skill;

    @Convert(converter = JsonConverter.class)
    @Column(name = "certificates", columnDefinition = "json")
    private Object certificates;

    @Column(name = "resume_url", length = 255)
    private String resumeUrl;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum JobStatus {
        SEEKING, EMPLOYED, INACTIVE
    }
}