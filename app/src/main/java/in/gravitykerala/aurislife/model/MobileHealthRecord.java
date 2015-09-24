package in.gravitykerala.aurislife.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prakash on 9/22/2015.
 */
public class MobileHealthRecord {

    /**
     * {
     * "userId": "sample string 1",
     * "recordType": "sample string 2",
     * "recordComplete": true,
     * "recordTitle": "sample string 4",
     * "recordDescription": "sample string 5",
     * "doctorName": "sample string 6",
     * "doctorId": "sample string 7",
     * "id": "sample string 8",
     * "__version": "QEBA",
     * "__createdAt": "2015-09-22T10:51:56.331Z",
     * "__updatedAt": "2015-09-22T10:51:56.331Z",
     * "__deleted": true
     * }
     */

    @SerializedName("userId")
    private String userId;
    @SerializedName("recordType")
    private String recordType;
    @SerializedName("recordComplete")
    private boolean recordComplete;
    @SerializedName("recordTitle")
    private String recordTitle;
    @SerializedName("recordDescription")
    private String recordDescription;
    @SerializedName("doctorName")
    private String doctorName;
    @SerializedName("doctorId")
    private String doctorId;
    @SerializedName("id")
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public boolean getRecordComplete() {
        return recordComplete;
    }

    public void setRecordComplete(boolean recordComplete) {
        this.recordComplete = recordComplete;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getRecordDescription() {
        return recordDescription;
    }

    public void setRecordDescription(String recordDescription) {
        this.recordDescription = recordDescription;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
