package in.gravitykerala.aurissample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

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
    String username, password;
    String KEY_username = "username";
    String KEY_password = "password";
    SharedPreferences prefs;

    LinearLayout l1;
    CoordinatorLayout coordinatorLayout;
    View.OnClickListener mOnClickListener = null;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkBox_remember);
        prefs = this.getSharedPreferences("in.gravity", Context.MODE_PRIVATE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                username = prefs.getString(KEY_username, "");
                password = prefs.getString(KEY_password, "");
                _emailText.setText(username);
                _passwordText.setText(password);
                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();
                if (saveLoginCheckBox.isChecked()) {

                    prefs.edit().putString(KEY_username, username).commit();
                    prefs.edit().putString(KEY_password, password).commit();

//                        SharedPreferences prefs = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                    // prefs.edit().putString(username, us).putString(password, ps).commit();
                } else {

                    //  SharedPreferences prefs = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                    prefs.edit().clear().commit();

                }


            }
        });
        try {
            mClient = new MobileServiceClient("https://gravityaurislife.azure-mobile.net",
                    "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68",
                    this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }

        });
    }

    public void login() {
        Log.d(TAG, "Login");


        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        loginreq();
        progressDialog.dismiss();


//
    }

    public void loginreq() {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
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
        LoginRequest req = new LoginRequest();
        req.uName = _emailText.getText().toString();
        req.Pword = _passwordText.getText().toString();
        mClient.invokeApi("CustomLogin", req, CustomLoginResult.class, new ApiOperationCallback<CustomLoginResult>() {
            @Override
            public void onCompleted(CustomLoginResult result, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    String userId = result.uId.replace("CUSTOM:", "");
                    MobileServiceUser user = new MobileServiceUser(userId);
                    String tok = result.Mtoken;
                    user.setAuthenticationToken(tok);
                    mClient.setCurrentUser(user);
                    Log.d("uid", userId);
                    Intent intent = new Intent(getApplicationContext(), FirstPage.class);
                    startActivity(intent);
                    finish();

                } else {
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
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Incorrect username or passsword", Snackbar.LENGTH_LONG)
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
                    _emailText.setText("");

                }
            };

        } else {
            Toast.makeText(getBaseContext(), "YOU ARE NOT CONNECTED TO AN NETWORK", Toast.LENGTH_LONG).show();
        }
        _loginButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;


//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }

//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }

        return valid;
    }
}
