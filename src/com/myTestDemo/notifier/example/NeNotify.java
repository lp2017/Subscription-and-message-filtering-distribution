package com.myTestDemo.notifier.example;

import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.msgfilter.CtrlNotifier;
import com.myTestDemo.notifier.msgfilter.Notify;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 使用案例
 * Ne注册订阅 且自定义接收过滤
 *
 * @author LiuPeng
 * @version 2019/6/6 20:50
 */

public class NeNotify extends Notify {

    private NeNotify() {
    }

    private static class Inner {
        private static final CtrlNotifier INSTANCE = new NeNotify();
    }

    public static CtrlNotifier getNotifier() {
        return Inner.INSTANCE;
    }

    @Override
    protected Map addFilterAndEventObserverID() {


        Predicate<Message> predicate = new Predicate<Message>() {
            @Override
            public boolean test(Message s) {
                boolean flag = false;
                flag = ("OC".equals(s.getType()) && "sender".equals(s.getSender()));
//                System.out.println(s.getType());
                return flag;
            }
        };

//        Predicate<Message> rule2 = s -> "OD".equals(s.getType());

//        List rules = new ArrayList<>();
//        rules.add(predicate);
//        rules.add(rule2);

        Map map = new HashMap<>();
        map.put("NE", predicate);
        return map;
    }

}
