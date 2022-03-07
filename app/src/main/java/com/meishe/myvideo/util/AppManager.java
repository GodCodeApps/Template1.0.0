package com.meishe.myvideo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.widget.Toast;
import java.util.Iterator;
import java.util.Stack;

public class AppManager {
    private static Stack<Activity> activityStack = new Stack<>();
    private static volatile AppManager instance = new AppManager();
    private long exitTime = 0;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    public void finishActivity() {
        if (!activityStack.empty()) {
            finishActivity(activityStack.lastElement());
        }
    }

    public void finishActivityByCallBack(Intent intent, Activity activity, Bundle bundle) {
        intent.putExtras(bundle);
        activity.setResult(-1, intent);
        getInstance().finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void finishActivity(Class<?> cls) {
        Iterator<Activity> it = activityStack.iterator();
        while (it.hasNext()) {
            Activity next = it.next();
            if (next.getClass().equals(cls)) {
                finishActivity(next);
            }
        }
    }

    public void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (activityStack.get(i) != null) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public int activietySizes() {
        Stack<Activity> stack = activityStack;
        if (stack != null) {
            return stack.size();
        }
        return 0;
    }

    public void jumpActivity(Activity activity, Class<? extends Activity> cls, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    public void jumpActivity(Activity activity, Class<? extends Activity> cls) {
        jumpActivity(activity, cls, null);
    }

    public void jumpActivityForResult(Activity activity, Class<? extends Activity> cls, Bundle bundle, int i) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, i);
    }

    public int getActivitySize() {
        Stack<Activity> stack = activityStack;
        if (stack != null) {
            return stack.size();
        }
        return 0;
    }

    public void safetyExitApp(Context context) {
        if (System.currentTimeMillis() - this.exitTime > 2000) {
            Toast.makeText(context.getApplicationContext(), "再按一次退出程序", 0).show();
            this.exitTime = System.currentTimeMillis();
            return;
        }
        try {
            finishAllActivity();
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception unused) {
        }
    }
}
