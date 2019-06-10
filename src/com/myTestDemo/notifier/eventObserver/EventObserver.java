package com.myTestDemo.notifier.eventObserver;

/**
 * Event 发生接口
 *
 * @author LiuPeng
 * @version 2019/6/6 15:49
 */
public interface EventObserver {

    /**
     * 消息通知
     * @param info 消息
     */
    void onEvent(Object info);

}
