
package com.satton.activity;

import com.satton.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewDialog extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.webview_dialog);

        webView = (WebView) findViewById(R.id.webview_dialog);
        webView.setVerticalScrollbarOverlay(true);
        webView.setHorizontalScrollbarOverlay(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginsEnabled(true);

        // この設定が無いと遅い。
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCacheMaxSize(16 * 1024 * 1024);

        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.mbga.jp/");
    }
}
