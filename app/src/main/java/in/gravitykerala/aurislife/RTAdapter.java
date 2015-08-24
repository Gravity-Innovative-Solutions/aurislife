package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class RTAdapter extends ArrayAdapter<MobileTransactions> {

    /**
     * Adapter context
     */
    Context mContext;


    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public RTAdapter(Context context, int layoutResourceId) {
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

        final MobileTransactions currentItem = getItem(position);

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
        if (currentItem.stats.matches("Pending")) {
            tvTitle.setTextColor(Color.BLUE);

        } else if (currentItem.stats.matches("Success")) {
            tvTitle.setTextColor(Color.GREEN);

        } else if (currentItem.stats.matches("Failed")) {
            tvTitle.setTextColor(Color.RED);

        }

        tvTitle.setText("RECHARGE STATUS" + ":" + "\t" + currentItem.stats);
        tvContent0.setText("RECHARGE AMOUNT" + ":" + "\t" + currentItem.recAmnt);
        tvContent1.setText("REMARKS" + ":" + "\t" + currentItem.remark);

//        tvContent.setOnClickListener(new View.OnClickListener() {
//            //
//            @Override
//            public void onClick(View arg0) {
//                Attendance activity = (Attendance) mContext;
//                activity.Attendance(currentItem);
//            }
//        });
        return row;
    }
}
