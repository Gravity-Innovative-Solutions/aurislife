package in.gravitykerala.aurislife;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import in.gravitykerala.aurislife.model.MobileHealthRecordDocument;
import in.gravitykerala.aurislife.model.MobilePrescriptionUpload;

/**
 * Created by USER on 10/12/2015.
 */
public class MobilePriscriptionCustom {
    @SerializedName("id")
    public String id;

    public class pelements {
        @SerializedName("prescription_Number")
        public int prescriptionNumber;
        @SerializedName("doctor_Name")
        public String doctorName;
        @SerializedName("user_Id")
        public String userId;
        @SerializedName("event_Date")
        public Date eventDate;
        @SerializedName("id")
        public String id;
        @SerializedName("remarks")
        public String remarks;
        @SerializedName("status")
        public String status;
        @SerializedName("points")
        public int points;
        @SerializedName("clinic_Name")
        public String clinicName;
        @SerializedName("images")
        public MobilePrescriptionUpload[] imagess;

    }
}
