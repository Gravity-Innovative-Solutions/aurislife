package in.gravitykerala.aurislife;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

import java.util.AbstractList;
import java.util.List;

public class OTPverification extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Creates textview for centre title
        TextView myMsg = new TextView(this);
        myMsg.setText("Update AurisLife");
        myMsg.setBackgroundColor(Color.WHITE);
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        myMsg.setTextSize(20);
        myMsg.setTextColor(Color.BLUE);
        //set custom title
        builder.setCustomTitle(myMsg);
        builder.setMessage("New Version available...adfsf ffmdnfdnf fjsfndkfnf jsafnjfns afdd");

        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.show();
        //Create custom message
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);

        tv = (TextView) findViewById(R.id.input_crnt_password);
        Button retreve = (Button) findViewById(R.id.button8);
        retreve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendotp();
            }
        });
        Button sedntotp = (Button) findViewById(R.id.button);
        sedntotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newOTP();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_otpverification, menu);
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

    public void sendotp() {

        {
            final ProgressDialog progressDialog = new ProgressDialog(OTPverification.this,
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
            SplashPage.mClient.invokeApi("OTPRetrieval", String.class, new ApiOperationCallback<String>() {
                @Override
                public void onCompleted(String result, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        Toast.makeText(OTPverification.this, result, Toast.LENGTH_LONG).show();

                        finish();

                    } else {
                        Toast.makeText(OTPverification.this, result, Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });
            ;
        }

    }

    public void newOTP() {

        String OTP = tv.getText().toString();
        Log.d("OTP", OTP);
        final ProgressDialog progressDialog = new ProgressDialog(OTPverification.this,
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
        SplashPage.mClient.invokeApi("OTPVerification?Password=" + tv.getText().toString(), String.class, new ApiOperationCallback<String>() {
            @Override
            public void onCompleted(String result, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Toast.makeText(OTPverification.this, result, Toast.LENGTH_LONG).show();

                    finish();

                } else {
                    Toast.makeText(OTPverification.this, result, Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
        ;


    }
}
