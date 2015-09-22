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
        @com.google.gson.annotations.SerializedName("type")
        public String type;

        @com.google.gson.annotations.SerializedName("title")
        public String title;
        @com.google.gson.annotations.SerializedName("content")
        public String content;


        public String getId() {
            return mId;
        }
    }

}
