package in.gravitykerala.aurislife;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.net.MalformedURLException;
import java.util.AbstractList;
import java.util.List;

public class ForgetPasswordActivity extends AppCompatActivity {
    public static MobileServiceClient mClient;
    EditText PhnNum;
    EditText Email;
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        try {
            mClient = new MobileServiceClient("https://gravityaurislife.azure-mobile.net",
                    "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68",
                    this);
//            String userId = PrefUtils.getFromPrefs(FirstPage.this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
//            String tok = PrefUtils.getFromPrefs(FirstPage.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
//            MobileServiceUser user = new MobileServiceUser(userId);
//            user.setAuthenticationToken(tok);
//            mClient.setCurrentUser(user);
//            if (userId.equals("default") && tok.equals("default")) {
//                Intent i = new Intent(this, LoginActivity.class);
//                startActivity(i);
//            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PhnNum = (EditText) findViewById(R.id.input_phnnum);
        Email = (EditText) findViewById(R.id.input_email);
        Submit = (Button) findViewById(R.id.button);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit() {

        {
            final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this,
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
            ForgetPassword req = new ForgetPassword();
            req.phnumber = PhnNum.getText().toString();
            req.email = Email.getText().toString();
//            String new_pwd_s = NewPwd.getText().toString();
//            String rnew_pwd_s = ReNpwd.getText().toString();


            mClient.invokeApi("ForgetPassword", req, String.class, new ApiOperationCallback<String>() {
                @Override
                public void onCompleted(String result, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        Toast.makeText(ForgetPasswordActivity.this, result, Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forget_password, menu);
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
}
