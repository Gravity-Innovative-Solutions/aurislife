package in.gravitykerala.aurissample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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

                        int position = spnr.getSelectedItemPosition();
                        Toast.makeText(getApplicationContext(), "You have selected " + operators[+position], Toast.LENGTH_LONG).show();
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
                String phn_string = phn.getText().toString();
                if (phn_string == null) {
                    Toast.makeText(Transaction.this, "Enter your Phone number", Toast.LENGTH_SHORT).show();
                } else {


                addItem();
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
        String amnt = phn.getText().toString().trim();
        final int amt = (Integer.parseInt(amnt));
        //Toast.makeText(this,amnt,Toast.LENGTH_LONG).show();

        //  item.recAmnt = amt;
        //  Toast.makeText(this,""+amt,Toast.LENGTH_LONG).show();
        //  item.Cname = (spnr.getSelectedItem().toString());

        // item.sId = mClient.getCurrentUser().getUserId();
        // item.setComplete(false);

        // Insert the new item
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
                            item.recAmnt = amt;
                            Toast.makeText(Transaction.this, "" + amt, Toast.LENGTH_LONG).show();
                            item.Cname = (spnr.getSelectedItem().toString());
                            //}

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    createAndShowDialog(e, "Error");

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