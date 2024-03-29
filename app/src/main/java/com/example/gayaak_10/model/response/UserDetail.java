package com.example.gayaak_10.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetail {

    @SerializedName("UserId")
    @Expose
    public int userId;
    @SerializedName("RoleId")
    @Expose
    public Integer roleId;
    @SerializedName("FirstName")
    @Expose
    public String firstName;
    @SerializedName("LastName")
    @Expose
    public String lastName;
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
    public Boolean isEmailVerified;
    @SerializedName("IsPhoneVerified")
    @Expose
    public Boolean isPhoneVerified;
    @SerializedName("Password")
    @Expose
    public String password;
    @SerializedName("FacebookId")
    @Expose
    public int facebookId;
    @SerializedName("GoogleId")
    @Expose
    public int googleId;
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
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    public Object createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    public Object modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    public Object createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    public Object modifiedBy;
    @SerializedName("CountryCode")
    @Expose
    public Integer countryCode;
    @SerializedName("CurrencyName")
    @Expose
    public Object currencyName;
    @SerializedName("CountryName")
    @Expose
    public String CountryName;
    @SerializedName("CountryId")
    @Expose
    public Integer CountryId;
    @SerializedName("UserWalletDataContract")
    @Expose
    public UserWalletDataContract userWalletDataContract = new UserWalletDataContract();
}
