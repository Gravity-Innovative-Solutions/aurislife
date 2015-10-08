package in.gravitykerala.aurislife;

import com.google.gson.annotations.SerializedName;

import in.gravitykerala.aurislife.model.MobileHealthRecordDocument;

/**
 * Created by USER on 9/25/2015.
 */
public class MobileHealthRecordCustom {
    @SerializedName("id")
    public String id;

    public class elements {
        @SerializedName("recordType")
        public String rectype;
        @SerializedName("id")
        public String id;

        @SerializedName("recordComplete")
        public Boolean recordComplete;
        @SerializedName("recordTitle")
        public String recordTitle;
        @SerializedName("recordDescription")
        public String recordDescription;
        @SerializedName("doctorName")
        public String doctorName;
        @SerializedName("healthRecordDocuments")
        public MobileHealthRecordDocument[] healthRecordDocuments;

    }
}
