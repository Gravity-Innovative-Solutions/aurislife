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

import in.gravitykerala.aurislife.FirstPage;

import in.gravitykerala.aurislife.PrefUtils;
import in.gravitykerala.aurislife.R;
import in.gravitykerala.aurislife.SplashPage;

public class NoCompulsaryUpdate extends Dialog {
    public NoCompulsaryUpdate(Context context) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.activity_no_compulsary_update);
        show();
    }
}
