package jp.typosone.android.azarashi;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import twitter4j.Status;

/**
 * Created by tsuyoshi on 2013/09/09.
 */
public class UserStreamAdapter extends twitter4j.UserStreamAdapter {
    private Handler mHandler;

    public UserStreamAdapter(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onStatus(Status status) {
        super.onStatus(status);

        Message msg = new Message();
        msg.obj = status;
        mHandler.sendMessage(msg);
    }
}
