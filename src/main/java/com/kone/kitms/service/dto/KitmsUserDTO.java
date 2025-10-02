package com.kone.kitms.service.dto;

import com.kone.kitms.domain.KitmsUser;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public class KitmsUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userNo;

    @NotNull
    private String userId;

    @NotNull
    private String userPwd;

    @NotNull
    private String userName;

    private String userTel;

    private String userMobile;

    private String userEmail;

    @NotNull
    private Boolean enable;

    private String userEtc;

    private LocalDate vacStartDate;

    private LocalDate vacEndDate;

    private ZonedDateTime createDt;

    private String createUserId;

    private Boolean firstFlag;

    private ZonedDateTime lastPassModDt;

    @NotNull
    private String authCode;

    private String authValue;

    private Long compNo;

    private String compName;

    private Long attachNo;

    private String attachFilePath;

    private String attachFileName;

    private double serviceStarAvg;

    private List<Long> branchNoList;

    private Long onsiteBranchNo;

    public String getOnsiteBranchName() {
        return onsiteBranchName;
    }

    public void setOnsiteBranchName(String onsiteBranchName) {
        this.onsiteBranchName = onsiteBranchName;
    }

    private String onsiteBranchName;
    private String userRole;
    private Long organNo;
    private String organName;
    private String organTypeName;
    
    private String useFlag;

    public KitmsUserDTO() {}

    public KitmsUserDTO(
        KitmsUser kitmsUser,
        String authValue,
        Long attachNo,
        String attachFilePath,
        String attachFileName,
        double serviceStarAvg
    ) {
        this.userNo = kitmsUser.getUserNo();
        this.userId = kitmsUser.getUserId();
        this.userPwd = kitmsUser.getUserPwd();
        this.userName = kitmsUser.getUserName();
        this.userTel = kitmsUser.getUserTel();
        this.userMobile = kitmsUser.getUserMobile();
        this.userEmail = kitmsUser.getUserEmail();
        this.enable = kitmsUser.getEnable();
        this.vacStartDate = kitmsUser.getVacStartDate();
        this.vacEndDate = kitmsUser.getVacEndDate();
        this.userEtc = kitmsUser.getUserEtc();
        this.createDt = kitmsUser.getCreateDt();
        this.createUserId = kitmsUser.getCreateUserId();
        this.firstFlag = kitmsUser.getFirstFlag();
        this.lastPassModDt = kitmsUser.getLastPassModDt();
        this.authCode = kitmsUser.getAuthCode();
        this.authValue = authValue;
        this.attachNo = attachNo;
        this.attachFilePath = attachFilePath;
        this.attachFileName = attachFileName;
        this.serviceStarAvg = serviceStarAvg;
        this.onsiteBranchNo = kitmsUser.getOnsiteBranchNo();
        this.userRole = kitmsUser.getUserRole();
        this.organNo = kitmsUser.getOrganNo();
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public LocalDate getVacStartDate() {
        return vacStartDate;
    }

    public void setVacStartDate(LocalDate vacStartDate) {
        this.vacStartDate = vacStartDate;
    }

    public LocalDate getVacEndDate() {
        return vacEndDate;
    }

    public void setVacEndDate(LocalDate vacEndDate) {
        this.vacEndDate = vacEndDate;
    }

    public String getUserEtc() {
        return userEtc;
    }

    public void setUserEtc(String userEtc) {
        this.userEtc = userEtc;
    }

    public ZonedDateTime getCreateDt() {
        return createDt;
    }

    public void setCreateDt(ZonedDateTime createDt) {
        this.createDt = createDt;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Boolean getFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(Boolean firstFlag) {
        this.firstFlag = firstFlag;
    }

    public ZonedDateTime getLastPassModDt() {
        return lastPassModDt;
    }

    public void setLastPassModDt(ZonedDateTime lastPassModDt) {
        this.lastPassModDt = lastPassModDt;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthValue() {
        return authValue;
    }

    public void setAuthValue(String authValue) {
        this.authValue = authValue;
    }

    public Long getCompNo() {
        return compNo;
    }

    public void setCompNo(Long compNo) {
        this.compNo = compNo;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public Long getAttachNo() {
        return attachNo;
    }

    public void setAttachNo(Long attachNo) {
        this.attachNo = attachNo;
    }

    public String getAttachFilePath() {
        return attachFilePath;
    }

    public void setAttachFilePath(String attachFilePath) {
        this.attachFilePath = attachFilePath;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public double getServiceStarAvg() {
        return serviceStarAvg;
    }

    public void setServiceStarAvg(double serviceStarAvg) {
        this.serviceStarAvg = serviceStarAvg;
    }

    public List<Long> getBranchNoList() {
        return branchNoList;
    }

    public void setBranchNoList(List<Long> branchNoList) {
        this.branchNoList = branchNoList;
    }

    public Long getOnsiteBranchNo() {
        return onsiteBranchNo;
    }

    public void setOnsiteBranchNo(Long onsiteBranchNo) {
        this.onsiteBranchNo = onsiteBranchNo;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getOrganNo() {
        return organNo;
    }

    public void setOrganNo(Long organNo) {
        this.organNo = organNo;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganTypeName() {
        return organTypeName;
    }

    public void setOrganTypeName(String organType) {
        this.organTypeName = organType;
    }

    public String getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }

    @Override
    public String toString() {
        return (
            "KitmsUserDTO{" +
            "userNo=" +
            userNo +
            ", userId='" +
            userId +
            '\'' +
            ", userPwd='" +
            userPwd +
            '\'' +
            ", userName='" +
            userName +
            '\'' +
            ", userTel='" +
            userTel +
            '\'' +
            ", userMobile='" +
            userMobile +
            '\'' +
            ", userEmail='" +
            userEmail +
            '\'' +
            ", enable=" +
            enable +
            ", userEtc='" +
            userEtc +
            '\'' +
            ", vacStartDate=" +
            vacStartDate +
            ", vacEndDate=" +
            vacEndDate +
            ", createDt=" +
            createDt +
            ", createUserId='" +
            createUserId +
            '\'' +
            ", firstFlag=" +
            firstFlag +
            ", lastPassModDt=" +
            lastPassModDt +
            ", authCode='" +
            authCode +
            '\'' +
            ", authValue='" +
            authValue +
            '\'' +
            ", compNo=" +
            compNo +
            ", compName='" +
            compName +
            '\'' +
            ", attachNo=" +
            attachNo +
            ", attachFilePath='" +
            attachFilePath +
            '\'' +
            ", attachFileName='" +
            attachFileName +
            '\'' +
            ", serviceStarAvg=" +
            serviceStarAvg +
            ", branchNoList=" +
            branchNoList +
            ", onsiteBranchNo=" +
            onsiteBranchNo +
            ", userRole='" +
            userRole +
            '\'' +
            ", organNo=" +
            organNo +
            ", organName='" +
            organName +
            '\'' +
            ", organTypeName='" +
            organTypeName +
            '\'' +
            '}'
        );
    }

    public static KitmsUser builderToEntity(KitmsUserDTO dto, String firstFlag) {
        KitmsUser kitmsUser = new KitmsUser();
        kitmsUser.setUserNo(dto.getUserNo());
        kitmsUser.setUserId(dto.getUserId());
        kitmsUser.setUserPwd(dto.getUserPwd());
        kitmsUser.setUserName(dto.getUserName());
        kitmsUser.setUserTel(dto.getUserTel());
        kitmsUser.setUserMobile(dto.getUserMobile());
        kitmsUser.setEnable(true);
        kitmsUser.setVacStartDate(dto.getVacStartDate());
        kitmsUser.setVacEndDate(dto.getVacEndDate());
        kitmsUser.setUserEmail(dto.getUserEmail());
        kitmsUser.setUserEtc(dto.getUserEtc());
        kitmsUser.setCreateDt(ZonedDateTime.now().plusHours(9));
        kitmsUser.setCreateUserId(dto.getCreateUserId());
        kitmsUser.setLastPassModDt(dto.getLastPassModDt());
        kitmsUser.setAuthCode(dto.getAuthCode());
        kitmsUser.setFirstFlag(firstFlag.equals("I"));
        kitmsUser.setOnsiteBranchNo(dto.getOnsiteBranchNo());
        kitmsUser.setUserRole(dto.getUserRole());
        kitmsUser.setOrganNo(dto.getOrganNo());

        return kitmsUser;
    }
}
