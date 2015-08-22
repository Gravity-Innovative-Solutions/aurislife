package in.gravitykerala.aurislife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

import java.net.MalformedURLException;

public class Uidtoken extends AppCompatActivity {

    public static MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidtoken);

        usertok();
        Intent i = new Intent(this, FirstPage.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_uidtoken, menu);
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
        try {
            mClient = new MobileServiceClient("https://gravityaurislife.azure-mobile.net",
                    "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68",
                    this);
            String uid = getIntent().getExtras().getString("Uid");
            String token = getIntent().getExtras().getString("Tok");
            MobileServiceUser user = new MobileServiceUser(uid);
            user.setAuthenticationToken(token);
            mClient.setCurrentUser(user);
//
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
