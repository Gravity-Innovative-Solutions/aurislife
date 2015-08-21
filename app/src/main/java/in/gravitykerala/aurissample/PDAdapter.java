package in.gravitykerala.aurissample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class PDAdapter extends ArrayAdapter<MobilePrescriptions> {

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

        final MobilePrescriptions currentItem = getItem(position);

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
        tvTitle.setText("PRISCRIPTION STATUS" + ":" + "\t" + currentItem.stats);
        tvContent0.setText("PRISCRIPTION NUMBER" + ":" + "\t" + currentItem.Prisno);
        tvContent1.setText("REMARK" + ":" + "\t" + currentItem.remark);
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
