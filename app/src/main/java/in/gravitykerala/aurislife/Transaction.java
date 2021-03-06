package in.gravitykerala.aurislife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

public class Transaction extends AppCompatActivity {
    //MobileServiceClient mClient;
    MobileServiceTable<MobileTransactions> mToDoTable;
    EditText phn;
    Spinner spnr;
    Button sbmt;
    int position = -1;
    int amount = 0;
    String[] operators = {
            "IDEA",
            "BSNL",
            "VODAFONE",
            "DOCOMO",
            "AIRTEL",
            "AIRCEL",
            "RELIANCE",
            "UNINOR",
            "TATA INDICOM"
    };
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        AzureMobileService.initialize(this);
        //SplashPage.Storetok(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_3);
        mProgressBar.setVisibility(ProgressBar.GONE);

        // mClient = AzureMobileService.client;
        mToDoTable = AzureMobileService.client.getTable(MobileTransactions.class);
        phn = (EditText) findViewById(R.id.input_phn);
        sbmt = (Button) findViewById(R.id.submit);
        spnr = (Spinner) findViewById(R.id.spinner);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_selector, operators);


        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        position = spnr.getSelectedItemPosition();
                        //   Toast.makeText(getApplicationContext(), "You have selected " + operators[+position], Toast.LENGTH_LONG).show();
                        // Toast.makeText(getApplicationContext(), R.string.youhaveslcted + operators[+position], Toast.LENGTH_LONG).show();
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );
        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount = phn.getText().length();

                int bal = getIntent().getIntExtra("bal", 0);
                if (!validate(bal)) {
                    return;
                }

                if (amount == 0) {
                    Toast.makeText(Transaction.this, R.string.enter_rchrg_amnt, Toast.LENGTH_SHORT).show();
                } else {
                    String phn_string = phn.getText().toString();
                    int a = Integer.parseInt(phn_string);
                    if (a > bal) {
                        Toast.makeText(Transaction.this, R.string.lesser_blnce, Toast.LENGTH_LONG).show();
                    } else {


                        addItem();
                    }
                }
            }
        });
    }

    public boolean validate(Integer userBalance) {
        boolean valid = true;

        String requestAmount = phn.getText().toString();
        if (requestAmount.isEmpty() || requestAmount.length() > 3) {
            phn.setError("Request recharge up to Rs " + userBalance + " (Your balance)");
            valid = false;
//            return false;
        } else {
            int amount = Integer.parseInt(requestAmount);


            if (amount < 0 || amount > userBalance) {
                phn.setError("Request recharge up to Rs " + userBalance + " (Your balance");
//            Toast.makeText(this, "Check phone no", Toast.LENGTH_LONG).show();
                valid = false;
            } else {
                phn.setError(null);
            }
        }

        return valid;
    }

    public void addItem() {
        if (AzureMobileService.client == null) {
            return;
        }

        // Create a new item
        final MobileTransactions item = new MobileTransactions();
        final String amnt = phn.getText().toString().trim();
        final int amt = (Integer.parseInt(amnt));
        final String Connction = spnr.getSelectedItem().toString();

        //Toast.makeText(this,amnt,Toast.LENGTH_LONG).show();

        //  item.recAmnt = amt;
        //  Toast.makeText(this,""+amt,Toast.LENGTH_LONG).show();
        //  item.Cname = (spnr.getSelectedItem().toString());

        // item.sId = mClient.getCurrentUser().getUserId();
        // item.setComplete(false);

        // Insert the new item
        item.recAmnt = amt;

        item.Cname = Connction;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final MobileTransactions entity = mToDoTable.insert(item).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //if(!entity.isComplete()){
                            // mAdapter.add(entity);0

                            Toast.makeText(Transaction.this, R.string.success, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Transaction.this, RecentTransactions.class);

                            startActivity(i);
                            //}

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    createAndShowDialog(e, "Error");
                    Log.d("", "error");

                }

                return null;

            }

            protected void onPostExecute(Void results) {
                super.onPostExecute(results);
                phn.setVisibility(EditText.VISIBLE);
                spnr.setVisibility(Spinner.VISIBLE);
                sbmt.setVisibility(Button.VISIBLE);
                mProgressBar.setVisibility(ProgressBar.GONE);
            }
        }.execute();
        sbmt.setVisibility(Button.GONE);
        phn.setVisibility(EditText.GONE);
        spnr.setVisibility(Spinner.GONE);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        phn.setText("");
        //pnr.setText("");
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        // createAndShowDialog(ex.getMessage(), title);
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        if (id == R.id.action_verify_otp) {
//           Intent i=new Intent(Transaction.this,OTPverification.class);
//            startActivity(i);
//        }
//        return super.onOptionsItemSelected(item);
//    }

}