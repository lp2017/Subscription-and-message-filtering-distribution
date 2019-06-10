package com.myTestDemo.notifier.msgfilter;

import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.eventObserver.EventObserver;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 消息 订阅/分发 具体实现类
 *
 * @author LiuPeng
 * @version 2019/6/6 18:29
 */

public abstract class Notify implements CtrlNotifier {

    private static FilterUtils filter = FilterUtils.getInstance();

    @Override
    public void subscribe(String key, EventObserver observer) {

        filter.addFilter(addFilterAndEventObserverID());
        filter.addCosumer(key, observer);

    }

    @Override
    public void subscribeAndFilter(String key, List<String> filterID, EventObserver observer) {

    }

    @Override
    public void unSubscribe(EventObserver observer) {

        filter.removeCosumer(observer);
    }

    @Override
    public void unSubscribe(String key) {

        filter.removeCosumer(key);
    }

    @Override
    public void unRegisterAll() {

        filter.unRegisterAll();
    }


    @Override
    public void post(Object info) {
        try {

            Map<String, Predicate<Message>> filters = filter.getFilters();
            if (null == filters) {
                return;
            }

            for (Map.Entry entry : filters.entrySet()) {

                Predicate<Message> a = (Predicate<Message>) entry.getValue();

                Message msg = (Message) info;

                if (a.test(msg)) {
//                    System.out.println("检验通过，准备通知给" + entry.getKey().toString());

                    getMobservers(entry.getKey().toString(), info);
                } else {
//                    System.out.println("检查不通过，抛弃" + msg.getType());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void getMobservers(String key, Object info) {

        HashSet<EventObserver> observers = filter.getMobservers(key);
        if (observers != null) {
            System.out.println("Observers.size：" + observers.size());
            for (EventObserver observer : observers) {
                observer.onEvent(info);
            }
        }
    }


    /**
     * 分发所有
     *
     * @param info 传递的信息
     */
    @Override
    public void postAll(Object info) {

        for (Map.Entry<String, HashSet<EventObserver>> entry : filter.getMobservers().entrySet()) {
            for (EventObserver observer : entry.getValue()) {
                observer.onEvent(info);
            }
        }
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

    /**
     * （新增）消费者id与过滤条件id绑定
     */
    protected abstract Map addFilterAndEventObserverID();

    private void checkKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key should not be null!");
        }
    }


    private void check(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key should not be null!");
        }
    }
}
