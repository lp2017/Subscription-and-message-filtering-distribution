package com.myTestDemo.notifier;

import com.myTestDemo.notifier.eventObserver.EventObserver;
import com.myTestDemo.notifier.msgfilter.CtrlNotifier;
import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.msgfilter.Notify;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * MainTest
 *
 * @author LiuPeng
 * @version 2017/11/4 20:49
 */
public class MainTest {
    public static String topic1 = "internal";
    public static String topic2 = "internal2";
    public static void main(String[] args) {
//        predicate();
        myFilter();
    }

    /**
     * predicate Test
     * add by LiuPeng
     */
    private static void predicate() {
        //        MainTest predicateTestOne = new MainTest();

        Predicate<Message> predicate = new Predicate<Message>() {
            @Override
            public boolean test(Message s) {
                boolean flag = false;
                flag = ("OC".equals(s.getType()) && "AAA".equals(s.getSender()));
                System.out.println(s.getType());
                return flag;
            }
        };

        Message msg = new Message();
        msg.setType("OC");
        msg.setSender("sender");

        Message msg2 = new Message();
        msg2.setType("OC");
//        msg2.setSender("sender");

        Message msg3 = new Message();
        msg3.setType("OC");
        msg3.setSender("AAA");

        System.out.println(predicate.test(msg));
        System.out.println("--- --- --- --- --- ---");
        System.out.println(predicate.test(msg2));
        System.out.println("--- --- --- --- --- ---");
        System.out.println(predicate.test(msg3));
    }

    private static void myFilter() {

        TestOne();//正常发送测试

        TestTwo();//不同分组不同订阅者测试


    }

    private static Map addFilterAndEventObserverID() {


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

    private static void TestTwo() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(4000);
                    System.out.println("NeNotify subscribe....");
                    CtrlNotifier notifier = Notify.getNotifier();
                    notifier.subscribe(topic2, "NE", new EventObserver() {
                        @Override
                        public void onEvent(Object info) {
                            Message msg = (Message) info;

                            System.out.println("NeNotify NE new1 info:" + msg.toString());
                        }
                    }, addFilterAndEventObserverID());
                    notifier.subscribe(topic1, "NE", new EventObserver() {
                        @Override
                        public void onEvent(Object info) {
                            Message msg = (Message) info;

                            System.out.println("NeNotify NE new2 info:" + msg.toString());
                        }
                    }, addFilterAndEventObserverID());
                    notifier.subscribe(topic1, "NE", new EventObserver() {
                        @Override
                        public void onEvent(Object info) {
                            Message msg = (Message) info;

                            System.out.println("NeNotify NE new3 info:" + msg.toString());
                        }
                    }, addFilterAndEventObserverID());

                    Thread.sleep(4000);

                    System.out.println("unSubscribe!!!!!! By Key ");
                    Notify.getNotifier().unSubscribe("NE");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                System.out.println("-----------------------> " + i);
                Message msg = new Message();
                msg.setType("OC");
                msg.setSender("sender");
                msg.setMsg("test_num_" + i);
                if(i%2==0){
                    Notify.getNotifier().post(topic1,msg);
                }else {
                    Notify.getNotifier().post(topic2,msg);
                }

                System.out.println("----------------------->");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private static void TestOne() {
        CtrlNotifier notifier = Notify.getNotifier();
        notifier.subscribe(topic1, "NE", new EventObserver() {
            @Override
            public void onEvent(Object info) {
                Message msg = new Message();
                msg = (Message) info;
                System.out.println("NeNotify NE info:" + msg.toString());
            }
        }, addFilterAndEventObserverID());


        CtrlNotifier notifier2 = Notify.getNotifier();
        notifier2.subscribe(topic1, "Router", new EventObserver() {
            @Override
            public void onEvent(Object info) {
                Message msg = new Message();
                msg = (Message) info;
                System.out.println("RouterNotify Router info:" + msg.toString());
            }
        }, addFilterAndEventObserverID());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Message msg = new Message();
                    msg.setType("OC");
                    msg.setSender("sender");
                    msg.setMsg("test msg");

                    Message msg2 = new Message();
                    msg2.setType("OC");
//                    msg2.setSender("sender");
                    msg2.setMsg("test msg2");

                    Message msg3 = new Message();
                    msg3.setType("NOTIFICATION");
                    msg3.setSender("CONTROLLER");
                    msg3.setMsg("test msg3");


                    Notify.getNotifier().post(topic1, msg);
                    System.out.println("--------------------");

                    Notify.getNotifier().post(topic1, msg2);
                    System.out.println("--------------------");

                    Notify.getNotifier().post(topic1, msg3);
                    System.out.println("--------------------");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
