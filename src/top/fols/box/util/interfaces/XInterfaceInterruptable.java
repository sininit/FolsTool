package top.fols.box.util.interfaces;
import top.fols.box.annotation.XAnnotations;

/*
 * Call the method of the state of the thread will be set to "break" state.
 * Note: thread interrupts are merely setting the thread state, will not stop the thread. 
 * Users need to monitoring the status of the thread and do processing.
 */
@XAnnotations("call the method of the state of the thread will be set to 'break' state. Note: thread interrupts are merely setting the thread state, will not stop the thread. Users need to monitoring the status of the thread and do processing.")
public interface XInterfaceInterruptable {
	public abstract void interrupt();
	public abstract boolean isInterrupt();
	public abstract boolean checkInterrupt() throws InterruptedException;
}
