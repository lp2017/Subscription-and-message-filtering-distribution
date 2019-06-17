package com.myTestDemo.notifier.msgfilter;


import com.myTestDemo.notifier.entity.CtrlNotifyEntity;

import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.eventObserver.EventObserver;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * 静态缓存，
 * 保存过滤条件及其id，对应消费者及其过滤条件id
 */
public class CtrlMsgFilterUtils {
    private static Logger log = LogManager.getLogger(CtrlMsgFilterUtils.class);
    private static volatile CtrlMsgFilterUtils ctrlMsgFilterUtils;
    /**
     * 观察者和过滤条件集合
     */
    private static List<CtrlNotifyEntity> notifyEntityList;

    public static CtrlMsgFilterUtils getInstance() {

        if (ctrlMsgFilterUtils == null) {

            ctrlMsgFilterUtils = new CtrlMsgFilterUtils();
        }

        notifyEntityList = new CopyOnWriteArrayList<CtrlNotifyEntity>();

        return ctrlMsgFilterUtils;
    }


    private static Predicate<Message> createRouterOCFilter() {

        Predicate<Message> rule2 = s -> "AAA".equals(s.getSender());
        return rule2;
    }

    public static void addFilterAndCosumer(String topic, String filterKey, EventObserver observer, Map<String, Predicate<Message>> filter) {
        try {

            addFilterAndObserverCheck(topic, filter, observer);

            CtrlNotifyEntity entity = getCtrlNotifyEntityByTopic(topic);

            if (null == entity) {

                CtrlNotifyEntity ctrlNotifyEntity = new CtrlNotifyEntity(topic, filterKey, observer, filter);

                notifyEntityList.add(ctrlNotifyEntity);

            } else {

                entity.addFilterAndEventObserver(filterKey, observer, filter);
            }


        } catch (Exception e) {

            System.out.println("addFilterAndCosumer" + e.getMessage());
        }
    }

    public static void addCosumer(String topic, String filterKey, EventObserver observer) {
        try {

            checkObserver(topic, observer);
            CtrlNotifyEntity notifyEntity = getCtrlNotifyEntityByTopic(topic);

            if (notifyEntity != null) {

                notifyEntity.addEventObserver(filterKey, observer);
            } else {
                log.info("addCosumer fail!!!,notifyEntityList dont has topic：{}", topic);
            }


        } catch (Exception e) {

            System.out.println("addCosumer " + e);
        }
    }

    private static CtrlNotifyEntity getCtrlNotifyEntityByTopic(String topic) {

        CtrlNotifyEntity notifyEntity = null;


        for (CtrlNotifyEntity entity : notifyEntityList) {
            if (entity.getTopic().equals(topic)) {
                notifyEntity = entity;
                break;
            }
        }
        return notifyEntity;
    }

    public static void removeFilter(String topic, String key) throws Exception {

        removeCheck(key);
        CtrlNotifyEntity notifyEntity = getCtrlNotifyEntityByTopic(topic);

        notifyEntity.removeFilter(key);
    }


    public static void getMobserversAndSend(CtrlNotifyEntity notifyEntity, String filterKey, Object info) {
        try {

            HashSet<EventObserver> observers = notifyEntity.getEventObserverMap().get(filterKey);

            if (observers != null) {
                //分发
                for (EventObserver observer : observers) {

                    observer.onEvent(info);
                }
            }

        } catch (Exception e) {
            log.error("getMobserversAndSend" + e.getMessage());
        }
    }

    public static Map<String, Predicate<Message>> getFilters(String topic) {
        try {

            for (CtrlNotifyEntity notifyEntity : notifyEntityList) {

                if (notifyEntity.getTopic().equals(topic)) {

                    return notifyEntity.getFilters();
                }
            }

        } catch (Exception e) {

            System.out.println("getFilters" + e);
        }
        return null;
    }

    public static void filterAndSend(String topic, Message info) {

        CtrlNotifyEntity notifyEntity = getCtrlNotifyEntityByTopic(topic);

        if (notifyEntity ==null || null == notifyEntity.getFilters() || notifyEntity.getFilters().isEmpty()) {
            log.debug("filterAndSend, CtrlNotifyEntity or getFilters is null , break! topic{}", topic);
            return;
        }

        Map<String, Predicate<Message>> filter = notifyEntity.getFilters();

        log.debug("post getFilters infos：{}", filter);

        for (Map.Entry entry : filter.entrySet()) {

            Predicate<Message> a = (Predicate<Message>) entry.getValue();

            if (a.test(info)) {

                log.debug("filters OK,send msg To:" + entry.getKey().toString());

                getMobserversAndSend(notifyEntity, entry.getKey().toString(), info);

            }
        }

    }

    public static Boolean isEmpty(String topic) {
        try {

            if (notifyEntityList.isEmpty()) {
                return true;
            }

            for (CtrlNotifyEntity notifyEntity : notifyEntityList) {

                if (notifyEntity.getTopic().equals(topic)) {

                    return false;
                }
            }
            return true;

        } catch (Exception e) {

            System.out.println("getFilters" + e);
        }
        return false;
    }

    public static void addFilterCheck(Map<String, Predicate<Message>> filter) throws Exception {

        if (filter == null || filter.isEmpty()) {

            throw new Exception("addFilter key or filter cannot be null");
        }
    }

    public static void addFilterAndObserverCheck(String topic, Map<String, Predicate<Message>> filter, EventObserver observer) throws Exception {

        if (StringUtils.isEmpty(topic)) {
            throw new Exception("addFilterAndObserverCheck topic cannot be null");
        }
        if (filter == null || filter.isEmpty()) {

            throw new Exception("addFilterAndObserverCheck key or filter cannot be null");
        }
        if (observer == null) {
            throw new IllegalArgumentException("addFilterAndObserverCheck observer should not be null!");
        }
    }

    public static void removeCheck(String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new Exception("removeCheck key cannot be null");
        }
    }

    public static void checkObserver(String topic, EventObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("observer should not be null!");
        }
        if (StringUtils.isEmpty(topic)) {
            throw new IllegalArgumentException("topic should not be null!");
        }
    }

    public static void removeCosumer(String topic, EventObserver observer) {
        try {

            checkObserver(topic, observer);

            Map<String, HashSet<EventObserver>> mObservers = getCtrlNotifyEntityByTopic(topic).getEventObserverMap();

            for (Map.Entry<String, HashSet<EventObserver>> entry : mObservers.entrySet()) {

                HashSet<EventObserver> set = entry.getValue();
                if (set.remove(observer)) {
                    break;
                }
            }
        } catch (Exception e) {

            log.error("removeCosumer  Exception" + e);
        }

    }

    public static void removeCosumer(String key) {
//        try {
//
//            mObservers.remove(key);
//
//        } catch (Exception e) {
//            log.error("removeCosumer  Exception" + e);
//        }
    }


    public static void unRegisterAll() {

        //本版本不提供这个
    }

    public static Map<String, HashSet<EventObserver>> getMobservers() {

        try {
            Map<String, HashSet<EventObserver>> mobservers = new HashMap<>();

            for (CtrlNotifyEntity notifyEntity : notifyEntityList) {
                mobservers.putAll(notifyEntity.getEventObserverMap());
            }

            return mobservers;

        } catch (Exception e) {

            log.error("getMobservers Exception:{}", e.getMessage());
        }

        return null;

    }
}
