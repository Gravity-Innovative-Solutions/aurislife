package in.gravitykerala.aurislife;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;

public class Notifications extends AppCompatActivity {
    private static final String KEY_TITLE = "title";
    private NotificationItemAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private MobileServiceTable<MobileNotifications.Exam> mToDoTable;

    public Notifications() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SplashPage.initializeMclient(this);
        mToDoTable = SplashPage.mClient.getTable("MobileNotifications", MobileNotifications.Exam.class);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isOnline();
                getNotificationList();
                mSwipeLayout.setRefreshing(true);
            }
        });
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        mSwipeLayout.setEnabled(true);
        mAdapter = new NotificationItemAdapter(Notifications.this, R.layout.notification_single);
        ListView listViewToDo = (ListView) findViewById(R.id.list_noti);
        listViewToDo.setAdapter(mAdapter);
        isOnline();
        getNotificationList();

    }


    public void getNotificationList() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        new AsyncTask<Void, Void, Void>() {
            @Override

            protected Void doInBackground(Void... params) {
                try {
                    final List<MobileNotifications.Exam> results =
                            mToDoTable.where().execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mSwipeLayout.setRefreshing(false);
                            if (results.size() == 0) {
                                Toast.makeText(Notifications.this, "No Notification", Toast.LENGTH_LONG).show();

                            } else {
                                for (MobileNotifications.Exam item : results) {
                                    mAdapter.add(item);

                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();

                    // createAndShowDialog(e, "Error");


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
}
