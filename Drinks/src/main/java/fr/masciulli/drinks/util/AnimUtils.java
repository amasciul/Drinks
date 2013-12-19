package fr.masciulli.drinks.util;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class AnimUtils {
    public static void scheduleEndAction(final Runnable endAction, long duration) {
        Timer timer = new Timer();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                endAction.run();
            }
        };
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }
        };
        timer.schedule(task, duration);
    }
}
