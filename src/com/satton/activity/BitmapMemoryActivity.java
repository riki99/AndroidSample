
package com.satton.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.satton.R;
import com.satton.util.MemoryInfoDumper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BitmapMemoryActivity extends Activity {

    ArrayList<ImageView> list = new ArrayList<ImageView>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        overridePendingTransition(R.anim.slidein_start, R.anim.slidein_end);

        load();

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout l = (LinearLayout) findViewById(R.id.bitmap);
                for (int i = 0; i < l.getChildCount(); i++) {
                    ImageView view = (ImageView) l.getChildAt(i);
                    recycle(view);
                }
                l.removeAllViews();
                load();
            }
        });

    }

    private void load() {
        try {
            LinearLayout v = (LinearLayout) findViewById(R.id.bitmap);

            for (int i = 0; i < 40; i++) {
                InputStream is = getApplication().getAssets().open(
                        "stamp/1/" + i + ".png");

                BitmapFactory.Options options = new BitmapFactory.Options();
                //                options.inPreferredConfig = Bitmap.Config.RGB_565;
                //                options.inPurgeable = true;
                Bitmap b = BitmapFactory.decodeStream(is, null, options);

                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(b);
                is.close();

                v.addView(imageView);
            }
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        Log.i("MemoryActivity", MemoryInfoDumper.getNativeHeapInfo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static void recycle(ImageView view) {
        Drawable toRecycle = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) toRecycle).getBitmap();

        view.setImageDrawable(null);

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slideout_start, R.anim.slideout_end);
        return;
    }

}
