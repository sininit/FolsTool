package top.fols.box.time;

public class XTiming {
	private long start,end;
	public XTiming start() {
		start = XTimeTool.currentTimeMillis();
		return this;
	}
	public long getStartTime() {return start;}
	
	public XTiming end() {
		end = XTimeTool.currentTimeMillis();
		return this;
	}
	public long getEndTime() {return end;}
	
	
	public long getEndLessStart() {
		return end - start;
	}
	public long endAndGetEndLessStart() {
		return end().getEndLessStart();
	}

	public static XTiming newAndStart() {
		return new XTiming().start();
	}
}
