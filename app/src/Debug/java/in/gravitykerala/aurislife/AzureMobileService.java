package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Prakash on 9/26/2015.
 */
public class AzureMobileService {
    public static final Object mAuthenticationLock = new Object();
    public static final String SHARED_PREF_KEY = "ZUMO";
    public static final String USERID_PREF = "USER_ID";
    public static final String TOKEN_PREF = "TOKEN";

    public static final String DEBUG_API_URL = "https://aurisbackup.azure-mobile.net/";
    public static final String DEBUG_API_KEY = "ZybfZmcYlbhGSFFMeVGSXavrmRBLOY96";
    public static final String RELEASE_API_URL = "https://gravityaurislife.azure-mobile.net/";
    public static final String RELEASE_API_KEY = "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68";

    public static String API_URL = RELEASE_API_URL;
    public static String API_KEY = RELEASE_API_KEY;

    public static boolean bAuthenticating = false;
    public static MobileServiceClient client;

    private static void useDebugServer() {
        API_URL = DEBUG_API_URL;
        API_KEY = DEBUG_API_KEY;
    }

    //Modify this function accordingly to set up the startup page to load after splash page
    private static void nextPageIfOnSplash(Context context) {
        if (context instanceof SplashPage) {    //Check if the given context is Splash activity
            Intent i = new Intent(context, FirstPage.class);   //Set the next page(Home activity) to load after splash page
            context.startActivity(i);
            ((SplashPage) context).finish();    //Set Splash activity here
        }
    }

    public static void initialize(Context context) {
        if (client == null) {
            try {
                useDebugServer();
                client = new MobileServiceClient(API_URL, API_KEY, context).withFilter(new RefreshTokenCacheFilter());

                // Authenticate passing false to load the current token cache if available.
                authenticate(false, context);
//                Log.d("PushNotification:", "registering");
//                NotificationsManager.handleNotifications(context, GCM_PUSH_SENDER_ID, PushNotificationHandler.class);
//                Log.d("PushNotification:", "registered");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //TODO check for Netowrk connectivity and add Exception handling
            }
        } else {
            client.setContext(context);
            //Start the next activity if the context came from splash activity
            nextPageIfOnSplash(context);
        }
    }

    private static boolean loadUserTokenCache(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String userId = prefs.getString(USERID_PREF, "undefined");
        if (userId == "undefined")
            return false;
        String token = prefs.getString(TOKEN_PREF, "undefined");
        if (token == "undefined")
            return false;

        MobileServiceUser user = new MobileServiceUser(userId);
        user.setAuthenticationToken(token);
        client.setCurrentUser(user);
        Log.d("Cache_User_UID", client.getCurrentUser().getUserId());
        Log.d("Cache_User_Token", client.getCurrentUser().getAuthenticationToken());

        return true;
    }

    public static void cacheUserToken(MobileServiceUser user, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERID_PREF, user.getUserId());
        editor.putString(TOKEN_PREF, user.getAuthenticationToken());
        editor.apply();
    }

    /**
     * Authenticates with the desired login provider. Also caches the token.
     * <p/>
     * If a local token cache is detected, the token cache is used instead of an actual
     * login unless bRefresh is set to true forcing a refresh.
     *
     * @param bForceRefreshCache Indicates whether to force a token refresh.
     */
    private static void authenticate(boolean bForceRefreshCache, final Context context) {

        bAuthenticating = true;

        if (bForceRefreshCache || !loadUserTokenCache(context)) {
            // New login using the provider and update the token cache. If login successfull call "cacheUserToken(mClient.getCurrentUser(), context)" to save tokens
            LoginDialog loginDialog = new LoginDialog(context);
        } else {
            // Other threads may be blocked waiting to be notified when
            // authentication is complete.
            synchronized (mAuthenticationLock) {
                bAuthenticating = false;
                mAuthenticationLock.notifyAll();
            }
            //Start the next activity if the context came from splash activity
            nextPageIfOnSplash(context);
        }
    }

    /**
     * Detects if authentication is in progress and waits for it to complete.
     * Returns true if authentication was detected as in progress. False otherwise.
     */
    public static boolean detectAndWaitForAuthentication() {
        boolean detected = false;
        synchronized (mAuthenticationLock) {
            do {
                if (bAuthenticating == true)
                    detected = true;
                try {
                    mAuthenticationLock.wait(1000);
                } catch (InterruptedException e) {
                }
            }
            while (bAuthenticating == true);
        }
        if (bAuthenticating == true)
            return true;

        return detected;
    }

    /**
     * Waits for authentication to complete then adds or updates the token
     * in the X-ZUMO-AUTH request header.
     *
     * @param request The request that receives the updated token.
     */
    private static void waitAndUpdateRequestToken(ServiceFilterRequest request) {
        MobileServiceUser user;
        if (detectAndWaitForAuthentication()) {
            user = client.getCurrentUser();
            if (user != null) {
                request.removeHeader("X-ZUMO-AUTH");
                request.addHeader("X-ZUMO-AUTH", user.getAuthenticationToken());
            }
        }
    }

    public static void clearUserToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERID_PREF, "undefined");
        editor.putString(TOKEN_PREF, "undefined");
        editor.apply();
        new LoginDialog(context);
    }


    /**
     * The RefreshTokenCacheFilter class filters responses for HTTP status code 401.
     * When 401 is encountered, the filter calls the authenticate method on the
     * UI thread. Out going requests and retries are blocked during authentication.
     * Once authentication is complete, the token cache is updated and
     * any blocked request will receive the X-ZUMO-AUTH header added or updated to
     * that request.
     */
    private static class RefreshTokenCacheFilter implements ServiceFilter {

        AtomicBoolean mAtomicAuthenticatingFlag = new AtomicBoolean();
//        static Context GlobalContext;

//        public RefreshTokenCacheFilter(Context context) {
////            this.GlobalContext = context;
//        }

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(
                final ServiceFilterRequest request,
                final NextServiceFilterCallback nextServiceFilterCallback
        ) {
            // In this example, if authentication is already in progress we block the request
            // until authentication is complete to avoid unnecessary authentications as
            // a result of HTTP status code 401.
            // If authentication was detected, add the token to the request.
            waitAndUpdateRequestToken(request);

            // Send the request down the filter chain
            // retrying up to 5 times on 401 response codes.
            ListenableFuture<ServiceFilterResponse> future = null;
            ServiceFilterResponse response = null;
            int responseCode = 401;
            for (int i = 0; (i < 5) && (responseCode == 401); i++) {
                future = nextServiceFilterCallback.onNext(request);
                try {
                    response = future.get();
                    responseCode = response.getStatus().getStatusCode();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    if (e.getCause().getClass() == MobileServiceException.class) {
                        MobileServiceException mEx = (MobileServiceException) e.getCause();
                        responseCode = mEx.getResponse().getStatus().getStatusCode();
                        if (responseCode == 401) {
                            // Two simultaneous requests from independent threads could get HTTP status 401.
                            // Protecting against that right here so multiple authentication requests are
                            // not setup to run on the UI thread.
                            // We only want to authenticate once. Requests should just wait and retry
                            // with the new token.
                            Log.d("mClient_Debug", "401 Unauthorized error, refreshing tokens");
                            if (mAtomicAuthenticatingFlag.compareAndSet(false, true)) {
                                // Authenticate on UI thread
                                ((Activity) client.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Force a token refresh during authentication.
                                        authenticate(true, client.getContext());
                                    }
                                });
                            }

                            // Wait for authentication to complete then update the token in the request.
                            waitAndUpdateRequestToken(request);
                            mAtomicAuthenticatingFlag.set(false);
                        }
                    }
                }
            }
            return future;
        }
    }

}
