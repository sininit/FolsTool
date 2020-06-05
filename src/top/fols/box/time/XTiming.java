package top.fols.box.time;

public class XTiming {
	private long start, end;

	public XTiming start() {
		this.start = XTimeTool.currentTimeMillis();
		return this;
	}

	public long getStartTime() {
		return start;
	}

	public XTiming end() {
		this.end = XTimeTool.currentTimeMillis();
		return this;
	}

	public long getEndTime() {
		return this.end;
	}

	public long getEndLessStart() {
		return this.end - this.start;
	}

	public long endAndGetEndLessStart() {
		return this.end().getEndLessStart();
	}

	public static XTiming newAndStart() {
		return new XTiming().start();
	}
}
