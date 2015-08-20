package in.gravitykerala.aurissample.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Prakash on 8/20/2015.
 */
public class MobilePrescriptions {

    /**
     * prescription_Number : 3
     * doctor_Name : sample string 5
     * user_Id : sample string 1
     * event_Date : 2015-08-20T09:54:53.78Z
     * id : sample string 9
     * remarks : sample string 8
     * status : sample string 2
     * points : 4
     * clinic_Name : sample string 6
     */
    @SerializedName("prescriptionNumber")
    private int prescriptionNumber;
    @SerializedName("doctorName")
    private String doctorName;
    @SerializedName("userId")
    private String userId;
    @SerializedName("eventDate")
    private Date eventDate;
    @SerializedName("id")
    private String id;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("status")
    private String status;
    @SerializedName("points")
    private int points;
    @SerializedName("clinicName")
    private String clinicName;

    public int getPrescriptionNumber() {
        return prescriptionNumber;
    }

    public void setPrescriptionNumber(int prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }
}
