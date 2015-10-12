package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.gravitykerala.aurislife.model.MobilePrescription;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class
        PDAdapter extends ArrayAdapter<MobilePriscriptionCustom.pelements> {
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

        final MobilePriscriptionCustom.pelements currentItem = getItem(position);

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


        final TextView tvstatus = (TextView) row.findViewById(R.id.status);
        final TextView tvContent0 = (TextView) row.findViewById(R.id.dctrname);
//        final TextView tvContent1 = (TextView) row.findViewById(R.id.uri);
//        tv_success.setText(currentItem.getStatus());

        tvstatus.setText(currentItem.status);
        tvContent0.setText(currentItem.doctorName);
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//        Date date = new Date();
//        String ourformat = formatter.format(currentItem.getEventDate());
        tvTitle.setText(currentItem.eventDate.toString());
//        String r1 = "";
        LinearLayout documentsLayout = (LinearLayout) row.findViewById(R.id.documentsLayout);


        for (int i = 0; i < currentItem.imagess.length; i++) {
            Log.d("ID", currentItem.imagess[i].getImageUri());
            TextView inflateTextView = (TextView) View.inflate(mContext, R.layout.health_record_item, null);
            inflateTextView.setText("Download document " + (i + 1));
//            r1 = r1 + currentItem.healthRecordDocuments[i].getImageUri() + "\n";
            inflateTextView.setTag(currentItem.imagess[i].getImageUri());
            inflateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse((String) v.getTag()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(intent);
                }
            });
            documentsLayout.addView(inflateTextView);
        }

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
