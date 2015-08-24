package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import in.gravitykerala.aurislife.model.MobilePrescription;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class PDAdapter extends ArrayAdapter<MobilePrescription> {

    /**
     * Adapter context
     */
    Context mContext;


    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public PDAdapter(Context context, int layoutResourceId) {
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

        final MobilePrescription currentItem = getItem(position);

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
        final TextView tvTitle = (TextView) row.findViewById(R.id.status);
        final TextView tv_success = (TextView) row.findViewById(R.id.textView_success);
        final TextView tv_failed = (TextView) row.findViewById(R.id.textView_failed);


        final TextView tvContent0 = (TextView) row.findViewById(R.id.amt);
        final TextView tvContent1 = (TextView) row.findViewById(R.id.remark);
//        tv_success.setText(currentItem.getStatus());
        if (currentItem.getStatus().matches("Pending")) {
            tvTitle.setTextColor(Color.BLUE);

        } else if (currentItem.getStatus().matches("Success")) {
            tvTitle.setTextColor(Color.GREEN);

        } else if (currentItem.getStatus().matches("Failed")) {
            tvTitle.setTextColor(Color.RED);

        }

        tvTitle.setText("PRESCRIPTION STATUS" + ":" + "\t" + currentItem.getStatus());
//        tv_success.setText(currentItem.getStatus());
//        tv_success.setText(currentItem.getStatus());


        tvContent0.setText("PRESCRIPTION NUMBER" + ":" + "\t" + currentItem.getPrescriptionNumber());
        tvContent1.setText("REMARKS" + ":" + "\t" + currentItem.getRemarks());
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
