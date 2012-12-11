
package com.satton.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.satton.R;
import com.satton.util.MemoryInfoDumper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BitmapMemoryActivity extends Activity {

    ArrayList<ImageView> list = new ArrayList<ImageView>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        overridePendingTransition(R.anim.slidein_start, R.anim.slidein_end);

        try {
            InputStream is = getApplication().getAssets().open("stamp/ninja.png");
            BitmapFactory.Options options = new BitmapFactory.Options();
            //            options.inPreferredConfig = Bitmap.Config.RGB;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap b = BitmapFactory.decodeStream(is, null, options);
            saveBitmapToSd(b);

        } catch (Exception e) {
            e.printStackTrace();
        }

        load();

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout l = (RelativeLayout) findViewById(R.id.bitmap);
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
            RelativeLayout v = (RelativeLayout) findViewById(R.id.bitmap);

            long sum = 0;
            for (int i = 0; i < 40; i++) {
                String name = "stamp/1/" + i + ".png";
                InputStream is = getApplication().getAssets().open(name);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inPurgeable = true;
                Bitmap b = BitmapFactory.decodeStream(is, null, options);

                int bytes = b.getRowBytes() * b.getHeight();
                sum += bytes;
                //                System.out.println(name + " " + bytes / 1024);

                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(b);
                is.close();
                v.addView(imageView);
            }
            System.out.println("sum = " + sum);

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

    public void saveBitmapToSd(Bitmap mBitmap) {
        try {
            // sdcardフォルダを指定
            File root = new File(Environment.getExternalStorageDirectory(), "satton");
            root.mkdir();

            // 日付でファイル名を作成　
            Date mDate = new Date();
            SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");

            // 保存処理開始
            FileOutputStream fos = new FileOutputStream(new File(root, fileName.format(mDate) + ".png"));

            // jpegで保存
            mBitmap.compress(CompressFormat.PNG, 100, fos);
            // 保存処理終了
            fos.close();
        } catch (Exception e) {
            Log.e("Error", "" + e.toString());
        }
    }

}
