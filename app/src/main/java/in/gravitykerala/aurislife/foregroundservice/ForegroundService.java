package in.gravitykerala.aurislife.foregroundservice;

/**
 * Created by Prakash on 8/22/2015.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.azure.storage.StorageCredentialsSharedAccessSignature;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import in.gravitykerala.aurislife.AzureMobileService;
import in.gravitykerala.aurislife.FirstPage;
import in.gravitykerala.aurislife.R;
import in.gravitykerala.aurislife.model.BlobUploadDetails;

public class ForegroundService extends Service {
    private static final String LOG_TAG = "ForegroundService";
    public static BlobUploadDetails imageUploaddata;
    //    public static MobileServiceClient mClient;
    public static String prescriptionId;
    PowerManager.WakeLock wakeLock;
    NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(LOG_TAG, "Received Start Foreground Intent ");
        AzureMobileService.initialize(this);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, FirstPage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo_auris);


        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.presc_upld))
                .setTicker(getString(R.string.uplding_prgrs))
                .setContentText(getString(R.string.uplding_prgrs))
                .setSmallIcon(R.drawable.icon_app_notification)
                .setProgress(0, 0, true)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();

//        startForeground
//        mNotificationManager.notify
        startForeground(Constants.NOTIFICATION_ID.SERVICE_FOREGROUND,
                notification);


        Log.d(LOG_TAG, "RecievedUploadData: " + imageUploaddata.blobURL);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                wakelockAcquire();
                Boolean uploadSuccess = uploadFileBlob(imageUploaddata.fileURI, imageUploaddata.blobURL, imageUploaddata.sharedAccessSignatureToken, imageUploaddata.containerName, imageUploaddata.resourceName);
                if (uploadSuccess) {
                    Log.d("UploadStatus:", "Upload Success");
                    submit(prescriptionId);

                } else {
//            throw new Exception("CustomExceptionAndroid: Blob uploading Failed");
                    Intent notificationIntent = new Intent(ForegroundService.this, FirstPage.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ForegroundService.this, 0,
                            notificationIntent, 0);
                    Notification notificationFailed = new NotificationCompat.Builder(ForegroundService.this)
                            .setContentTitle(getString(R.string.presc_upld))
                            .setTicker(getString(R.string.pres_upld_failed))
                            .setContentText(getString(R.string.pres_upld_failed_bcoz_nw_issue))
                            .setSmallIcon(R.drawable.icon_app_notification)
                            .setContentIntent(pendingIntent)
                            .setOngoing(false).build();
                    stopForeground(true);
                    mNotificationManager.notify(Constants.NOTIFICATION_ID.SERVICE_RESULT, notificationFailed);
                    wakelockRelease();
                    stopSelf();
                }
                return null;
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    {
//            Log.i(LOG_TAG, "Received Stop Foreground Intent");
//            stopForeground(true);
//            stopSelf();
//        }
        return START_NOT_STICKY;
    }

    private void wakelockAcquire() {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ImageUploadWakelock");
        }
        wakeLock.acquire();
    }

    private void wakelockRelease() {
        if (wakeLock != null)
            wakeLock.release();
    }

    public void submit(String prescriptionid) {

        Log.d("UpdateStatusPresc:", "PrescriptionID" + prescriptionid);
        List<Pair<String, String>> parameters = new ArrayList<>();
        parameters.add(new Pair<>("PrescriptionId", prescriptionid));
        ListenableFuture<String> result = AzureMobileService.client.invokeApi("UpdateStatusPrescription", null, "POST", parameters, String.class);
        Futures.addCallback(result, new FutureCallback<String>() {
            @Override
            public void onFailure(Throwable exc) {
                Intent notificationIntent = new Intent(ForegroundService.this, FirstPage.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(ForegroundService.this, 0,
                        notificationIntent, 0);
                Notification notificationFailed = new NotificationCompat.Builder(ForegroundService.this)
                        .setContentTitle(getString(R.string.presc_upld))
                        .setTicker(getString(R.string.pres_upld_failed))
                        .setContentText(getString(R.string.pres_upld_failed_bcoz_nw_issue))
                        .setSmallIcon(R.drawable.icon_app_notification)
                        .setContentIntent(pendingIntent)
                        .setOngoing(false).build();
                stopForeground(true);
                mNotificationManager.notify(Constants.NOTIFICATION_ID.SERVICE_RESULT,
                        notificationFailed);
                exc.printStackTrace();
                //RequestFailed
                Toast.makeText(ForegroundService.this, "Uploading failed", Toast.LENGTH_LONG).show();
                Log.d("UpdateStatusPresc:", "Failed; Updating prescription status");
//                Log.d("UpdateStatusPresc:", getString(R.string.req_err));
                wakelockRelease();
                stopSelf();
            }

            @Override
            public void onSuccess(String result) {
                //RequestSuccess

                Intent notificationIntent = new Intent(ForegroundService.this, FirstPage.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ForegroundService.this, 0,
                        notificationIntent, 0);
                Notification notificationFailed = new NotificationCompat.Builder(ForegroundService.this)
                        .setContentTitle(getString(R.string.presc_upld))
                        .setTicker(getString(R.string.pres_upld_success))
                        .setContentText(getString(R.string.upld_finished))
                        .setSmallIcon(R.drawable.icon_app_notification)
                        .setContentIntent(pendingIntent)
                        .setOngoing(false).build();

                mNotificationManager.notify(Constants.NOTIFICATION_ID.SERVICE_RESULT,
                        notificationFailed);
                stopForeground(true);

                Toast.makeText(ForegroundService.this, getString(R.string.prescrptn) + result, Toast.LENGTH_LONG).show();
                Log.d("UpdateStatusPresc:", result);
                wakelockRelease();

                stopSelf();
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

            is = this.getContentResolver().openInputStream(fileURI);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

}
