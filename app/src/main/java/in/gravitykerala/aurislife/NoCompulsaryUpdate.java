package in.gravitykerala.aurislife;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

public class NoCompulsaryUpdate extends Dialog {
    public NoCompulsaryUpdate(final Context context) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.activity_no_compulsary_update);
        show();

        Button btn = (Button) findViewById(R.id.button10);
        Button btn1 = (Button) findViewById(R.id.button11);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.aurislife&hl=en"));
                context.startActivity(browserIntent);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(context, FirstPage.class);
                context.startActivity(browserIntent);
            }
        });
    }
}