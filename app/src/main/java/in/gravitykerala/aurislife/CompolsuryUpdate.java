package in.gravitykerala.aurislife;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import in.gravitykerala.aurislife.R;

public class CompolsuryUpdate extends Dialog {
    public CompolsuryUpdate(Context context) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.activity_compolsury_update);
        show();
    }
}