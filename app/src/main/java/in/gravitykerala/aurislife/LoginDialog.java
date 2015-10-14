package in.gravitykerala.aurislife;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

/**
 * TODO: document your custom view class.
 */
public class LoginDialog extends Dialog {
    private static final int REQUEST_SIGNUP = 0;
    EditText email, password;
    Button login;
    LinearLayout interactiveLayout;
    ProgressBar progressBar;
    TextView _signupLink;
    Button _forgetPword;
    ScrollView loginSV;
    EditText _passwordText;
    EditText _phNo;
    Button _loginButton;
    //    String username, password;
//    String KEY_username = "username"; 69992133
//    String KEY_password = "password";
//    SharedPreferences prefs;
    LinearLayout l1;
    CoordinatorLayout coordinatorLayout;
    View.OnClickListener mOnClickListener = null;
//    @InjectView(R.id.input_email)
//    EditText _phNo;
//    @InjectView(R.id.input_password)
//    EditText _passwordText;
//    @InjectView(R.id.btn_login)
//    Button _loginButton;
//    @InjectView(R.id.link_signup2)
//    TextView _signupLink1;
//    @InjectView(R.id.button7)
//    Button _forgetPword;


    public LoginDialog(final Context context) {
        super(context);
        Log.d("LoginDialog:", "Started");
        setContentView(R.layout.login_dialog);
        setCancelable(false);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        _signupLink = (TextView) findViewById(R.id.link_signup2);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _phNo = (EditText) findViewById(R.id.input_email);
        _forgetPword = (Button) findViewById(R.id.button7);
        _passwordText = (EditText) findViewById(R.id.input_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
        interactiveLayout = (LinearLayout) findViewById(R.id.layout_interactive);
        show();
        setTitle("Auris Life");
        this.getWindow().setBackgroundDrawableResource(R.color.white);






        // loginSV = (ScrollView) findViewById(R.id.login_scrollview);
//        prefs = this.getSharedPreferences("in.gravity", Context.MODE_PRIVATE);


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(context, SignupActivity.class);
                context.startActivity(intent);
            }

        });
        _forgetPword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(context, ForgetPasswordActivity.class);
                context.startActivity(intent);
            }

        });
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LoginDialog:", "Clicked");
                disableUI();
                LoginRequest req = new LoginRequest();
                req.uName = _phNo.getText().toString();
                req.Pword = _passwordText.getText().toString();
                Log.d("LoginCred", "Username:" + req.uName + "Password:" + req.Pword);

                MobileServiceClient mClientLogin = null;
                try {
                    mClientLogin = new MobileServiceClient(SplashPage.APINAME,
                            SplashPage.APIKEY,
                            context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                SplashActivity.initializeMclient(context);
                mClientLogin.invokeApi("CustomLogin", req, CustomLoginResult.class, new ApiOperationCallback<CustomLoginResult>() {
                    //TODO convert to non-deprecated function; remember about Synchronization
                    @Override
                    public void onCompleted(CustomLoginResult result, Exception exception, ServiceFilterResponse response) {
                        synchronized (SplashPage.mAuthenticationLock) {
                            if (exception == null) {
                                Log.d("LoginDialog:", "Success");
                                String userId = result.uId.replace("CUSTOM:", "");
                                MobileServiceUser user = new MobileServiceUser(userId);
                                String tok = result.Mtoken;
                                user.setAuthenticationToken(tok);
                                SplashPage.mClient.setCurrentUser(user);
                                SplashPage.cacheUserToken(SplashPage.mClient.getCurrentUser(), context);
                                Log.d("Result_SUCCESS_UID", userId);
                                Log.d("Result_SUCCESS_Token", tok);

//                            Intent intent = new Intent(getApplicationContext(), Attendance_marking.class);
//                            startActivity(intent);
//                            finish();
                                SplashPage.bAuthenticating = false;
                                SplashPage.mAuthenticationLock.notifyAll();
                                if (context instanceof SplashPage) {
                                    Intent i = new Intent(context, FirstPage.class);
                                    context.startActivity(i);
                                    ((SplashPage) context).finish();
                                }
                                dismiss();

                            } else {
//                            onLoginFailed();
                                Log.d("LoginDialog:", "Failed");
                                enableUI();
                                Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show();
                                exception.printStackTrace();
                                enableUI();
                            }

                        }
                    }
                });

            }
        });

    }

    private void enableUI() {
        interactiveLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }

    private void disableUI() {
        interactiveLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }
}
