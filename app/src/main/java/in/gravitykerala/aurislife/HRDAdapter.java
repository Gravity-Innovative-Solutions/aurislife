package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.gravitykerala.aurislife.model.MobilePrescription;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class HRDAdapter extends ArrayAdapter<MobileHealthRecordCustom.elements> {

    /**
     * Adapter context
     */
    Context mContext;


    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public HRDAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        String r1 = null;
        final MobileHealthRecordCustom.elements currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        String status;
//        if (currentItem.stats.equals("true")) {
//            status = "sucess";
//
//        } else {
//            status = "failed";
//        }
        final TextView tvTitle = (TextView) row.findViewById(R.id.title);


        final TextView tvdate = (TextView) row.findViewById(R.id.status);
        final TextView tvContent0 = (TextView) row.findViewById(R.id.amt);
        final TextView tvContent1 = (TextView) row.findViewById(R.id.uri);
//        tv_success.setText(currentItem.getStatus());


        tvdate.setText("Record STATUS" + ":" + "\t" + currentItem.reccomplt);
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//        Date date = new Date();
//        String ourformat = formatter.format(currentItem.getEventDate());
        tvTitle.setText(currentItem.rectitle);
        for (int i = 0; i < currentItem.hlthrecdocumnt.length; i++) {
            Log.d("ID", currentItem.hlthrecdocumnt[i].getImageUri());

            r1 = r1 + currentItem.hlthrecdocumnt[i].getImageUri() + "\n";

        }
        tvContent0.setText(r1);
//        tv_success.setText(currentItem.getStatus());
//        tv_success.setText(currentItem.getStatus());
//        tvContent0.setText("PRESCRIPTION NUMBER" + ":" + "\t" + currentItem.getPrescriptionNumber());
//        if (currentItem.getRemarks() == null) {
//            String remarks = "Not Completed";
//            tvContent1.setText("REMARKS" + ":" + "\t" + remarks);
//        } else {
//            tvContent1.setText("REMARKS" + ":" + "\t" + currentItem.getRemarks());
//        }
//        tvTitle.setOnClickListener(new View.OnClickListener() {
//            //
//            @Override
//            public void onClick(View arg0) {
//                PriscriptionDetails activity = (PriscriptionDetails) mContext;
//                activity.detail(currentItem);
//            }
//        });
//        tvContent.setOnClickListener(new View.OnClickListener() {
//            //
//            @Override
//            public void onClick(View arg0) {
//                Attendance activity = (Attendance) ontext;
//                activity.Attendance(currentItem);
//            }
//        });
        return row;
    }
}
