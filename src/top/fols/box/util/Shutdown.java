package top.fols.box.util;

public class Shutdown {
	public static void addShutdownHook(Runnable runnable) {
		Runtime.getRuntime().addShutdownHook(new Thread(runnable));
	}


    static {
        try {
            hookConsoleKillHandler();
        } catch (Throwable ignored) {}
    }

	static void hookConsoleKillHandler() {
        new MqKillHandler().registerSignal("TERM");
        new MqKillHandler().registerSignal(System.getProperties().getProperty("os.name").toLowerCase().startsWith("win") ? "INT" : "USR2");
    }
	static class MqKillHandler  implements sun.misc.SignalHandler {
        public MqKillHandler() { }

        /**
         * 注册信号
         * @param signalName name
         */
        public void registerSignal(String signalName) {
            sun.misc.Signal signal = new sun.misc.Signal(signalName);
            sun.misc.Signal.handle(signal, this);
        }
        @Override
        public void handle(sun.misc.Signal signal) {
            try {
                System.exit(0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
