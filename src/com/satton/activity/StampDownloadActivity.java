package com.satton.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.satton.R;

public class StampDownloadActivity extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.satton.R.layout.stamp_download);


		Button b = (Button) findViewById(R.id.stamp_download_button);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				findViewById(R.id.stamp_download_button).setVisibility(View.GONE);
				findViewById(R.id.stamp_progress_layout).setVisibility(View.VISIBLE);

		        final ProgressBar bar = (ProgressBar)findViewById(R.id.stamp_progressBar);
		        bar.setMax(100);

		        new Thread() {
					public void run() {
						for (int i = 0; i < 10; i++) {
							bar.setProgress(i * 10);
							try {
								sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}
				}.start();

			}
		});


	}



}
