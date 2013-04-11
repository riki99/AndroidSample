
package com.satton.overlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SystemOverlayLayerActivity extends Activity {
    Button start_button;
    Button stop_button;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(SystemOverlayLayerActivity.this, LayerService.class));

    }
}
