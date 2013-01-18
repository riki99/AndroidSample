
package com.satton.activity;

import com.satton.R;

import android.app.Activity;
import android.os.Bundle;

public class NoAnimeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balloon_dialog);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(0, 0);
    }

}
