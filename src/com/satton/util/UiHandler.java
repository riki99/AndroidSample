
package com.satton.util;

import android.os.Handler;
import android.os.Looper;

/**
 * AndroidのHandlerクラスのutil
 * 
 * @author tanabe.satoru 2013/02/19
 * @since 2013/02/19
 * @version 2013/02/19
 */
public class UiHandler extends Handler implements Runnable {

    public UiHandler() {
        super(Looper.getMainLooper());
    }

    public UiHandler(Handler.Callback callback) {
        super(Looper.getMainLooper(), callback);
    }

    public boolean post() {
        return post(this);
    }

    public boolean postAtFrontOfQueue() {
        return postAtFrontOfQueue(this);
    }

    public boolean postAtTime(Object token, long uptimeMillis) {
        return postAtTime(this, token, uptimeMillis);
    }

    public boolean postAtTime(long uptimeMillis) {
        return postAtTime(this, uptimeMillis);
    }

    public boolean postDelayed(long delayMillis) {
        return postDelayed(this, delayMillis);
    }

    @Override
    public void run() {

    }

}
