package com.meishe.myvideo.edit.manager;

import java.util.ArrayDeque;

public class OperateManager<T> implements IOperateManager<T> {
    private static volatile OperateManager sOperateManager;
    ArrayDeque<T> cancelStack = new ArrayDeque<>();
    private boolean isCancelStackEmpty = true;
    private boolean isRecoverStackEmpty = false;
    T nowOperate;
    private OnOperateStateListener onOperateStateListener;
    ArrayDeque<T> recoverStack = new ArrayDeque<>();

    public interface OnOperateStateListener {
        void onCancelDataListener(boolean z);

        void onRecoverDataListener(boolean z);
    }

    private OperateManager() {
    }

    public void setOnOperateStateListener(OnOperateStateListener onOperateStateListener2) {
        this.onOperateStateListener = onOperateStateListener2;
    }

    public static OperateManager getInstance() {
        if (sOperateManager == null) {
            synchronized (OperateManager.class) {
                if (sOperateManager == null) {
                    sOperateManager = new OperateManager();
                }
            }
        }
        return sOperateManager;
    }

    @Override // com.meishe.myvideo.edit.manager.IOperateManager
    public T cancelOperate() {
        OnOperateStateListener onOperateStateListener2;
        OnOperateStateListener onOperateStateListener3;
        if (this.nowOperate == null) {
            return null;
        }
        if (this.cancelStack.isEmpty()) {
            if (this.cancelStack.isEmpty() && !this.isCancelStackEmpty) {
                this.isCancelStackEmpty = true;
                if (this.cancelStack.isEmpty() && (onOperateStateListener3 = this.onOperateStateListener) != null) {
                    onOperateStateListener3.onCancelDataListener(this.isCancelStackEmpty);
                }
            }
            return null;
        }
        this.recoverStack.push(this.nowOperate);
        if (this.isRecoverStackEmpty) {
            this.isRecoverStackEmpty = false;
            OnOperateStateListener onOperateStateListener4 = this.onOperateStateListener;
            if (onOperateStateListener4 != null) {
                onOperateStateListener4.onRecoverDataListener(this.isRecoverStackEmpty);
            }
        }
        this.nowOperate = this.cancelStack.pop();
        if (this.cancelStack.isEmpty() && !this.isCancelStackEmpty) {
            this.isCancelStackEmpty = true;
            if (this.cancelStack.isEmpty() && (onOperateStateListener2 = this.onOperateStateListener) != null) {
                onOperateStateListener2.onCancelDataListener(this.isCancelStackEmpty);
            }
        }
        return this.nowOperate;
    }

    @Override // com.meishe.myvideo.edit.manager.IOperateManager
    public T recoverOperate() {
        T t;
        if (this.recoverStack.isEmpty() || (t = this.nowOperate) == null) {
            return null;
        }
        this.cancelStack.push(t);
        if (this.isCancelStackEmpty) {
            this.isCancelStackEmpty = false;
            OnOperateStateListener onOperateStateListener2 = this.onOperateStateListener;
            if (onOperateStateListener2 != null) {
                onOperateStateListener2.onCancelDataListener(this.isCancelStackEmpty);
            }
        }
        this.nowOperate = this.recoverStack.pop();
        if (this.recoverStack.isEmpty() && !this.isRecoverStackEmpty) {
            this.isRecoverStackEmpty = true;
            OnOperateStateListener onOperateStateListener3 = this.onOperateStateListener;
            if (onOperateStateListener3 != null) {
                onOperateStateListener3.onRecoverDataListener(this.isRecoverStackEmpty);
            }
        }
        return this.nowOperate;
    }

    @Override // com.meishe.myvideo.edit.manager.IOperateManager
    public void addOperate(T t) {
        T t2 = this.nowOperate;
        if (t2 != null) {
            this.cancelStack.push(t2);
        }
        this.nowOperate = t;
        if (this.isCancelStackEmpty && !this.cancelStack.isEmpty()) {
            this.isCancelStackEmpty = false;
            OnOperateStateListener onOperateStateListener2 = this.onOperateStateListener;
            if (onOperateStateListener2 != null) {
                onOperateStateListener2.onCancelDataListener(this.isCancelStackEmpty);
            }
        }
        this.isCancelStackEmpty = this.cancelStack.isEmpty();
        this.recoverStack.clear();
        if (!this.isRecoverStackEmpty) {
            this.isRecoverStackEmpty = this.recoverStack.isEmpty();
            OnOperateStateListener onOperateStateListener3 = this.onOperateStateListener;
            if (onOperateStateListener3 != null) {
                onOperateStateListener3.onRecoverDataListener(this.isRecoverStackEmpty);
            }
        }
    }

    @Override // com.meishe.myvideo.edit.manager.IOperateManager
    public void destroy() {
        this.cancelStack.clear();
        this.recoverStack.clear();
        this.nowOperate = null;
    }

    public boolean haveOperate() {
        return this.cancelStack.size() > 0;
    }
}
