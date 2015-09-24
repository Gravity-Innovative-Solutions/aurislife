package in.gravitykerala.aurislife;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

public class CompolsuryUpdate extends Dialog {
    public CompolsuryUpdate(final Context context) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.activity_compolsury_update);
        getWindow().setBackgroundDrawableResource(R.color.primary_lighter);
        show();
        setTitle("Update Alert");



        Button btn = (Button) findViewById(R.id.button9);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.aurislife&hl=en"));
                context.startActivity(browserIntent);
            }
        });
    }
}