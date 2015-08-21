package in.gravitykerala.aurissample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.microsoft.azure.storage.StorageCredentialsSharedAccessSignature;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.gravitykerala.aurissample.model.*;

/**
 * Created by Prakash on 8/20/2015.
 */
public class UploadFragment extends Fragment {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    //    public static final int MEDIA_TYPE_VIDEO = 2;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "AurisLife";
    PowerManager.WakeLock wakeLock;
    MobileServiceClient mClient;
    private Uri fileUri; // file url to store image/video
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;
    private Button uploadPrescription;
    private MobileServiceTable<MobilePrescription> mPrescriptionsTable;
    private MobileServiceTable<MobilePrescriptionUpload> mPrescriptionUploadTable;

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mClient = LoginActivity.mClient;
        ;

        View rootView = inflater.inflate(R.layout.activity_upload, container, false);
        imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
        btnCapturePicture = (Button) rootView.findViewById(R.id.btnCapturePicture);
        uploadPrescription = (Button) rootView.findViewById(R.id.button_uploadpresc);

        mPrescriptionsTable = mClient.getTable("MobilePrescriptions", MobilePrescription.class);
        mPrescriptionUploadTable = mClient.getTable("MobilePrescriptionUpload", MobilePrescriptionUpload.class);

        uploadPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity(),
                    R.string.camera_not_support,
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            // finish();
        }

        Button mobpricss = (Button) rootView.findViewById(R.id.button4);
        mobpricss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PriscriptionDetails.class);
                startActivity(i);
            }
        });
        return rootView;
    }

    public void submit(String Prescriptionid) {


        List<Pair<String, String>> parameters = new AbstractList<Pair<String, String>>() {
            @Override
            public Pair<String, String> get(int i) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }
        };

        mClient.invokeApi("UpdateStatusPrescription?PrescriptionId=" + Prescriptionid, String.class, new ApiOperationCallback<String>() {
            @Override
            public void onCompleted(String result, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void addItem() {

        // Create a new item
        final MobilePrescription mobilePrescription = new MobilePrescription();

        mobilePrescription.setEventDate(new Date());


        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    wakelockAcquire();
                    final MobilePrescription resultPrescription = mPrescriptionsTable.insert(mobilePrescription).get();
                    Log.d("InsertedItem:", resultPrescription.getId());

                    if (resultPrescription.getId() != null) //Prescription craeted
                    {
                        final MobilePrescriptionUpload mobilePrescriptionUpload = new MobilePrescriptionUpload();
                        mobilePrescriptionUpload.setPrescriptionId(resultPrescription.getId());
                        mobilePrescriptionUpload.setContainerName("prescriptionimages");

                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String imageName = df.format(new Date());

                        mobilePrescriptionUpload.setResourceName(imageName + ".jpg");

                        MobilePrescriptionUpload resultImageUpload = mPrescriptionUploadTable.insert(mobilePrescriptionUpload).get();

                        if ((resultImageUpload.getSasQueryString() != null) && (!resultImageUpload.getSasQueryString().isEmpty())) {
                            Boolean uploadSuccess = uploadFileBlob(fileUri, resultImageUpload.getImageUri(), resultImageUpload.getSasQueryString(), resultImageUpload.getContainerName(), resultImageUpload.getResourceName());
                            if (uploadSuccess) {
                                Log.d("UploadStatus:", "Upload Success");
                                submit(resultPrescription.getId());

                            } else {
                                throw new Exception("CustomExceptionAndroid: Blob ploading Failed");
                            }
                        } else {
                            throw new Exception("CustomExceptionAndroid: blob data not inserted in Database; Did not recieve SecureAccessSignatureToken");
                        }

                    } else {
                        throw new Exception("CustomExceptionAndroid: Prescription not inserted in Database");
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Put UI operation Here When uploading successfull
                        }
                    });
                    wakelockRelease();
                } catch (final Exception e) {
                    e.printStackTrace();
//                    createAndShowDialogFromTask(e, "Error");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Put UI operation Here When uploading Failed
                        }
                    });
                    wakelockRelease();
                }

                return null;
            }
        };

//        getActivity().runAsyncTask(task);
//        task.execute();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void wakelockAcquire() {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ImageUploadWakelock");
        }
        wakeLock.acquire();
    }

    private void wakelockRelease() {
        if (wakeLock != null)
            wakeLock.release();
    }

    private boolean uploadFileBlob(Uri fileURI, String blobURL, String sharedAccessSignatureToken, String containerName, String resourceName) {
        try {
            StorageCredentialsSharedAccessSignature cred = new StorageCredentialsSharedAccessSignature(sharedAccessSignatureToken);
            URI imageURI = new URI(blobURL);

            URI containerUri = new URI("https://" + imageURI.getHost() + "/" + containerName);
//            Log.d("ContainerURI:", containerUri.toString());

            CloudBlobContainer container = new CloudBlobContainer(containerUri, cred);
//            Log.d("ContainerDetails:", container.getUri().toString());

            CloudBlockBlob blobFromSASCredential = container.getBlockBlobReference(resourceName);
            InputStream is = null;
            try {
                is = getActivity().getContentResolver().openInputStream(fileURI);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("Blob Upload", "Upload Starting");
            blobFromSASCredential.upload(is, -1);
//              blobFromSASCredential.uploadText("Sample text");
            Log.d("Blob Upload", "Done:" + blobURL);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    // @Override
//        protected void onRestoreInstanceState(Bundle savedInstanceState) {
//            super.onRestoreInstanceState(savedInstanceState);
//
//            // get the file url
//            fileUri = savedInstanceState.getParcelable("file_uri");
//        }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(),
                        R.string.user_cancelled, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void previewCapturedImage() {
        try {
            // hide video preview
//            videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        Log.d("URI type", "" + type);
        File file = getOutputMediaFile(type);
        return Uri.fromFile(file);
    }

}
