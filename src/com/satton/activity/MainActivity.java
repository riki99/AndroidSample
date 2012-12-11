
package com.satton.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.satton.R;
import com.satton.sample.screenlockenable.ScreenStateService;

public class MainActivity extends Activity {
	public static MainActivity i;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		i = this;

		@SuppressWarnings("rawtypes")
		Class[] as = {
				 PopupNotificationActivity.class , BitmapMemoryActivity.class,};
		for (final Class<Activity> c : as) {
			Button b = new Button(this);
			b.setText(c.getSimpleName());
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, c));
				}
			});

			LinearLayout v = (LinearLayout) findViewById(R.id.lay);
			v.addView(b);
		}

		try {




		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------

	public void imageBtn(View v) {
		startActivity(new Intent(this, BitmapMemoryActivity.class));
	}

	public void showDialog(View v) {
		showDialog();
	}

	public void showDialog2(View v) {
		startActivity(new Intent(this, PopupNotificationActivity.class));
	}

	public void startService(View v) {
		ScreenStateService.start();
	}

	public static void showDialog() {
		LayoutInflater inflater = i.getLayoutInflater();
		View view = inflater.inflate(R.layout.balloon_dialog, null);

		Drawable bitmapDrawable = i.getResources().getDrawable(
				R.drawable.icon_balloon);
		bitmapDrawable.setBounds(32, 32, 32, 32);

		// ダイアログを作成します。
		final AlertDialog dlg = new AlertDialog.Builder(i)
				.setIcon(R.drawable.icon_balloon).setTitle("太郎さん")
				.setView(view)
				.setPositiveButton("表示", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setNegativeButton("閉じる", null).create();

		dlg.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						+ WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						+ WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						+ WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		dlg.show();
	}

}
