package in.gravitykerala.aurislife;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import in.gravitykerala.aurislife.R;

public class CompolsuryUpdate extends Dialog {
    public CompolsuryUpdate(final Context context) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.activity_compolsury_update);
        show();
        Button btn = (Button) findViewById(R.id.button9);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FirstPage.class);
                context.startActivity(i);
            }
        });
    }
}