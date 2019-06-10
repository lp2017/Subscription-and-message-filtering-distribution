package com.myTestDemo.notifier.msgfilter;

import com.myTestDemo.notifier.eventObserver.EventObserver;

import java.util.List;

/**
 * 订阅/分发 功能接口
 *
 * @author
 * @version 2019/6/6 20:49
 */
public interface CtrlNotifier {

    /**
     * 订阅-注册观察者
     *
     * @param key      用于标识observer，不能为null
     * @param observer 注册对应的observer接口，不能为null
     */
    void subscribe(String key, EventObserver observer);

    /**
     * 订阅-注册观察者
     *
     * @param key      用于标识observer，不能为null
     * @param observer 注册对应的observer接口，不能为null
     */
    void subscribeAndFilter(String key, List<String> filterID, EventObserver observer);


    /**
     * 取消订阅-注销观察者
     *
     * @param observer 注销对应的observer接口，不能为null
     */
    void unSubscribe(EventObserver observer);

    /**
     * 取消订阅-注销观察者
     *
     * @param key 按key值注销，不能为null
     */
    void unSubscribe(String key);

    /**
     * 取消订阅-注销所有观察者
     */
    void unRegisterAll();

    /**
     * 分发-按key分发
     *
     * @param info 传递的信息
     */
    void post(Object info);

    /**
     * 分发-所有observer
     *
     * @param info 传递的信息
     */
    void postAll(Object info);
}
