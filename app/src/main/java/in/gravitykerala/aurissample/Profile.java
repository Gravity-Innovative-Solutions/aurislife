package in.gravitykerala.aurissample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Profile extends AppCompatActivity {
    Button chngepswd;


//    @InjectView(R.id.btn_change_pswd) Button _btnChangePswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        chngepswd = (Button) findViewById(R.id.btn_change_pswd);


//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);

//        _btnChangePswd.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Profile.this, ChangePasswordActivity.class);
//                startActivity(intent);
//            }
//        });


        chngepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangePasswordActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        // Disable going back to the Profile
        moveTaskToBack(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
