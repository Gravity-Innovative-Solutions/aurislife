package in.gravitykerala.aurislife;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;

import in.gravitykerala.aurislife.R;
import in.gravitykerala.aurislife.model.MobilePrescription;

public class Detail extends AppCompatActivity {
    private MobileServiceTable<MobilePrescription> mToDoTable;
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tv = (TextView) findViewById(R.id.textView2);
        tv1 = (TextView) findViewById(R.id.textView3);
        tv2 = (TextView) findViewById(R.id.textView4);
        tv3 = (TextView) findViewById(R.id.textView6);
        SplashPage.initializeMclient(this);
        //SplashPage.Storetok(this);
        // mClient = SplashPage.mClient;

        // Get the Mobile Service Table instance to use

        mToDoTable = SplashPage.mClient.getTable("MobilePrescriptions", MobilePrescription.class);
        GetItemsFromTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

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

    public void GetItemsFromTable() {
        final String s = getIntent().getExtras().getString("id");
        // Get the items that weren't marked as completed and add them in the
        // adapter

        new AsyncTask<Void, Void, Void>() {
            @Override

            protected Void doInBackground(Void... params) {
                try {
                    final List<MobilePrescription> results =
                            mToDoTable.where().field("Id").eq(s).execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if (results.size() == 0) {
                                Toast.makeText(Detail.this, R.string.no_prescription, Toast.LENGTH_LONG).show();

                            } else {
                                for (MobilePrescription item : results) {
                                    if (item.getDoctorName() == null) {
                                        tv.setText("Not Yet Assigned");
                                    } else {

                                        tv.setText(item.getDoctorName());
                                    }
                                    if (item.getClinicName() == null) {
                                        tv1.setText("Not Yet Assigned");
                                    } else {

                                        tv1.setText(item.getClinicName());
                                    }
                                    if (item.getDoctorName() == null) {
                                        tv3.setText("Not Yet ACCEPTED");
                                    } else {

                                        tv3.setText("" + item.getPrescriptionNumber());
                                    }
                                    tv2.setText("" + item.getPoints());


                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();

                    //createAndShowDialog(e, "Error");


                }
                return null;
            }

            protected void onPostExecute(Void results) {
                super.onPostExecute(results);
//
            }
        }.execute();


    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {

            Toast.makeText(getBaseContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
