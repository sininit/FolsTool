package top.fols.box.time;

import java.io.Serializable;

public class XTimeConsum implements Serializable {
	private static final long serialVersionUID = 1L;


	private String id;
	private long start, end;


	public String getID() {
		return this.id;
	}
	public XTimeConsum setID(String id) {
		this.id = id;
		return this;
	}


	public XTimeConsum start() {
		this.start = XTimeTool.getSystemCurrentTimeMillis();
		return this;
	}
	public long getStartTime() {
		return start;
	}


	public XTimeConsum end() {
		this.end = XTimeTool.getSystemCurrentTimeMillis();
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




	public static XTimeConsum newInstance(String id) {
		XTimeConsum tc = new XTimeConsum()
				.setID(id);
		return tc;
	}
	public static XTimeConsum newAndStart(String id) {
		return newInstance(id).start();
	}
	public static XTimeConsum newAndStart() {
		return newAndStart(null);
	}





	@Override
	public String toString() {
		// TODO: Implement this method
		return String.format("[%s]time consum: %s", this.getID(), this.getEndLessStart());
	}






}
