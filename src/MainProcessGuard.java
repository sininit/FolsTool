import top.fols.box.util.process_guard.ApplicationStarter;
import top.fols.box.util.process_guard.ProcessGuard;
import top.fols.box.util.process_guard.android.AndroidPackageFinder;
import top.fols.box.util.process_guard.util.FastCommand;
import top.fols.box.util.process_guard.util.WinOrUnixCommandFindProcesser;

public class MainProcessGuard {
    public static void main(String[] args) throws InterruptedException {
        final ProcessGuard.BooleanValue g = new ProcessGuard.BooleanValue(false);


        WinOrUnixCommandFindProcesser dfp = new WinOrUnixCommandFindProcesser();
        dfp.getParameter().setCaption("notepad++.exe");

        final ProcessGuard pg = ProcessGuard.newProcessGuard();
        pg.setApplicationStarter(null);
        pg.setFindProcesser(dfp);
        pg.setCallback(new ProcessGuard.ProcessMonitorCallback() {
            @Override
            public void start(ProcessGuard.ProcessObjectGroup.diff diff) {
                // TODO: Implement this method
                System.out.println("start: " + diff);
            }

            @Override
            public void diff(ProcessGuard.ProcessObjectGroup.diff diff) {
                // TODO: Implement this method
                System.out.println("change: " + diff);
            }

            @Override
            public void exit() {
                // TODO: Implement this method

                System.out.println(pg.getLastProcessObjectGroup());
                System.out.println("exit");
            }
        });
        pg.setAsyncMarkClose(g);
        pg.start();


//        {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    // TODO: Implement this method
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//
//                    pg.setAsyncMarkClose(new ProcessGuard.BooleanValue(true));
//                }
//            }).start();
//        }

        while ((Object) System.currentTimeMillis() != null) {
            Thread.sleep(1000);
        }

    }
}

