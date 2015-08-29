package in.gravitykerala.aurislife;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;

import in.gravitykerala.aurislife.model.MobilePrescription;

public class PriscriptionDetails extends ActionBarActivity {
    // private MobileServiceClient mClient;
    private TextView tv, pull;

    private MobileServiceTable<MobilePrescription> mToDoTable;
    private SwipeRefreshLayout mSwipeLayout;

    private PDAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priscription_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        //buttonRefresh = (Button) findViewById(R.id.button_refresh);
        // Initialize the progress bar
        // mProgressBar.setVisibility(ProgressBar.GONE);

//            buttonRefresh.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    refreshItemsFromTable();
//
//                }
        // });
        pull = (TextView) findViewById(R.id.textView_pull);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mSwipeLayout.setRefreshing(true);
                isOnline();
                refreshItemsFromTable();
                //mSwipeLayout.setRefreshing(true);
            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        mSwipeLayout.setEnabled(true);
        SplashPage.initializeMclient(this);
        SplashPage.Storetok(this);
        // mClient = SplashPage.mClient;

        // Get the Mobile Service Table instance to use

        mToDoTable = SplashPage.mClient.getTable("MobilePrescriptions", MobilePrescription.class);

        // mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);

        // Create an adapter to bind the items with the view
        mAdapter = new PDAdapter(this, R.layout.priscription_single);
        ListView listViewToDo = (ListView) findViewById(R.id.list);
        listViewToDo.setAdapter(mAdapter);
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        isOnline();
        refreshItemsFromTable();
    }
    // Load the items from the Mobile Service
    // refreshItemsFromTable();

    //        String s = getIntent().getExtras().getString("day");
//        Toast.makeText(getBaseContext(), s,
//                Toast.LENGTH_LONG).show();
    public void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        new AsyncTask<Void, Void, Void>() {
            @Override

            protected Void doInBackground(Void... params) {
                try {
                    final List<MobilePrescription> results =
                            mToDoTable.where().execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mSwipeLayout.setRefreshing(false);
                            if (results.size() == 0) {
                                Toast.makeText(PriscriptionDetails.this, R.string.no_prescription, Toast.LENGTH_LONG).show();

                            } else {
                                for (MobilePrescription item : results) {
                                    mAdapter.add(item);
                                    pull.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();

                    createAndShowDialog(e, "Error");


                }
                return null;
            }

            protected void onPostExecute(Void results) {
                super.onPostExecute(results);
//
            }
        }.execute();

        mAdapter.clear();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            mSwipeLayout.setRefreshing(false);
            Toast.makeText(getBaseContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        // createAndShowDialog(ex.getMessage(), title);
    }

}

