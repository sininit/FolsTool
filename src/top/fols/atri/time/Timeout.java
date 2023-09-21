package top.fols.atri.time;

import java.util.Timer;
import java.util.TimerTask;

import top.fols.atri.interfaces.interfaces.ICallbackOneParam;
import top.fols.atri.lang.Objects;

public class Timeout {
	final Timer timer  = new Timer();
	
	public void cancel() {
		try { timer.cancel(); } catch (Throwable ignored) {}
	} 
	
	public class Control {
		long      time;
		TimerTask thread;
		ICallbackOneParam<Control> runnable;
		boolean   executed;

		public void cancel() {
			try { thread.cancel(); } catch (Throwable ignored) {}
		}
		
		
		
		public long setTimeout(long millisecondsLater) {
			Objects.requireTrue(millisecondsLater >= 0, "time argument error");
			this.cancel();
			
			Control clone = Timeout.this.setTimeout(this.runnable, millisecondsLater);
			this.time     = clone.time;
			this.thread   = clone.thread;
			this.runnable = clone.runnable;
			this.executed = clone.executed;

			return millisecondsLater;
		}
		public long setInterval(long millisecondsLater) {
			Objects.requireTrue(millisecondsLater >= 0, "time argument error");
			this.cancel();

			Control clone = Timeout.this.setInterval(this.runnable, millisecondsLater);
			this.time     = clone.time;
			this.thread   = clone.thread;
			this.runnable = clone.runnable;
			this.executed = clone.executed;

			return millisecondsLater;
		}
		public long getTime() {
			return time;
		}
		
		
		
		public boolean isExecuted() {
			return executed;
		}
		public ICallbackOneParam<Control> runnable() {
			return runnable;
		}
	}




	public Control setTimeout(final Runnable runnable, long millisecondsLater) {
		return setTimeout(new ICallbackOneParam<Control>(){
			@Override
			public void callback(Control param) {
				runnable.run();
			}
		}, millisecondsLater);
	}
	public Control setTimeout(final ICallbackOneParam<Control> runnable, long millisecondsLater) {
		Objects.requireTrue(millisecondsLater >= 0, "time argument error");
		
		final Control control = new Control();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				// TODO: Implement this method
				try { 
					runnable.callback(control); 
				} catch (Throwable ignored) {
				} finally {
					control.executed = true;
				}
				control.cancel();
			}
		};
		control.thread   = tt;
		control.runnable = runnable;
		control.time     = millisecondsLater;
		control.executed = false;

		timer.schedule(tt, millisecondsLater);

		return control;
	}
	public Control setInterval(final Runnable runnable, long millisecondsLater) {
		return setInterval(new ICallbackOneParam<Control>(){
			@Override
			public void callback(Control param) {
				runnable.run();
			}
		}, millisecondsLater);
	}
	public Control setInterval(final ICallbackOneParam<Control> runnable, long millisecondsLater) {
		Objects.requireTrue(millisecondsLater >= 0, "time argument error");

		final Control control = new Control();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				// TODO: Implement this method
				try { 
					runnable.callback(control); 
				} catch (Throwable ignored) {
				} finally {
					control.executed = true;
				}
			}
		};
		control.thread   = tt;
		control.runnable = runnable;
		control.time     = millisecondsLater;
		control.executed = false;

		timer.schedule(tt, millisecondsLater, millisecondsLater);

		return control;
	}


}
