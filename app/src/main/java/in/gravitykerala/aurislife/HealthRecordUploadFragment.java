package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.gravitykerala.aurislife.foregroundservice.HealthRecordService;
import in.gravitykerala.aurislife.model.BlobUploadDetails;
import in.gravitykerala.aurislife.model.MobileHealthRecord;
import in.gravitykerala.aurislife.model.MobileHealthRecordDocument;

/**
 * Created by Prakash on 8/20/2015.
 */
public class HealthRecordUploadFragment extends Fragment {
    //    public static final int MEDIA_TYPE_IMAGE = 1;
//    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int FILE_CHOOSER_REQUEST_CODE = 150;
    //    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    //    public static final int MEDIA_TYPE_VIDEO = 2;
    // directory name to store captured images and videos
//    private static final String IMAGE_DIRECTORY_NAME = "AurisLife";
    PowerManager.WakeLock wakeLock;
    //    MobileServiceClient mClient;
    LinearLayout layoutRecordDetails;
    EditText et_recordTitle, et_recordDescription, et_doctorName;
    private Uri pickedFileUri; // file url to store image/video
    //    private ImageView imgPreview;
//    private VideoView videoPreview;
    private Button btnPickFile;
    private Button btnUploadDocument;
    private MobileServiceTable<MobileHealthRecord> mHealthRecord;
    private MobileServiceTable<MobileHealthRecordDocument> mHealthRecordDocument;
    private ScrollView scrollView_upload;
    private ProgressBar progressBar_upload;
    //    private boolean imageTaken = false;
    private boolean filePicked = false;

    /*
     * returning image / video
     */
//    private static File getOutputMediaFile(int type) {
//
//        // External sdcard location
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                IMAGE_DIRECTORY_NAME);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
//                        + IMAGE_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
////        } else if (type == MEDIA_TYPE_VIDEO) {
////            mediaFile = new File(mediaStorageDir.getPath() + File.separator
////                    + "VID_" + timeStamp + ".mp4");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SplashPage.initializeMclient(getActivity());
        // SplashPage.Storetok(getActivity());
//        mClient = SplashPage.mClient;

        View rootView = inflater.inflate(R.layout.health_record_upload, container, false);
//        imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
        btnPickFile = (Button) rootView.findViewById(R.id.btnCapturePicture);
        btnUploadDocument = (Button) rootView.findViewById(R.id.button_uploadpresc);
        btnUploadDocument.setVisibility(Button.GONE);
        layoutRecordDetails = (LinearLayout) rootView.findViewById(R.id.layout_recordDetails);
        et_recordTitle = (EditText) rootView.findViewById(R.id.editText_title);
        et_recordDescription = (EditText) rootView.findViewById(R.id.editText_description);
        et_doctorName = (EditText) rootView.findViewById(R.id.editText_doctorName);
        scrollView_upload = (ScrollView) rootView.findViewById(R.id.scrollView_upload);
        progressBar_upload = (ProgressBar) rootView.findViewById(R.id.progressBar_upload);
        mHealthRecord = SplashPage.mClient.getTable("MobileHealthRecord", MobileHealthRecord.class);
        mHealthRecordDocument = SplashPage.mClient.getTable("MobileHealthRecordDocument", MobileHealthRecordDocument.class);

        btnUploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filePicked) {
                    Toast.makeText(getActivity(), (getString(R.string.initializing_file_upload)), Toast.LENGTH_LONG).show();
                    disableUI();
                    addItem();
                } else {
                    Toast.makeText(getActivity(), (getString(R.string.choose_document_first)), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnPickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                chooseFile();
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
                //TODO
                // Toast.makeText(getActivity(), "Work in progress", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), HealthRecordDetailView.class);
                startActivity(i);
            }
        });
        return rootView;
    }

    private void disableUI() {
        progressBar_upload.setVisibility(ProgressBar.VISIBLE);
        scrollView_upload.setVisibility(ScrollView.GONE);
    }

    private void enableUI() {
        scrollView_upload.setVisibility(ScrollView.VISIBLE);
        progressBar_upload.setVisibility(ProgressBar.GONE);
    }

    public void submit(String prescriptionid) {


        List<Pair<String, String>> parameters = new ArrayList<>();
        parameters.add(new Pair<>("PrescriptionId", prescriptionid));
        ListenableFuture<String> result = SplashPage.mClient.invokeApi("UpdateStatusHealthRecordDocument", null, "POST", parameters, String.class);
        Futures.addCallback(result, new FutureCallback<String>() {
            @Override
            public void onFailure(Throwable exc) {
                exc.printStackTrace();
                //RequestFailed
                Toast.makeText(getActivity(), getString(R.string.upld_failed), Toast.LENGTH_LONG).show();
                Log.d("UpdateStatusPresc:", getString(R.string.upld_failed));
                Log.d("UpdateStatusPresc:", getString(R.string.req_err));
            }

            @Override
            public void onSuccess(String result) {
                //RequestSuccess
                Toast.makeText(getActivity(), getString(R.string.prescrptn) + result, Toast.LENGTH_LONG).show();
                Log.d("UpdateStatusPresc:", result);
            }
        });

//        mClient.invokeApi("UpdateStatusPrescription?PrescriptionId=" + prescriptionid, String.class, new ApiOperationCallback<String>() {
//            @Override
//            public void onCompleted(String result, Exception exception, ServiceFilterResponse response) {
//                if (exception == null) {
//
//                    //RequestSuccess
//                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//                } else {
//                    //RequestFailed
//                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//                    Log.d("UpdateStatusPresc:", result);
//                    exception.printStackTrace();
//                    Log.d("UpdateStatusPresc:", "Request Error");
//                }
//
//
//            }
//        });
    }

    private void addItem() {

        // Create a new item
        final MobileHealthRecord mobileHealthRecord = new MobileHealthRecord();

//        mobilePrescription.setEventDate(new Date());
        mobileHealthRecord.setDoctorName(et_doctorName.getText().toString());
        mobileHealthRecord.setRecordTitle(et_recordTitle.getText().toString());
        mobileHealthRecord.setRecordDescription(et_recordDescription.getText().toString());


        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    wakelockAcquire();
                    final MobileHealthRecord resultHealthRecord = mHealthRecord.insert(mobileHealthRecord).get();
                    Log.d("InsertedItem:", resultHealthRecord.getId());

                    if (resultHealthRecord.getId() != null) //Prescription craeted
                    {
                        final MobileHealthRecordDocument mobileHealthRecordDocument = new MobileHealthRecordDocument();
                        mobileHealthRecordDocument.setHealthRecordId(resultHealthRecord.getId());
                        mobileHealthRecordDocument.setContainerName("HealthRecords");

                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String imageName = df.format(new Date());

                        mobileHealthRecordDocument.setResourceName(imageName + ".jpg");

                        MobileHealthRecordDocument resultHealthRecordDocument = mHealthRecordDocument.insert(mobileHealthRecordDocument).get();

                        if ((resultHealthRecordDocument.getSasQueryString() != null) && (!resultHealthRecordDocument.getSasQueryString().isEmpty())) {

                            BlobUploadDetails imageUpload = new BlobUploadDetails();
                            imageUpload.id = resultHealthRecordDocument.getId();
                            imageUpload.fileURI = pickedFileUri;
                            imageUpload.blobURL = resultHealthRecordDocument.getImageUri();
                            imageUpload.sharedAccessSignatureToken = resultHealthRecordDocument.getSasQueryString();
                            imageUpload.containerName = resultHealthRecordDocument.getContainerName();
                            imageUpload.resourceName = resultHealthRecordDocument.getResourceName();

                            Intent forgroundService = new Intent(getActivity(), HealthRecordService.class);
                            HealthRecordService.imageUploaddata = imageUpload;
//                            HealthRecordService.mClient = mClient;
                            HealthRecordService.prescriptionId = resultHealthRecord.getId();
                            getActivity().startService(forgroundService);

//                            Boolean uploadSuccess = uploadFileBlob(fileUri, resultImageUpload.getImageUri(), resultImageUpload.getSasQueryString(), resultImageUpload.getContainerName(), resultImageUpload.getResourceName());
//                            if (uploadSuccess) {
//                                Log.d("UploadStatus:", "Upload Success");
//                                submit(resultPrescription.getId());
//
//                            } else {
//                                throw new Exception("CustomExceptionAndroid: Blob uploading Failed");
//                            }
                        } else {
                            throw new Exception("CustomExceptionAndroid: blob data not inserted in Database; Did not recieve SecureAccessSignatureToken");
                        }

                    } else {
                        throw new Exception("CustomExceptionAndroid: Prescription not inserted in Database");
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Put UI operation Here When uploading service start is successfull
                            Toast.makeText(getActivity(), getString(R.string.uplding_prgrs), Toast.LENGTH_LONG).show();
                            enableUI();
                        }
                    });
                    wakelockRelease();
                } catch (final Exception e) {
                    e.printStackTrace();
//                    createAndShowDialogFromTask(e, "Error");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Put UI operation Here When uploading service start Failed
                            enableUI();
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

//    private boolean uploadFileBlob(Uri fileURI, String blobURL, String sharedAccessSignatureToken, String containerName, String resourceName) {
//        try {
//            StorageCredentialsSharedAccessSignature cred = new StorageCredentialsSharedAccessSignature(sharedAccessSignatureToken);
//            URI imageURI = new URI(blobURL);
//
//            URI containerUri = new URI("https://" + imageURI.getHost() + "/" + containerName);
////            Log.d("ContainerURI:", containerUri.toString());
//
//            CloudBlobContainer container = new CloudBlobContainer(containerUri, cred);
////            Log.d("ContainerDetails:", container.getUri().toString());
//
//            CloudBlockBlob blobFromSASCredential = container.getBlockBlobReference(resourceName);
//            InputStream is = null;
//            try {
//                is = getActivity().getContentResolver().openInputStream(fileURI);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            Log.d("Blob Upload", getString(R.string.upld_starting));
//            blobFromSASCredential.upload(is, -1);
////              blobFromSASCredential.uploadText("Sample text");
//            Log.d("Blob Upload", "Done:" + blobURL);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private void chooseFile() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("file/*"); // intent type to filter application based on your requirement
        startActivityForResult(fileIntent, FILE_CHOOSER_REQUEST_CODE);
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
        outState.putParcelable("file_uri", pickedFileUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // successfully captured the image
                // display it in image view
//                previewCapturedImage();

                //TODO Enable upload button
                pickedFileUri = data.getData();
                filePicked = true;
                layoutRecordDetails.setVisibility(LinearLayout.VISIBLE);
                btnUploadDocument.setVisibility(Button.VISIBLE);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(),
                        R.string.user_cancelled_file_picker, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                        getString(R.string.capture_failed), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

//    private void previewCapturedImage() {
//        try {
//            // hide video preview
////            videoPreview.setVisibility(View.GONE);
//
//            imgPreview.setVisibility(View.VISIBLE);
//
//            // bimatp factory
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            // downsizing image as it throws OutOfMemory Exception for larger
//            // images
//            options.inSampleSize = 8;
//
//            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//                    options);
//
//            imgPreview.setImageBitmap(bitmap);
//            imageTaken = true;
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }

//    public Uri getOutputMediaFileUri(int type) {
//        Log.d("URI type", "" + type);
//        File file = getOutputMediaFile(type);
//        return Uri.fromFile(file);
//    }

}
