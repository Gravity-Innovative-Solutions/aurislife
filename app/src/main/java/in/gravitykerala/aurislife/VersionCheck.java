package in.gravitykerala.aurislife;

/**
 * Created by USER on 9/3/2015.
 */
public class VersionCheck {
    @com.google.gson.annotations.SerializedName("id")
    public String mId;

    @com.google.gson.annotations.SerializedName("latest_Version_Name")
    public String Vname;

    @com.google.gson.annotations.SerializedName("mandatory_Version_Nam")
    public String mVname;

    @com.google.gson.annotations.SerializedName("latest_Version_Code")
    public double Vcode;

    @com.google.gson.annotations.SerializedName("mandatory_Version_Code")
    public double mVcode;
}
