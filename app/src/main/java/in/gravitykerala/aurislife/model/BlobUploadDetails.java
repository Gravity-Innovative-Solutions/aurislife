package in.gravitykerala.aurislife.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Prakash on 8/23/2015.
 */
public class BlobUploadDetails {

    public Uri fileURI;
    public String blobURL;
    public String sharedAccessSignatureToken;
    public String containerName;
    public String resourceName;

}
