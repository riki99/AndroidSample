package com.satton.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.satton.R;

public class BalloneActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		Window win = getWindow();
		win.addFlags(
					+ WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
		            + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		            + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		            + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_ballone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ballone, menu);
        return true;
    }
    
    public static void showBallone(Activity mainActivity) {
		Intent i = new Intent(mainActivity, BalloneActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mainActivity.startActivity(i);
	}
    
    public void close(View v) {
    	super.onDestroy();
    }
    
    public void startTalkActivity(View v) {
//		Intent i = new Intent(getBaseContext(), TalkActivity.class);
//		i.putExtra("talk_room_id", v.getTag().toString());
//		startActivity(i);
    }
    
}
