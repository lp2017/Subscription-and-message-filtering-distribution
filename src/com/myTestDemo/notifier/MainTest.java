package com.myTestDemo.notifier;

import com.myTestDemo.notifier.eventObserver.EventObserver;
import com.myTestDemo.notifier.msgfilter.CtrlNotifier;
import com.myTestDemo.notifier.entity.Message;
import com.myTestDemo.notifier.example.NeNotify;
import com.myTestDemo.notifier.example.RouterNotify;

import java.util.function.Predicate;

/**
 * MainTest
 *
 * @author LiuPeng
 * @version 2017/11/4 20:49
 */
public class MainTest {

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

        TestOne();

        TestTwo();


    }

    private static void TestTwo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(4000);
                    System.out.println("NeNotify subscribe....");
                    CtrlNotifier notifier = NeNotify.getNotifier();
                    notifier.subscribe("NE", new EventObserver() {
                        @Override
                        public void onEvent(Object info) {
                            Message msg = (Message) info;

                            System.out.println("NeNotify NE new1 info:" + msg.toString());
                        }
                    });
                    notifier.subscribe("NE", new EventObserver() {
                        @Override
                        public void onEvent(Object info) {
                            Message msg = (Message) info;

                            System.out.println("NeNotify NE new2 info:" + msg.toString());
                        }
                    });
                    notifier.subscribe("NE", new EventObserver() {
                        @Override
                        public void onEvent(Object info) {
                            Message msg = (Message) info;

                            System.out.println("NeNotify NE new3 info:" + msg.toString());
                        }
                    });

                    Thread.sleep(4000);

                    System.out.println("unSubscribe!!!!!! By Key ");
                    RouterNotify.getNotifier().unSubscribe("NE");

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

                RouterNotify.getNotifier().post(msg);
                System.out.println("----------------------->");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void TestOne() {
        CtrlNotifier notifier = NeNotify.getNotifier();
        notifier.subscribe("NE", new EventObserver() {
            @Override
            public void onEvent(Object info) {
                Message msg = new Message();
                msg = (Message) info;
                System.out.println("NeNotify NE info:" + msg.toString());
            }
        });


        CtrlNotifier notifier2 = RouterNotify.getNotifier();
        notifier2.subscribe("Router", new EventObserver() {
            @Override
            public void onEvent(Object info) {
                Message msg = new Message();
                msg = (Message) info;
                System.out.println("RouterNotify Router info:" + msg.toString());
            }
        });

//        notifier2.subscribe("American", new EventObserver() {
//            @Override
//            public void onEvent(Object info) {
//                System.out.println("NeNotify American info = " + info);
//            }
//        });
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


                    RouterNotify.getNotifier().post(msg);
                    System.out.println("--------------------");

                    RouterNotify.getNotifier().post(msg2);
                    System.out.println("--------------------");

                    RouterNotify.getNotifier().post(msg3);
                    System.out.println("--------------------");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
