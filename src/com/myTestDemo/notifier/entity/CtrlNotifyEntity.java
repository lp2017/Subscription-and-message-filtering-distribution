package com.myTestDemo.notifier.entity;

import com.myTestDemo.notifier.eventObserver.EventObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class CtrlNotifyEntity {
    private static Logger log = LogManager.getLogger(CtrlNotifyEntity.class);
    private String topic;
    private Map<String, HashSet<EventObserver>> eventObserverMap;
    private Map<String, Predicate<Message>> filters;

    public CtrlNotifyEntity(String topic, String filterKey, EventObserver eventObserver, Map<String, Predicate<Message>> filterList){
        this.topic = topic;
        this.filters = filterList;
        addEventObserver(filterKey,eventObserver);
    }
    public CtrlNotifyEntity(){
    }
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Map<String, HashSet<EventObserver>> getEventObserverMap() {
        if(null == this.eventObserverMap){

            this.eventObserverMap = new ConcurrentHashMap<>();//注册集合
        }
        return this.eventObserverMap;
    }

    public void setEventObserverMap(Map<String, HashSet<EventObserver>> eventObserverList) {
        this.eventObserverMap = eventObserverList;
    }

    public Map<String, Predicate<Message>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Predicate<Message>> filters) {
        this.filters = filters;
    }


    /**
     * addFilterAndEventObserver
     * @param filterKey
     * @param eventObserver
     * @param filterList
     */
    public void addFilterAndEventObserver(String filterKey, EventObserver eventObserver,Map<String, Predicate<Message>> filterList) {
        addFilter(filterList);
        addEventObserver(filterKey,eventObserver);
    }

    public void addEventObserver(String filterKey, EventObserver eventObserver) {

        if (null == this.eventObserverMap) {

            this.eventObserverMap = new ConcurrentHashMap<>();//注册集合
        }
        HashSet<EventObserver> set = eventObserverMap.get(filterKey);

        if (set == null) {
            set = new HashSet<>();
        }

        set.add(eventObserver);

        eventObserverMap.put(filterKey, set);
    }

    public void addFilter(Map<String, Predicate<Message>> filterList) {
        if (null == this.filters) {
            filters = new ConcurrentHashMap<>();//注过滤集合集合
        }
        filters.putAll(filterList);
    }

    public void removeFilter(String key) {
        try {
//            CtrlMsgFilterUtils.removeCheck(key);
            filters.remove(key);
        } catch (Exception e) {
//            log.error("removeFilter,key{},Exception{}", key, e.getMessage());
        }

    }
}
