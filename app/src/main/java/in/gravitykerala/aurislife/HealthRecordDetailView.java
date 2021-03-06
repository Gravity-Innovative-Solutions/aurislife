package in.gravitykerala.aurislife;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.AbstractList;
import java.util.List;

import in.gravitykerala.aurislife.model.MobilePrescription;

public class HealthRecordDetailView extends AppCompatActivity {
    // private MobileServiceClient mClient;
    private TextView tv, pull;

    private MobileServiceTable<MobileHealthRecordCustom> mToDoTable;
    private SwipeRefreshLayout mSwipeLayout;

    private HRDAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
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
        AzureMobileService.initialize(this);
        // SplashPage.Storetok(this);
        // mClient = AzureMobileService.client;

        // Get the Mobile Service Table instance to use

        // mToDoTable = AzureMobileService.client.getTable("MobileHealthRecordCustom", MobileHealthRecordCustom.class);

        // mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);

        // Create an adapter to bind the items with the view
        mAdapter = new HRDAdapter(this, R.layout.health_single);
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
        ListenableFuture<MobileHealthRecordCustom.elements[]> result = AzureMobileService.client.invokeApi("MobileHealthRecordDetails", MobileHealthRecordCustom.elements[].class);

        Futures.addCallback(result, new FutureCallback<MobileHealthRecordCustom.elements[]>() {
            @Override
            public void onFailure(Throwable exc) {
                exc.printStackTrace();
                Log.d("Output", "" + exc);
                //createAndShowDialog((Exception) exc, "Error");
            }

            @Override
            public void onSuccess(MobileHealthRecordCustom.elements[] result) {
                String[] r = null;
                String r1 = "";

                // List<MobileHealthRecordCustom> s = result.length;
                if (result.length == 0) {
                    mSwipeLayout.setRefreshing(false);
                    Toast.makeText(HealthRecordDetailView.this, "No Item", Toast.LENGTH_LONG).show();
                } else {
                    for (MobileHealthRecordCustom.elements item1 : result) {

                        mSwipeLayout.setRefreshing(false);
                        mAdapter.add(item1);
                    }
                }

            }
        });
        // mSwipeLayout.setRefreshing(false);
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

    public void detail(final MobilePrescription item) {

        Intent newActivity0 = new Intent(this, Detail.class).putExtra("<id>", item.getId());
        newActivity0.putExtra("id", item.getId());
        Log.d("id", item.getId());
        startActivity(newActivity0);


    }
}

