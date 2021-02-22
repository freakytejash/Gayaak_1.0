package com.example.gayaak_10.student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllTutorsDemoDetail {

    @SerializedName("UserId")
    @Expose
    public Integer userId;
    @SerializedName("RoleId")
    @Expose
    public Integer roleId;
    @SerializedName("Role")
    @Expose
    public Object role;
    @SerializedName("IsAdmin")
    @Expose
    public Boolean isAdmin;
    @SerializedName("FirstName")
    @Expose
    public String firstName;
    @SerializedName("LastName")
    @Expose
    public String lastName;
    @SerializedName("FullName")
    @Expose
    public Object fullName;
    @SerializedName("Email")
    @Expose
    public String email;
    @SerializedName("Phone")
    @Expose
    public String phone;
    @SerializedName("Gender")
    @Expose
    public Integer gender;
    @SerializedName("ProfileImage")
    @Expose
    public String profileImage;
    @SerializedName("IsEmailVerified")
    @Expose
    public Object isEmailVerified;
    @SerializedName("IsPhoneVerified")
    @Expose
    public Object isPhoneVerified;
    @SerializedName("Password")
    @Expose
    public Object password;
    @SerializedName("FacebookId")
    @Expose
    public Object facebookId;
    @SerializedName("GoogleId")
    @Expose
    public Object googleId;
    @SerializedName("LastLogin")
    @Expose
    public Object lastLogin;
    @SerializedName("RegisterType")
    @Expose
    public Object registerType;
    @SerializedName("OTP")
    @Expose
    public Object oTP;
    @SerializedName("LoginOrSignup")
    @Expose
    public Integer loginOrSignup;
    @SerializedName("UserName")
    @Expose
    public Object userName;
    @SerializedName("LoginUserId")
    @Expose
    public Integer loginUserId;
    @SerializedName("ImageName")
    @Expose
    public String imageName;
    @SerializedName("UserImage")
    @Expose
    public Object userImage;
    @SerializedName("ImageData")
    @Expose
    public Object imageData;
    @SerializedName("AccountTypeName")
    @Expose
    public Object accountTypeName;
    @SerializedName("CountryId")
    @Expose
    public Object countryId;
    @SerializedName("CountryName")
    @Expose
    public Object countryName;
    @SerializedName("CountryCode")
    @Expose
    public Integer countryCode;
    @SerializedName("TimeZone")
    @Expose
    public Object timeZone;
    @SerializedName("LiveClassDetailDataContract")
    @Expose
    public ArrayList<LiveClassDetailDataContract> liveClassDetailDataContract = null;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public String modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Integer modifiedBy;

}
