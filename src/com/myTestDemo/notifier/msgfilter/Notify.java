package com.myTestDemo.notifier.msgfilter;

import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.eventObserver.EventObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 消息 订阅/分发
 *
 * @author LiuPeng
 * @version 2019/6/3 18:29
 */

public class Notify implements CtrlNotifier {

    private Logger log = LogManager.getLogger(Notify.class);
    private static CtrlMsgFilterUtils filterUtils = CtrlMsgFilterUtils.getInstance();

    private static class Inner {
        private static final CtrlNotifier INSTANCE = new Notify();
    }

    public static CtrlNotifier getNotifier() {
        return Inner.INSTANCE;
    }

    @Override
    public void subscribe(String topic, String filterKey, EventObserver observer, Map<String, Predicate<Message>> filter) {

        filterUtils.addFilterAndCosumer(topic,filterKey,observer,filter);
        log.debug("Notify subscribe Ok!,  key:{}", filterKey);
    }

    @Override
    public void unSubscribe(String topic,EventObserver observer) {

        filterUtils.removeCosumer(topic,observer);
    }

    @Override
    public void unSubscribe(String key) {

        filterUtils.removeCosumer(key);
    }

    @Override
    public void unRegisterAll() {

        filterUtils.unRegisterAll();
    }

    /**
     * 分发所有
     * @param topic 分组标签
     * @param info 传递的信息
     */
    @Override
    public void post(String topic,Message info) {

        try {

            CtrlMsgFilterUtils.filterAndSend(topic,info);

        } catch (Exception e) {

            log.error("Notify post Exception：{}", e.getMessage());
        }
    }


    /**
     * 分发所有
     *
     * @param info 传递的信息
     */
    @Override
    public void postAll(Object info) {

    }

    /**
     * 根据过滤规则过滤集合
     *
     * @param msgList 数据集合
     * @param mappers 规律规则
     * @param <I>     数据类型
     * @return 过滤后的数据集合
     */
    private static <I> List<I> filter(Collection<I> msgList, List<Predicate<I>> mappers) {
        return msgList.stream()
                .filter(ele -> mappers.stream()
                        .reduce(t -> true, Predicate::and)
                        .test(ele)).collect(Collectors.toList());
    }
}
