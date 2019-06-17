package com.myTestDemo.notifier.msgfilter;

import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.eventObserver.EventObserver;

import java.util.Map;
import java.util.function.Predicate;

/**
 * 订阅/分发 功能接口
 */
public interface CtrlNotifier {

//    /**
//     * 订阅-注册观察者
//     *
//     * @param key      用于标识observer，不能为null
//     * @param observer 注册对应的observer接口，不能为null
//     */
//    void subscribe(String key, EventObserver observer);

    /**
     * 订阅-注册观察者
     *
     * @param topic    分组
     * @param key      用于标识observer，不能为null
     * @param filter   自定义消息过滤条件，不能为null
     * @param observer 注册对应的observer接口，不能为null
     */
    void subscribe(String topic, String key, EventObserver observer, Map<String, Predicate<Message>> filter);


    /**
     * 取消订阅-注销观察者
     *
     * @param observer 注销对应的observer接口，不能为null
     */
    void unSubscribe(String topic, EventObserver observer);

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
     * @param topic 分组
     * @param info  传递的信息
     */
    void post(String topic, Message info);

    /**
     * 所有observer
     *
     * @param info 传递的信息
     */
    void postAll(Object info);

}
