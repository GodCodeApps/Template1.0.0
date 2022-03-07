package com.meishe.engine.draft;

import java.util.Stack;

public class OperateManagerEx {
    private static volatile OperateManagerEx sDraftManager;
    private Stack<OperateData> mStack = new Stack<>();

    private OperateManagerEx() {
    }

    public static OperateManagerEx getInstanse() {
        if (sDraftManager == null) {
            synchronized (OperateManagerEx.class) {
                if (sDraftManager == null) {
                    sDraftManager = new OperateManagerEx();
                }
            }
        }
        return sDraftManager;
    }

    public void saveTimeline(String str, String str2) {
        this.mStack.push(new OperateData(str, str2));
    }

    public OperateData undo() {
        if (this.mStack.isEmpty()) {
            return null;
        }
        return this.mStack.pop();
    }
}
