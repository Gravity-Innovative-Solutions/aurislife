package in.gravitykerala.aurislife;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.aurislife.NoCompulsaryUpdate;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.net.MalformedURLException;
import java.util.AbstractList;
import java.util.List;

import android.os.Handler;
import android.widget.Toast;

public class SplashPage extends AppCompatActivity {
    public static MobileServiceClient mClient;
    public static Context currentContext;
    private static int SPLASH_TIME_OUT = 2200;
    double versionCode = BuildConfig.VERSION_CODE;

    //  String versionName = BuildConfig.VERSION_NAME;
    public static void initializeMclient(Context context) {

        try {
            
//            SplashPage.mClient = new MobileServiceClient("https://mcettodolist.azure-mobile.net/", "iPwHgDEkiZXWHXyDmqGCBKYRCMBQGg87", context); //Production server
          SplashPage.mClient = new MobileServiceClient("https://gravityaurislife.azure-mobile.net", "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68", context); //Test Server
// String userId = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
//                String tok = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
//                MobileServiceUser user = new MobileServiceUser(userId);
//                user.setAuthenticationToken(tok);
//                mClient.setCurrentUser(user);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO check for Netowrk connectivity and add Exception handling

        }

    }

    public static void Storetok(Context context) {


        String userId = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
        String tok = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
        MobileServiceUser user = new MobileServiceUser(userId);
        user.setAuthenticationToken(tok);
        mClient.setCurrentUser(user);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        currentContext = this;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                initializeMclient(SplashPage.this);

                updationcheck();
                usertok();
                // Toast.makeText(SplashPage.this,""+versionCode+versionName,Toast.LENGTH_SHORT).show();
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void usertok() {

        String userId = PrefUtils.getFromPrefs(SplashPage.this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
            String tok = PrefUtils.getFromPrefs(SplashPage.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
            if (userId.equals("default") && tok.equals("default")) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, FirstPage.class);
                startActivity(i);
            }

    }

    public void updationcheck() {

        {
            final ProgressDialog progressDialog = new ProgressDialog(SplashPage.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.authenticating));
            progressDialog.show();
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
            ListenableFuture<VersionCheck> result = SplashPage.mClient.invokeApi("VersionCheck", VersionCheck.class);

            Futures.addCallback(result, new FutureCallback<VersionCheck>() {
                @Override
                public void onFailure(Throwable exc) {
                    exc.printStackTrace();
                    Log.d("Output", "error");
                    //createAndShowDialog((Exception) exc, "Error");
                }

                @Override
                public void onSuccess(VersionCheck result) {


                    Log.d("ID", result.Vname);

                    Toast.makeText(SplashPage.this, "" + result.Vcode, Toast.LENGTH_SHORT).show();
                    if (1 < result.mVcode) {
                        new CompolsuryUpdate(currentContext);
                    } else if (versionCode < result.Vcode) {
                        new NoCompulsaryUpdate(currentContext);

                    }
                    progressDialog.dismiss();
                }
            });
        }

    }
}
