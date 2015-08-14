package in.gravitykerala.aurissample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.AbstractList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecentTransactions extends ActionBarActivity {
    private MobileServiceClient mClient;
    private TextView tv;
    private MobileServiceTable<MobileTransactions> mToDoTable;
    private SwipeRefreshLayout mSwipeLayout;

    private RTAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_transactions);
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
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mSwipeLayout.setRefreshing(true);
                refreshItemsFromTable();
                //mSwipeLayout.setRefreshing(true);
            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        mSwipeLayout.setEnabled(true);
        mClient = LoginActivity.mClient;

        // Get the Mobile Service Table instance to use

        mToDoTable = mClient.getTable(MobileTransactions.class);

        // mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);

        // Create an adapter to bind the items with the view
        mAdapter = new RTAdapter(this, R.layout.transaction_single);
        ListView listViewToDo = (ListView) findViewById(R.id.list);
        listViewToDo.setAdapter(mAdapter);
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
            protected void onPostExecute(Void result) {
                mSwipeLayout.setRefreshing(false);
                Toast toast2 = Toast.makeText(RecentTransactions.this, "no network",
                        Toast.LENGTH_SHORT);
                toast2.show();
            }
            protected Void doInBackground(Void... params) {
                try {
                    final List<MobileTransactions> results =
                            mToDoTable.where().execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mSwipeLayout.setRefreshing(false);
                            if (results.size() == 0) {
                                Toast.makeText(RecentTransactions.this, "NO TRANSACTIPNS YET!!", Toast.LENGTH_LONG).show();

                            } else {
                                for (MobileTransactions item : results) {
                                    mAdapter.add(item);
                                }
                            }
                        }
                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                    createAndShowDialog(e, "Error");
//                    Toast.makeText(RecentTransactions.this,"NO NETWORK",Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {

                    e.printStackTrace();
                    Log.d("netwrk", "prob ");
//                    Toast.makeText(getBaseContext(), "Error retrieving notifications, Probably bad network connection!", Toast.LENGTH_LONG).show();
//                } catch (MobileServiceException e) {
//                    e.printStackTrace();
                }
                return null;
            }

        }.execute();

        mAdapter.clear();
    }


    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        // createAndShowDialog(ex.getMessage(), title);
    }

}

