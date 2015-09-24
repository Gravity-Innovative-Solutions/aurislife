package in.gravitykerala.aurislife;

import java.util.ArrayList;

/**
 * Created by USER on 7/10/2015.
 */
public class MobileNotifications {
    @com.google.gson.annotations.SerializedName("id")
    public String mId;
    ArrayList<Exam> staffs;

    MobileNotifications() {
        staffs = new ArrayList<Exam>();

    }

    public Exam getName(int i) {
        return staffs.get(i);
    }

    public String getId() {
        return mId;
    }

    public class Exam {
        @com.google.gson.annotations.SerializedName("id")
        public String mId;
        @com.google.gson.annotations.SerializedName("title")
        public String title;
        @com.google.gson.annotations.SerializedName("content")
        public String content;
        @com.google.gson.annotations.SerializedName("imageUrl")
        public String img;
        @com.google.gson.annotations.SerializedName("event_Date")
        public String evdate;

        public String getId() {
            return mId;
        }
    }

}
