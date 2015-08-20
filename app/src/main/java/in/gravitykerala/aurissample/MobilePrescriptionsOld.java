package in.gravitykerala.aurissample;

import java.util.Date;

/**
 * Created by USER on 8/17/2015.
 */
public class MobilePrescriptionsOld {
    @com.google.gson.annotations.SerializedName("id")
    public String Mid;

    @com.google.gson.annotations.SerializedName("user_Id")
    public String uId;

    @com.google.gson.annotations.SerializedName("prescription_Number")
    public String Prisno;

    @com.google.gson.annotations.SerializedName("clinic_Name")
    public String clncnam;

    @com.google.gson.annotations.SerializedName("doctor_Name")
    public String drname;

    @com.google.gson.annotations.SerializedName("points")
    public int pts;
    @com.google.gson.annotations.SerializedName("event_Date")
    public Date evdate;

    @com.google.gson.annotations.SerializedName("status")
    public String stats;

    @com.google.gson.annotations.SerializedName("remarks")
    public String remark;
}
