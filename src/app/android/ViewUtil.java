package app.android;

import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import app.util.RuntimeUtils;

public class ViewUtil {

	public static void fadeIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(500);
		view.startAnimation(anim);
		view.setVisibility(View.VISIBLE);
	}

	public static void fadeOut(final Handler mHandler, final View view) {
		final Runnable run = new Runnable() {
			public void run() {
				if (view.hasFocus()) {
					return;
				}
				AlphaAnimation anim = new AlphaAnimation(1.0f,0.0f);
				anim.setDuration(500);
				view.startAnimation(anim);
				view.setVisibility(View.INVISIBLE);
			}
		};
		mHandler.removeCallbacks(run);
		mHandler.postDelayed(run, 5000);
	}

}
