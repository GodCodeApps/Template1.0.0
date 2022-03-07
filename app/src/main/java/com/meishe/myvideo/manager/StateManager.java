package com.meishe.myvideo.manager;

import com.meishe.myvideo.util.Constants;
import java.util.ArrayList;
import java.util.Iterator;

public class StateManager {
    public static final int FROM_TYPE_CLICK_BOTTOM_MENU_VIEW = 1;
    public static final int FROM_TYPE_CLICK_MULTI_VIEW = 2;
    public static final int FROM_TYPE_CLICK_OTHER_VIEW = 3;
    private static StateManager mStateManager;
    private String mCurrentState = Constants.STATE_MAIN_MENU;
    private ArrayList<OnStateChangeListener> mOnStateChangeListenerList = new ArrayList<>();

    public interface OnStateChangeListener {
        void onChangeState(String str, int i);
    }

    public static StateManager getInstance() {
        if (mStateManager == null) {
            mStateManager = new StateManager();
        }
        return mStateManager;
    }

    private StateManager() {
    }

    public void changeState(String str, int i) {
        this.mCurrentState = str;
        Iterator<OnStateChangeListener> it = this.mOnStateChangeListenerList.iterator();
        while (it.hasNext()) {
            it.next().onChangeState(str, i);
        }
    }

    public String getCurrentState() {
        return this.mCurrentState;
    }

    public void setCurrentState(String str) {
        this.mCurrentState = str;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListenerList.add(onStateChangeListener);
    }
}
