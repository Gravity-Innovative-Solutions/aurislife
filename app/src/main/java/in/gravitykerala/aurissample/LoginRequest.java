package in.gravitykerala.aurissample;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @com.google.gson.annotations.SerializedName("userName")
    public String uName;

    @com.google.gson.annotations.SerializedName("password")
    public String Pword;
    @com.google.gson.annotations.SerializedName("id")
    public String mId;
}
