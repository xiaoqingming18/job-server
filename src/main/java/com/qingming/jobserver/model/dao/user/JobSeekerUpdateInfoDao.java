package com.qingming.jobserver.model.dao.user;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import java.time.LocalDate;

public class JobSeekerUpdateInfoDao {
    // 用户基础信息
    @Null(groups = {Default.class}, message = "用户ID不能修改")
    private Long userId;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {UpdateGroup.class})
    private String mobile;
    
    @Email(message = "邮箱格式不正确", groups = {UpdateGroup.class})
    private String email;
    
    @Pattern(regexp = "^https?://.*", message = "头像地址格式不正确", groups = {UpdateGroup.class})
    private String avatar;

    // 求职者扩展信息
    @Size(min = 2, max = 5, message = "真实姓名长度2-5位", groups = {UpdateGroup.class})
    private String realName;
    
    @Pattern(regexp = "^(male|female)$", message = "性别只能为male或female", groups = {UpdateGroup.class})
    private String gender;
    
    @Past(message = "出生日期必须为过去时间", groups = {UpdateGroup.class})
    private LocalDate birthday;
    
    @Size(max = 30, message = "期望职位最长30字符", groups = {UpdateGroup.class})
    private String expectPosition;
    
    @Min(value = 0, message = "工作年限不能小于0", groups = {UpdateGroup.class})
    @Max(value = 50, message = "工作年限不能超过50", groups = {UpdateGroup.class})
    private Integer workYears;
    
    @Size(max = 500, message = "技能描述最长500字符", groups = {UpdateGroup.class})
    private String skill;
    
    @Size(max = 200, message = "证书信息最长200字符", groups = {UpdateGroup.class})
    private String certificates;
    
    @Pattern(regexp = "^https?://.*\\.(pdf|docx)$", message = "简历必须为PDF或Word文档", groups = {UpdateGroup.class})
    private String resumeUrl;

    // 分组校验接口
    public interface UpdateGroup {}
    public interface CreateGroup {}

    // getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public String getExpectPosition() { return expectPosition; }
    public void setExpectPosition(String expectPosition) { this.expectPosition = expectPosition; }

    public Integer getWorkYears() { return workYears; }
    public void setWorkYears(Integer workYears) { this.workYears = workYears; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public String getCertificates() { return certificates; }
    public void setCertificates(String certificates) { this.certificates = certificates; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
}