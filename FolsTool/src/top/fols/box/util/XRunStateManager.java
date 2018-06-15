package top.fols.box.util;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import top.fols.box.statics.XStaticBaseType;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
public class XRunStateManager
{
	private static final AtomicBoolean state = new AtomicBoolean();
	public boolean getRunState()
	{
		return state.get();
	}
	public void setRunState(boolean b)
	{
		state.set(b);
	}
}

