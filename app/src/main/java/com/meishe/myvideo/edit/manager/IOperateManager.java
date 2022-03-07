package com.meishe.myvideo.edit.manager;

public interface IOperateManager<E> {
    void addOperate(E e);

    E cancelOperate();

    void destroy();

    E recoverOperate();
}
