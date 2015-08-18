package in.gravitykerala.aurissample;

/**
 * Created by USER on 8/17/2015.
 */
public class MobilePriscriptionDTO {
    @com.google.gson.annotations.SerializedName("id")
    public String Mid;

    @com.google.gson.annotations.SerializedName("user_Id")
    public String uId;

    @com.google.gson.annotations.SerializedName("recharge_Amount")
    public int recAmnt;

    @com.google.gson.annotations.SerializedName("phno")
    public String pno;

    @com.google.gson.annotations.SerializedName("connection_Name")
    public String Cname;

    @com.google.gson.annotations.SerializedName("status")
    public String stats;
    @com.google.gson.annotations.SerializedName("remark")
    public String remark;
}
