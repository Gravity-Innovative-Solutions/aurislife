package in.gravitykerala.aurislife.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prakash on 8/20/2015.
 */
public class MobilePrescriptionUpload {

    /**
     * imageUri : sample string 5
     * upload_Status : sample string 6
     * containerName : sample string 2
     * sasQueryString : sample string 4
     * resourceName : sample string 3
     * prescription_Id : sample string 1
     * id : sample string 7
     */
    @SerializedName("imageUri")
    private String imageUri;
    @SerializedName("uploadStatus")
    private String uploadStatus;
    @SerializedName("containerName")
    private String containerName;
    @SerializedName("sasQueryString")
    private String sasQueryString;
    @SerializedName("resourceName")
    private String resourceName;
    @SerializedName("prescriptionId")
    private String prescriptionId;
    @SerializedName("id")
    private String id;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getSasQueryString() {
        return sasQueryString;
    }

    public void setSasQueryString(String sasQueryString) {
        this.sasQueryString = sasQueryString;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
