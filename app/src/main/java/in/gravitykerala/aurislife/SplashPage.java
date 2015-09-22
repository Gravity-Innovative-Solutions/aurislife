package in.gravitykerala.aurislife;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.net.MalformedURLException;

import android.os.Handler;

public class SplashPage extends AppCompatActivity {
    public static MobileServiceClient mClient;
    private static int SPLASH_TIME_OUT = 2500;

    public static void initializeMclient(Context context) {

        try {
            SplashPage.mClient = new MobileServiceClient("https://aurisbackup.azure-mobile.net/", "ZybfZmcYlbhGSFFMeVGSXavrmRBLOY96", context);
//            SplashPage.mClient = new MobileServiceClient("https://gravityaurislife.azure-mobile.net", "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68", context);

//                String userId = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
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
                usertok();
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


        // finish();
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
}
