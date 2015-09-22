package in.gravitykerala.aurislife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Adapter to bind a ToDoItem List to a view
 */
public class NotificationItemAdapter extends ArrayAdapter<StudentNotificationDTO.Exam> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public NotificationItemAdapter(Context context, int layoutResourceId) {
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

        final StudentNotificationDTO.Exam currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        //Initialize ImageView and others
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView2);
        final TextView tvTitle = (TextView) row.findViewById(R.id.row_title);
        final TextView tvContent = (TextView) row.findViewById(R.id.row_content);
        tvTitle.setText(currentItem.title);
        tvContent.setText(currentItem.content);


//Loading image from below url into imageView

        Picasso.with(getContext())
                .load(currentItem.getId())
                .into(imageView);

//        checkBox.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                if (checkBox.isChecked()) {
//                    checkBox.setEnabled(false);
//                    if (mContext instanceof NotificationActivity) {
//                        NotificationActivity activity = (NotificationActivity) mContext;
//                        activity.checkItem(currentItem);
//                    }
//                }
//            }
//        });

        return row;
    }

}