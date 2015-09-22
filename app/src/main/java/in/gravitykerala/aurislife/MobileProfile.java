package in.gravitykerala.aurislife;

/**
 * Created by USER on 8/12/2015.
 */
public class MobileProfile {
    @com.google.gson.annotations.SerializedName("id")
    public String Mid;

    @com.google.gson.annotations.SerializedName("name")
    public String name;

    @com.google.gson.annotations.SerializedName("email")
    public String email;

    @com.google.gson.annotations.SerializedName("phno")
    public String phn;

    @com.google.gson.annotations.SerializedName("balance")
    public int bal;

    @com.google.gson.annotations.SerializedName("varification_Status")
    public Boolean status;

}
