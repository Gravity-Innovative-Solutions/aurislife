package in.gravitykerala.aurissample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

import org.apache.http.HttpResponse;

import java.util.AbstractList;
import java.util.List;


public class ChangePasswordActivity extends AppCompatActivity {
    public static MobileServiceClient mClient;
    EditText Curerntpwd;
    EditText NewPwd;
    EditText ReNpwd;
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Curerntpwd = (EditText) findViewById(R.id.input_crnt_password);
        NewPwd = (EditText) findViewById(R.id.input_new_password);
        ReNpwd = (EditText) findViewById(R.id.input_new_confirm_password);
        Submit = (Button) findViewById(R.id.button);
        mClient = LoginActivity.mClient;
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit() {
        {
            final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this,
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
            ChangePassword req = new ChangePassword();
            req.cpwd = Curerntpwd.getText().toString();
            req.npwd = NewPwd.getText().toString();
//            req.phno = _PHNO.getText().toString();
//            req.Pword = _passwordText.getText().toString();

            mClient.invokeApi("ChangePassword", req, String.class, new ApiOperationCallback<String>() {
                @Override
                public void onCompleted(String result, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        Toast.makeText(ChangePasswordActivity.this, result, Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(ChangePasswordActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }
//    @Override
//    public void onBackPressed() {
//        // Disable going back to the MainActivity
//        moveTaskToBack(true);
//    }
//

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
}