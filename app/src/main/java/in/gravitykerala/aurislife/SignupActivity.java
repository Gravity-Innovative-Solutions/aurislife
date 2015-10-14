package in.gravitykerala.aurislife;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.AbstractList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    public static MobileServiceClient mClient;
    //   public static MobileServiceClient mClient;
    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_phn)
    EditText _PHNO;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    @InjectView(R.id.input_Ref_phn)
    EditText _refPhoneNo;
    @InjectView(R.id.input_pin)
    EditText _pincode;
    Spinner spinner_districts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        AzureMobileServiceAuris.initialize(this);
        spinner_districts = (Spinner) findViewById(R.id.spinner_districts);
        try {
            mClient = new MobileServiceClient(SplashPage.APINAME, SplashPage.APIKEY, this);
//                Log.d("PushNotification:", "registering");
//                NotificationsManager.handleNotifications(context, GCM_PUSH_SENDER_ID, PushNotificationHandler.class);
//                Log.d("PushNotification:", "registered");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO check for Netowrk connectivity and add Exception handling

        }
        //mClient = A;
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);

        CheckBox checkbox_add_ref = (CheckBox) findViewById(R.id.add_referror);
        ButterKnife.inject(this);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    _passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    _passwordText.setInputType(129);
                }
            }
        });

        checkbox_add_ref.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    _refPhoneNo.setVisibility(View.VISIBLE);
                } else {
                    _refPhoneNo.setVisibility(View.GONE);
                }
            }
        });


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        //  mClient = FirstPage.mClient;

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            //onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

//        String name = _nameText.getText().toString();
//        String email = _phNo.getText().toString();
//        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        signupreq();
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    public void signupreq() {
        {
            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
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
            CustomRegistration req = new CustomRegistration();
            req.Name = _nameText.getText().toString();
            req.Email = _emailText.getText().toString();
            req.phno = _PHNO.getText().toString();
            req.Pword = _passwordText.getText().toString();
            req.refrel = _refPhoneNo.getText().toString();
            req.area = spinner_districts.getSelectedItem().toString();
            req.pin = _pincode.getText().toString();
            final ListenableFuture<String> result = mClient.invokeApi("CustomRegistration", req, "POST", parameters, String.class);
            Futures.addCallback(result, new FutureCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    //  Log.d("Success", result.toString());
                    onSignupSuccess();
                }


                @Override
                public void onFailure(Throwable t) {
                    Log.d("Failed", result.toString());
                    onSignupFailed();
                }
            });


//                @Override
//                public void onCompleted (String result, Exception exception, ServiceFilterResponse
//                response){
//                if (exception == null) {
//                    Toast.makeText(SignupActivity.this, R.string.success, Toast.LENGTH_LONG).show();
//                    finish();
//
//                } else {
//                    onSignupFailed();
//                }
//                progressDialog.dismiss();
//            }
//            }
//
//            );
        }

    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Registration Success", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registration Failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String phNo = _PHNO.getText().toString();
        String refPhNo = _refPhoneNo.getText().toString();
        String pincode = _pincode.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("At least 3 characters");
//            Toast.makeText(this, "Check your name", Toast.LENGTH_LONG).show();
//            Toast.makeText(this, "Password minimum 8 characters required", Toast.LENGTH_LONG);
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Email error");
//            Toast.makeText(this, "Check email", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (phNo.isEmpty() || phNo.length() != 10) {
            _PHNO.setError("10 digit mobile number");
//            Toast.makeText(this, "Check phone no", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _PHNO.setError(null);
        }

        if (pincode.isEmpty() || pincode.length() != 6) {
            _pincode.setError("Enter Valid Pincode ");
//            Toast.makeText(this, "Check phone no", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _PHNO.setError(null);
        }
        if ((refPhNo.isEmpty()) || refPhNo.length() != 10)
            _refPhoneNo.setError("Enter 10 Digit Mobile Number");


        if (password.isEmpty() || password.length() < 8 || password.length() > 15) {
            _passwordText.setError("between 8 and 15 characters");
//            Toast.makeText(this, "Password between 8 and 15 characters", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (phNo.isEmpty() || phNo.length() != 10) {
            _PHNO.setError("10 digit mobile number");
//            Toast.makeText(this, "Check phone no", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _PHNO.setError(null);
        }

        return valid;
    }
}