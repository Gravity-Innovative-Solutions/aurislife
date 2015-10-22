package in.gravitykerala.aurislife;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.AbstractList;
import java.util.List;

public class SplashPage extends AppCompatActivity {
    public static final Object mAuthenticationLock = new Object();
    public static final String SHAREDPREFFILE = "temp";

    public static final String USERIDPREF = "uid";
    public static final String TOKENPREF = "tkn";
    public static boolean bAuthenticating = false;
    //    public static MobileServiceClient mClient;
    public static Context currentContext;
    private static int SPLASH_TIME_OUT = 1300;
    double versionCode = BuildConfig.VERSION_CODE;

//    public static void initializeMclient(Context context) {
//
//        if (Azure == null) {
//            try {
//                Azure = new MobileServiceClient(API_URL, API_KEY, context).withFilter(new RefreshTokenCacheFilter());
//
//                // Authenticate passing false to load the current token cache if available.
//                authenticate(false, context);
////                Log.d("PushNotification:", "registering");
////                NotificationsManager.handleNotifications(context, GCM_PUSH_SENDER_ID, PushNotificationHandler.class);
////                Log.d("PushNotification:", "registered");
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                //TODO check for Netowrk connectivity and add Exception handling
//
//            }
//        } else {
//            mClient.setContext(context);
//        }
//    }

//    private static boolean loadUserTokenCache(final Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
//        String userId = prefs.getString(USERIDPREF, "undefined");
//        if (userId == "undefined")
//            return false;
//        String token = prefs.getString(TOKENPREF, "undefined");
//        if (token == "undefined")
//            return false;
//
//        MobileServiceUser user = new MobileServiceUser(userId);
//        user.setAuthenticationToken(token);
//        mClient.setCurrentUser(user);
//        Log.d("Cache_User_UID", mClient.getCurrentUser().getUserId());
//        Log.d("Cache_User_Token", mClient.getCurrentUser().getAuthenticationToken());
//
//        return true;
//    }

//    public static void cacheUserToken(MobileServiceUser user, Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(USERIDPREF, user.getUserId());
//        editor.putString(TOKENPREF, user.getAuthenticationToken());
//        editor.apply();
//    }

    /**
     * Authenticates with the desired login provider. Also caches the token.
     * <p/>
     * If a local token cache is detected, the token cache is used instead of an actual
     * login unless bRefresh is set to true forcing a refresh.
     *
     * @param bForceRefreshCache Indicates whether to force a token refresh.
     */
//    private static void authenticate(boolean bForceRefreshCache, final Context context) {
//
//        bAuthenticating = true;
//
//        if (bForceRefreshCache || !loadUserTokenCache(context)) {
//            // New login using the provider and update the token cache. If login successfull call "cacheUserToken(mClient.getCurrentUser(), context)" to save tokens
//            LoginDialog loginDialog = new LoginDialog(context);
////            mClient.login(MobileServiceAuthenticationProvider.MicrosoftAccount,
////                    new UserAuthenticationCallback() {
////                        @Override
////                        public void onCompleted(MobileServiceUser user,
////                                                Exception exception, ServiceFilterResponse response) {
////
////                            synchronized(mAuthenticationLock)
////                            {
////                                if (exception == null) {
////
////                                    createTable();
////                                } else {
////                                    createAndShowDialog(exception.getMessage(), "Login Error");
////                                }
////                                bAuthenticating = false;
////                                mAuthenticationLock.notifyAll();
////                            }
////                        }
////                    });
//        } else {
//            // Other threads may be blocked waiting to be notified when
//            // authentication is complete.
//            synchronized (mAuthenticationLock) {
//                bAuthenticating = false;
//                mAuthenticationLock.notifyAll();
//            }
//            if (context instanceof SplashPage) {
//                Intent i = new Intent(context, FirstPage.class);
//                context.startActivity(i);
//                ((SplashPage) context).finish();
//            }
//        }
//    }

    /**
     * Detects if authentication is in progress and waits for it to complete.
     * Returns true if authentication was detected as in progress. False otherwise.
     */
//    public static boolean detectAndWaitForAuthentication() {
//        boolean detected = false;
//        synchronized (mAuthenticationLock) {
//            do {
//                if (bAuthenticating == true)
//                    detected = true;
//                try {
//                    mAuthenticationLock.wait(1000);
//                } catch (InterruptedException e) {
//                }
//            }
//            while (bAuthenticating == true);
//        }
//        if (bAuthenticating == true)
//            return true;
//
//        return detected;
//    }

    /**
     * Waits for authentication to complete then adds or updates the token
     * in the X-ZUMO-AUTH request header.
     *
     * @param request The request that receives the updated token.
     */
//    private static void waitAndUpdateRequestToken(ServiceFilterRequest request) {
//        MobileServiceUser user = null;
//        if (detectAndWaitForAuthentication()) {
//            user = mClient.getCurrentUser();
//            if (user != null) {
//                request.removeHeader("X-ZUMO-AUTH");
//                request.addHeader("X-ZUMO-AUTH", user.getAuthenticationToken());
//            }
//        }
//    }

//    public static void clearUserToken(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(USERIDPREF, "undefined");
//        editor.putString(TOKENPREF, "undefined");
//        editor.apply();
//        new LoginDialog(context);
//        //  android.os.Process.killProcess(android.os.Process.myPid());
//        // System.exit(1);
//    }

//    public static void Storetok(Context context) {
//
//
//        String userId = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
//        String tok = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
//        MobileServiceUser user = new MobileServiceUser(userId);
//        user.setAuthenticationToken(tok);
//        mClient.setCurrentUser(user);
//
//    }

    public void updationcheck() {

        {
//            final ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage(getString(R.string.authenticating));
//            progressDialog.show();
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
            ListenableFuture<VersionCheck> result = AzureMobileService.client.invokeApi("VersionCheck", VersionCheck.class);

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

//                    Toast.makeText(SplashPage.this, "" + result.mVcode, Toast.LENGTH_SHORT).show();
                    if (versionCode < result.mVcode) {
                        new CompolsuryUpdate(currentContext);
                    } else if (versionCode < result.Vcode) {
                        new NoCompulsaryUpdate(currentContext);

                    }
                    //progressDialog.dismiss();
                }
            });
        }

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
                AzureMobileService.initialize(SplashPage.this);
//                usertok();
                isOnline();
                updationcheck();
                // close this activity

//                finish();
            }
        }, SPLASH_TIME_OUT);


        // finish();
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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(getBaseContext(), "not connected", Toast.LENGTH_LONG).show();
        }
        return false;
    }
//    public void usertok() {
//
//        String userId = PrefUtils.getFromPrefs(SplashPage.this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
//        String tok = PrefUtils.getFromPrefs(SplashPage.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
//        if (userId.equals("default") && tok.equals("default")) {
////            Intent i = new Intent(this, LoginActivity.class);
/////            startActivity(i);
//        } else {
//            Intent i = new Intent(this, FirstPage.class);
//            startActivity(i);
//        }
//
//    }

    /**
     * The RefreshTokenCacheFilter class filters responses for HTTP status code 401.
     * When 401 is encountered, the filter calls the authenticate method on the
     * UI thread. Out going requests and retries are blocked during authentication.
     * Once authentication is complete, the token cache is updated and
     * any blocked request will receive the X-ZUMO-AUTH header added or updated to
     * that request.
     */
//    private static class RefreshTokenCacheFilter implements ServiceFilter {
//
//        AtomicBoolean mAtomicAuthenticatingFlag = new AtomicBoolean();
////        static Context GlobalContext;
//
////        public RefreshTokenCacheFilter(Context context) {
//////            this.GlobalContext = context;
////        }
//
//        @Override
//        public ListenableFuture<ServiceFilterResponse> handleRequest(
//                final ServiceFilterRequest request,
//                final NextServiceFilterCallback nextServiceFilterCallback
//        ) {
//            // In this example, if authentication is already in progress we block the request
//            // until authentication is complete to avoid unnecessary authentications as
//            // a result of HTTP status code 401.
//            // If authentication was detected, add the token to the request.
//            waitAndUpdateRequestToken(request);
//
//            // Send the request down the filter chain
//            // retrying up to 5 times on 401 response codes.
//            ListenableFuture<ServiceFilterResponse> future = null;
//            ServiceFilterResponse response = null;
//            int responseCode = 401;
//            for (int i = 0; (i < 5) && (responseCode == 401); i++) {
//                future = nextServiceFilterCallback.onNext(request);
//                try {
//                    response = future.get();
//                    responseCode = response.getStatus().getStatusCode();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    if (e.getCause().getClass() == MobileServiceException.class) {
//                        MobileServiceException mEx = (MobileServiceException) e.getCause();
//                        responseCode = mEx.getResponse().getStatus().getStatusCode();
//                        if (responseCode == 401) {
//                            // Two simultaneous requests from independent threads could get HTTP status 401.
//                            // Protecting against that right here so multiple authentication requests are
//                            // not setup to run on the UI thread.
//                            // We only want to authenticate once. Requests should just wait and retry
//                            // with the new token.
//                            Log.d("mClient_Debug", "401 Unauthorized error, refreshing tokens");
//                            if (mAtomicAuthenticatingFlag.compareAndSet(false, true)) {
//                                // Authenticate on UI thread
//                                ((Activity) mClient.getContext()).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // Force a token refresh during authentication.
//                                        authenticate(true, mClient.getContext());
//                                    }
//                                });
//                            }
//
//                            // Wait for authentication to complete then update the token in the request.
//                            waitAndUpdateRequestToken(request);
//                            mAtomicAuthenticatingFlag.set(false);
//                        }
//                    }
//                }
//            }
//            return future;
//        }
//    }
}
