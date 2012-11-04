package com.satton.activity;

import com.satton.R;
import com.satton.R.layout;
import com.satton.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;

public class ImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image, menu);

        ((Button)findViewById(R.id.button)).setBackgroundDrawable(getResources().getDrawable(R.drawable.icon));
        return true;
    }
}