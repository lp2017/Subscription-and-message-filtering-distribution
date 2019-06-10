package com.myTestDemo.notifier.example;

import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.msgfilter.CtrlNotifier;
import com.myTestDemo.notifier.msgfilter.Notify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 测试案例 router
 * @author LiuPeng
 * @version 2019/6/6 18:29
 */

public class RouterNotify extends Notify {

//    private Logger log = LogManager.getLogger(RouterNotify.class);

    private static class Inner {

        private static final CtrlNotifier INSTANCE = new RouterNotify();
    }

    public static CtrlNotifier getNotifier() {
        return Inner.INSTANCE;
    }

    /**
     * 增加Router的过滤条件，两个例子
     * 条件可为:
     * 如 Message    type 为：MessageType.NOTIFICATION
     * sender 为 String CONTROLLER = "CONTROLLER"
     * data 为 Notification  且 Notification entityType =  EntityType.ROUTER.name()
     * Notification."Object", jsonParseUtil.toJson(ctrlDbRouter));
     *
     * @return map
     */
    @Override
    protected Map<String, List<Predicate<Message>>> addFilterAndEventObserverID() {
        Map map = new HashMap<>();
        try {
            //增加过滤条件
            Predicate<Message> predicate = new Predicate<Message>() {
                @Override
                public boolean test(Message s) {

                    boolean flag = false;
                    //测试自定义
                    flag = ("NOTIFICATION".equals(s.getType()) && "CONTROLLER".equals(s.getSender()));

                    return flag;
                }
            };

/*        Predicate<Message> rule2 = s -> "OD".equals(s.getType());
        List rules = new ArrayList<>();
        rules.add(predicate);
        rules.add(rule2);*/


            //key键
            map.put("Router", predicate);

        } catch (Exception e) {

//            log.error("addFilterAndEventObserverID Exception :{}", e.getMessage());
        }
        return map;
    }
}