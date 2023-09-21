package top.fols.box.util.thread;

import top.fols.box.thread.FixedThreadPool;

public class FixedThreadPoolTest {
    public static void main(String[] args) throws  Throwable{


        final FixedThreadPool pool = new FixedThreadPool().setMaxRunningCount(Integer.MAX_VALUE);
        for (int fori = 0;fori < 100;fori++) {
            final int i = fori;
            FixedThreadPool.Run runable = new FixedThreadPool.Run(){
                @Override
                public void run() {
                    // TODO: Implement this method
                    while (true) {
                        try {
                            Thread.currentThread().sleep(100);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    System.out.println(i);
                }
            };
            pool.post(runable);
        }
        System.out.println(String.format("wait:%s, running:%s, max:%s", pool.getWaitCount(), pool.getNowRunningCount(), pool.getMaxRunningCount()));
        pool.removeAllAndWaitEnd(pool.list());
        System.out.println(String.format("wait:%s, running:%s, max:%s", pool.getWaitCount(), pool.getNowRunningCount(), pool.getMaxRunningCount()));


        System.out.println("----------------------");

        Thread.sleep(1001);



        while (true) {
            if (false) {break;}

            System.out.println(String.format(
                    "wait:%s, running:%s, max:%s, \n"
                            + "isDealThreadRunning:%s, isDealThreadWait:%s"

                    , pool.getWaitCount()
                    , pool.getNowRunningCount()
                    , pool.getMaxRunningCount()

                    , pool.is_deal_thread_running()
                    , pool.is_deal_thread_running_wait()
            ));

            if (!(pool.is_deal_thread_running() || pool.is_deal_thread_running_wait())) {
                FixedThreadPool.Run runable = new FixedThreadPool.Run(){
                    @Override
                    public void run() {
                        // TODO: Implement this method
                        while (true) {
                            try {
                                Thread.currentThread().sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                            System.out.println("(end): " + this);
                            break;
                        }
                    }
                };
                pool.post(runable);
                System.out.println("(post): " + runable);


                System.out.println(String.format(
                        "wait:%s, running:%s, max:%s, \n"
                                + "isDealThreadRunning:%s, isDealThreadWait:%s"

                        , pool.getWaitCount()
                        , pool.getNowRunningCount()
                        , pool.getMaxRunningCount()

                        , pool.is_deal_thread_running()
                        , pool.is_deal_thread_running_wait()
                ));
                break;
            }
        }

        if(true){return;}

    }
}