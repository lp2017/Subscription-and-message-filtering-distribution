package com.myTestDemo.notifier.msgfilter;


import com.myTestDemo.notifier.eventObserver.EventObserver;
import com.myTestDemo.notifier.entity.Message;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * 静态缓存，保存过滤条件
 * <keyID,filter>
 *
 * @author LiuPeng
 * @version 2019/6/6 20:49
 */
public class FilterUtils {

    private static volatile FilterUtils filterlist;
    /**
     * 观察者集合
     */
    private static Map<String, HashSet<EventObserver>> mObservers;
    /**
     * 过滤条件集合
     */
    private static Map<String, Predicate<Message>> filters;

    public static final FilterUtils getInstance() {

        if (filterlist == null) {
            filterlist = new FilterUtils();
        }
        filters = new ConcurrentHashMap<>();//注过滤集合集合
        mObservers = new ConcurrentHashMap<>();//注册集合

        filters.put("Router", createRouterOCFilter());

        return filterlist;
    }


    private static Predicate<Message> createRouterOCFilter() {

        Predicate<Message> rule2 = s -> "AAA".equals(s.getSender());
        return rule2;
    }

    public static void addFilter(Map<String, Predicate<Message>> filter) {
        try {

            addFilterCheck(filter);
            filters.putAll(filter);

//            System.out.println("filters: "+filters.toString());
        } catch (Exception e) {

            System.out.println("addFilter" + e);
        }
    }

    public static void removeFilter(String key) throws Exception {

        removeCheck(key);
        filters.remove(key);
    }

    public static void addCosumer(String key, EventObserver observer) {
        try {

            System.out.println("addCosumer。。。。。");
            checkObserver(observer);

            HashSet<EventObserver> set = mObservers.get(key);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(observer);
            mObservers.put(key, set);


            System.out.println("subscribe : " + mObservers.size());
        } catch (Exception e) {

            System.out.println("addCosumer " + e);
        }

    }

    public static void removeCosumer(EventObserver observer) {
        try {

            System.out.println("removeCosumer。。。。。");
            checkObserver(observer);

            for (Map.Entry<String, HashSet<EventObserver>> entry : mObservers.entrySet()) {
                HashSet<EventObserver> set = entry.getValue();
                if (set.remove(observer)) {
                    break;
                }
            }
            System.out.println("removeCosumer : " + mObservers.size());
        } catch (Exception e) {

            System.out.println("removeCosumer " + e);
        }

    }

    public static void removeCosumer(String key) {
        try {

            System.out.println("removeCosumer。。。。。");
            mObservers.remove(key);
            System.out.println("removeCosumer : " + mObservers.size());
        } catch (Exception e) {

            System.out.println("removeCosumer " + e);
        }
    }

    public static void unRegisterAll(){
        mObservers.clear();
    }

    public static HashSet<EventObserver> getMobservers(String key) {
        try {

            HashSet<EventObserver> observers = mObservers.get(key);

            return observers;

        } catch (Exception e) {

            System.out.println("getMobservers" + e);
        }
        return null;
    }

    public static Map<String, HashSet<EventObserver>> getMobservers() {

        return mObservers;

    }

    public static Map<String, Predicate<Message>> getFilters() {
        try {

            return filters;

        } catch (Exception e) {

            System.out.println("getFilters" + e);
        }
        return null;
    }

    private static void addFilterCheck(Map<String, Predicate<Message>> filter) throws Exception {

        if (filter == null || filter.isEmpty()) {

            throw new Exception("addFilter key or filter cannot be null");

        }

    }

    private static void removeCheck(String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new Exception("removeCheck key cannot be null");
        }
    }

    private static void checkObserver(EventObserver observer) throws Exception {

        if (observer == null) {
            throw new Exception("observer should not be null!");
        }
    }

    private static void checkRemoveObserver(String key, EventObserver observer) throws Exception {

        checkObserver(observer);

        HashSet<EventObserver> set = mObservers.get(key);
        if (set == null || set.isEmpty()) {
            throw new Exception("checkRemoveObserver HashSet isEmpty!");
        }
    }

}
