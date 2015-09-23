package in.gravitykerala.aurislife.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prakash on 9/22/2015.
 */
public class MobileHealthRecordDocument {

    /**
     * {
     * "healthRecordId": "sample string 1",
     * "uploadComplete": true,
     * "containerName": "sample string 3",
     * "resourceName": "sample string 4",
     * "sasQueryString": "sample string 5",
     * "imageUri": "sample string 6",
     * "uid": "sample string 7",
     * "id": "sample string 8",
     * "__version": "QEBA",
     * "__createdAt": "2015-09-22T10:50:11.911Z",
     * "__updatedAt": "2015-09-22T10:50:11.911Z",
     * "__deleted": true
     * }
     */

    @SerializedName("healthRecordId")
    private String healthRecordId;
    @SerializedName("uploadComplete")
    private boolean uploadComplete;
    @SerializedName("containerName")
    private String containerName;
    @SerializedName("resourceName")
    private String resourceName;
    @SerializedName("sasQueryString")
    private String sasQueryString;
    @SerializedName("imageUri")
    private String imageUri;
    @SerializedName("uid")
    private String uid;
    @SerializedName("id")
    private String id;

    public String getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(String healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    public boolean getUploadComplete() {
        return uploadComplete;
    }

    public void setUploadComplete(boolean uploadComplete) {
        this.uploadComplete = uploadComplete;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getSasQueryString() {
        return sasQueryString;
    }

    public void setSasQueryString(String sasQueryString) {
        this.sasQueryString = sasQueryString;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
