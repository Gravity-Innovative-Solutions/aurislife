package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
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

        final TextView tvContent0 = (TextView) row.findViewById(R.id.amt);
        final TextView tvContent1 = (TextView) row.findViewById(R.id.remark);
        tvTitle.setText("PRISCRIPTION STATUS" + ":" + "\t" + currentItem.getStatus());
        tvContent0.setText("PRISCRIPTION NUMBER" + ":" + "\t" + currentItem.getPrescriptionNumber());
        tvContent1.setText("REMARK" + ":" + "\t" + currentItem.getRemarks());
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
