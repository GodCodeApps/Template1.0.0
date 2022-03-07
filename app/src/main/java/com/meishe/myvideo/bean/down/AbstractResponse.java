package com.meishe.myvideo.bean.down;

public class AbstractResponse {
    public int code;
    public int errNo;
    public String msg;

    public String toString() {
        return "AbstractResponse{code=" + this.code + ", msg='" + this.msg + '\'' + ", errNo=" + this.errNo + '}';
    }
}
