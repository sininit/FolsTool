package top.fols.box.util.thread;

import top.fols.box.lang.XStringFormat;

public class XFixedThreadPoolTest {
    public static void main(String[] args) throws  Throwable{


        final XFixedThreadPool pool = new XFixedThreadPool().setMaxRuningCount(Integer.MAX_VALUE);
        for (int fori = 0;fori < 100;fori++) {
            final int i = fori;
            XFixedThreadPool.Run runable = new XFixedThreadPool.Run(){
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
        System.out.println(XStringFormat.strf("wait:{0}, running:{1}, max:{2}", pool.getWaitCount(), pool.getNowRuningCount(), pool.getMaxRuningCount()));
        pool.removeAllAndWaitEnd(pool.list());
        System.out.println(XStringFormat.strf("wait:{0}, running:{1}, max:{2}", pool.getWaitCount(), pool.getNowRuningCount(), pool.getMaxRuningCount()));


        System.out.println("----------------------");

        Thread.currentThread().sleep(1001);



        while (true) {
            if (false) {break;}

            System.out.println(XStringFormat.strf(
                    "wait:{0}, running:{1}, max:{2}, \n"
                            + "isDealThreadRunning:{3}, isDealThreadWait:{4}"

                    , pool.getWaitCount()
                    , pool.getNowRuningCount()
                    , pool.getMaxRuningCount()

                    , pool.is_deal_thread_running()
                    , pool.is_deal_thread_running_wait()
            ));

            if (!(pool.is_deal_thread_running() || pool.is_deal_thread_running_wait())) {
                XFixedThreadPool.Run runable = new XFixedThreadPool.Run(){
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


                System.out.println(XStringFormat.strf(
                        "wait:{0}, running:{1}, max:{2}, \n"
                                + "isDealThreadRunning:{3}, isDealThreadWait:{4}"

                        , pool.getWaitCount()
                        , pool.getNowRuningCount()
                        , pool.getMaxRuningCount()

                        , pool.is_deal_thread_running()
                        , pool.is_deal_thread_running_wait()
                ));
                break;
            }
        }

        if(true){return;}

    }
}