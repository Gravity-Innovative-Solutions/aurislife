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
        public Boolean reccomplt;
        @SerializedName("recordTitle")
        public String rectitle;
        @SerializedName("recordDescription")
        public String recdescription;
        @SerializedName("doctorName")
        public String docname;
        @SerializedName("healthRecordDocuments")
        public MobileHealthRecordDocument[] hlthrecdocumnt;

    }
}
