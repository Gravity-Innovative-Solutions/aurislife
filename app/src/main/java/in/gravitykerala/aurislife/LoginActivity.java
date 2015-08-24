package in.gravitykerala.aurislife;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

import java.net.MalformedURLException;
import java.util.AbstractList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static MobileServiceClient mClient;
    CheckBox saveLoginCheckBox;
    ScrollView loginSV;
//    String username, password;
//    String KEY_username = "username";
//    String KEY_password = "password";
//    SharedPreferences prefs;

    LinearLayout l1;
    CoordinatorLayout coordinatorLayout;
    View.OnClickListener mOnClickListener = null;
    @InjectView(R.id.input_email)
    EditText _phNo;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;
    @InjectView(R.id.button7)
    Button _forgetPword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

//        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkBox_remember);
        loginSV = (ScrollView) findViewById(R.id.login_scrollview);
//        prefs = this.getSharedPreferences("in.gravity", Context.MODE_PRIVATE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                Log.d("loginstatus:", getString(R.string.login_oprtn_call_finished));

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }

        });
        _forgetPword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }

        });
//        finish();
    }

    public void login() {
        Log.d(TAG, getString(R.string.login));


        if (!validate()) {
            onLoginFailed();
            return;
        }
        loginSV.setVisibility(ScrollView.GONE);
        // _loginButton.setEnabled(false);


//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(false);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
////        String email = _phNo.getText().toString();
////        String password = _passwordText.getText().toString();
//
//        // TODO: Implement your own authentication logic here.
//
        loginreq();
//        progressDialog.dismiss();


//
    }

    public void loginreq() {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
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
        LoginRequest req = new LoginRequest();
        req.uName = _phNo.getText().toString();
        req.Pword = _passwordText.getText().toString();
        SplashPage.initializeMclient(this);
        mClient = SplashPage.mClient;
//        try {
//
//            //LoginActivity.mClient = new MobileServiceClient("https://gravityaurislife.azure-mobile.net", "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68", LoginActivity.this);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        mClient.invokeApi("CustomLogin", req, CustomLoginResult.class, new ApiOperationCallback<CustomLoginResult>() {
            @Override
            public void onCompleted(CustomLoginResult result, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    String userId = result.uId.replace("CUSTOM:", "");
                    MobileServiceUser user = new MobileServiceUser(userId);
                    String tok = result.Mtoken;
                    PrefUtils.saveToPrefs(LoginActivity.this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, userId);
                    PrefUtils.saveToPrefs(LoginActivity.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, tok);
                    user.setAuthenticationToken(tok);
                    mClient.setCurrentUser(user);
                    Log.d("uid", userId);
                    Intent intent = new Intent(getApplicationContext(), FirstPage.class);
                    startActivity(intent);
                    finish();

                } else {
                    loginSV.setVisibility(ScrollView.VISIBLE);
                    onLoginFailed();
                }
                progressDialog.dismiss();
            }
        });

    }


    //@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//                if (resultCode == RESULT_OK) {
//                    Intent intent = new Intent(getApplicationContext(), Profile.class);
//                startActivity(intent);
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        // Disable going back to the Profile
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

//        _loginButton.setEnabled(true);
        finish();
    }

    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
//            progressDialog.dismiss();
//            Toast.makeText(getBaseContext(), "YOU ARE NOT CONNECTED TO AN NETWORK", Toast.LENGTH_LONG).show();
        }
        return false;
    }
    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

//        View.OnClickListener mOnClickListener;
        if (isOnline() == true) {

//            Toast.makeText(getBaseContext(), "Incorrect Username or Password", Toast.LENGTH_LONG).show();
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _phNo.setText("");
                    _passwordText.setText("");

                }
            };
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Incorrect Username or Password", Snackbar.LENGTH_LONG)
                    .setAction("Clear", mOnClickListener);
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();



        } else {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.no_connection, Snackbar.LENGTH_LONG)
                    .setAction("Retry", mOnClickListener);
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();

                }
            };
        }
        _loginButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        String phNo = _phNo.getText().toString();
        String password = _passwordText.getText().toString();

        if (phNo.isEmpty() || phNo.length() != 10) {
            _phNo.setError("10 digit mobile number required");
//            Toast.makeText(this, "Check phone no", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _phNo.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 15) {
            _passwordText.setError("Between 8 and 15 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
