package in.gravitykerala.aurissample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

public class Transaction extends Activity {
    MobileServiceClient mClient;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        mClient = LoginActivity.mClient;
        mToDoTable = mClient.getTable(MobileTransactions.class);
        phn = (EditText) findViewById(R.id.input_phn);
        sbmt = (Button) findViewById(R.id.submit);
        spnr = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, operators);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        position = spnr.getSelectedItemPosition();
                        //   Toast.makeText(getApplicationContext(), "You have selected " + operators[+position], Toast.LENGTH_LONG).show();
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


                if (amount == 0) {
                    Toast.makeText(Transaction.this, "Enter your Recharge Amount", Toast.LENGTH_SHORT).show();
                } else {
                    String phn_string = phn.getText().toString();
                    int a = Integer.parseInt(phn_string);
                    if (a > bal) {
                        Toast.makeText(Transaction.this, "Your Balance Is Less Than You Request", Toast.LENGTH_LONG).show();
                    } else {


                        addItem();
                    }
                }
            }
        });
    }

    public void addItem() {
        if (mClient == null) {
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
                            // mAdapter.add(entity);

                            Toast.makeText(Transaction.this, Connction + "\t" + amt, Toast.LENGTH_LONG).show();
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
        }.execute();

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
}