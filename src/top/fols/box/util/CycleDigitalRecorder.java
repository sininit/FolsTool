package top.fols.box.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import top.fols.atri.interfaces.abstracts.BitsOptions;
import top.fols.atri.lang.Finals;
import top.fols.atri.time.Times;

import static top.fols.atri.lang.Arrayz.leftMove;
import static top.fols.atri.lang.Arrayz.rightMove;


@SuppressWarnings({"AccessStaticViaInstance", "SameParameterValue", "UnnecessaryLocalVariable"})
public class CycleDigitalRecorder implements Serializable {
	private static final long serialVersionUID = 1L;

//	static public void main(String[] args) {
//		test.test(args);
//	}
	static class test {
		public static class DigitalRecords extends CycleDigitalRecorder {
			public DigitalRecords(long cycle, int recordCount) {
				super(cycle, recordCount);
			}

			long current;
			@Override
			public long currentTime() {
				return current;
			}
		}
		public static void test(String[] args) {
			{
				DigitalRecords recorder = new DigitalRecords(TimeUnit.DAYS.toMillis(1), 7);
				recorder.addCurrentRecordValue(1);
				System.out.println(recorder);


				recorder.current -= TimeUnit.DAYS.toMillis(1) * 3;
				recorder.addCurrentRecordValue(2);
				System.out.println(recorder);

				recorder.current -= TimeUnit.DAYS.toMillis(1) * 2;
				recorder.addCurrentRecordValue(3);
				System.out.println(recorder);


				recorder.current += TimeUnit.DAYS.toMillis(1) * 1;
				recorder.addCurrentRecordValue(4);
				System.out.println(recorder);


				recorder.current += TimeUnit.DAYS.toMillis(1) * 5;
				recorder.addCurrentRecordValue(5);
				System.out.println(recorder);


				recorder.current += TimeUnit.DAYS.toMillis(1) * 1;
				recorder.addCurrentRecordValue(6);
				System.out.println(recorder);

				recorder.current -= TimeUnit.DAYS.toMillis(1) * 7;
				recorder.addCurrentRecordValue(7);
				System.out.println(recorder);

				recorder.current += TimeUnit.DAYS.toMillis(1) * 7;
				recorder.addCurrentRecordValue(8);
				System.out.println(recorder);

				recorder.current += TimeUnit.DAYS.toMillis(1) * 1;
				recorder.addCurrentRecordValue(9);
				System.out.println(recorder);


				recorder.current -= TimeUnit.DAYS.toMillis(1) * 1;
				recorder.addCurrentRecordValue(10);
				System.out.println(recorder);

				//new
//				start=0, cycle=86400000, data=[1, 0, 0, 0, 0, 0, 0], currentCycleIndex=0
//				start=-259200000, cycle=86400000, data=[2, 0, 0, 1, 0, 0, 0], currentCycleIndex=0
//				start=-432000000, cycle=86400000, data=[3, 0, 2, 0, 0, 1, 0], currentCycleIndex=0
//				start=-432000000, cycle=86400000, data=[3, 4, 2, 0, 0, 1, 0], currentCycleIndex=1
//				start=-432000000, cycle=86400000, data=[3, 4, 2, 0, 0, 1, 5], currentCycleIndex=6
//				start=-345600000, cycle=86400000, data=[4, 2, 0, 0, 1, 5, 6], currentCycleIndex=6
//				start=-432000000, cycle=86400000, data=[7, 4, 2, 0, 0, 1, 5], currentCycleIndex=0
//				start=-345600000, cycle=86400000, data=[4, 2, 0, 0, 1, 5, 8], currentCycleIndex=6
//				start=-259200000, cycle=86400000, data=[2, 0, 0, 1, 5, 8, 9], currentCycleIndex=6
//				start=-259200000, cycle=86400000, data=[2, 0, 0, 1, 5, 18, 9], currentCycleIndex=5


			}
		}


	}






	static final int unitSize = BitsOptions.BIG_ENDIAN.LONG_BYTE_LENGTH;
	static final int presetCount = 2; //startTime cycle

	public static int calcDataBytesLength(int recordCount) {
		return (presetCount + recordCount) * unitSize;
	}
	public static long[] bytesToLongs(byte[] datas) {
		if (null == datas) {
			return Finals.EMPTY_LONG_ARRAY;
		} else {
			long[] data = new long[datas.length / unitSize];
			for (int i = 0;i < data.length;i++) {
				data[i] = BitsOptions.BIG_ENDIAN.getLong(datas, i * unitSize);
			}
			return data;
		}
	}
	public static byte[] longsToBytes(long[] datas) {
		byte[] data = new byte[datas.length * unitSize];
		for (int i = 0;i < datas.length;i++) {
			BitsOptions.BIG_ENDIAN.putBytes(data, i * unitSize, datas[i]);
		}
		return data;
	}



	static long _getStartTime(long[] data) {
		return data[0];
	}
	static void _setStartTime(long[] data, long v) {
		data[0] = v;
	}
	static long _getCycle(long[] data) {
		return data[1];
	}
	static void _setCycle(long[] data, long v) {
		data[1] = v;
	}
	static long _getRecordValue(long[] data, int RecordIndex) {
		return data[presetCount + RecordIndex];
	}
	static void _setRecordValue(long[] data, int RecordIndex, long record) {
		data[presetCount + RecordIndex] = record;
	}
	static int _getRecordCount(long[] data) {
		return data.length - presetCount;
	}
	static long[] _create(long startTime, long cycle,
						  int recordCount) {
		long[] newData = new long[presetCount + recordCount];
		_setStartTime(newData, startTime);
		_setCycle(newData, cycle);
		return newData;
	}
	static void _reset(long[] data,
					   long startTime, long cycle) {
		_setStartTime(data, startTime);
		_setCycle(data, cycle);

		_clearRecordValue(data);
	}
	static void _clearRecordValue(long[] data)	{
		int length = data.length;
		for (int i = presetCount; i < length; i++)
			data[i] = 0;
	}
	static long[] _getRecordValues(long[] loaded) {
		long[] records = new long[_getRecordCount(loaded)];
		for (int i = 0; i < records.length; i++)
			records[i] = _getRecordValue(loaded, i);
		return records;
	}



	long[] dataCache;
	long   cycle;
	int    cycleRecordCount;

	public CycleDigitalRecorder(long cycle, int cycleRecordCount) throws IllegalArgumentException {
		this(cycle, cycleRecordCount, null);
	}
	public CycleDigitalRecorder(long cycle, int cycleRecordCount, long[] dataCache) throws IllegalArgumentException {
		if (cycle < 0)
			throw new IllegalArgumentException("cycle: " + cycle);
		if (cycleRecordCount <= 0)
			throw new IllegalArgumentException("recordCount: " + cycleRecordCount);

		this.cycle            = cycle;
		this.cycleRecordCount = cycleRecordCount;
		this.dataCache   = isMatch(dataCache)
				? dataCache
				: _create(this.currentCycleTime(), this.cycle, this.cycleRecordCount);
	}



	public void save(long[] serializ) {}

	protected long[]  getCache()			{ return dataCache;  }
	protected void    setCache(long[] data) throws IllegalArgumentException {
		if (isMatch(data)) {
			this.dataCache = data;
		} else {
			throw new IllegalArgumentException("data=" + Arrays.toString(data));
		}
	}
	protected boolean isMatch(long[] data)  {
		return (null != data && data.length > 0 &&
				_getCycle(data) == cycle &&
				_getRecordCount(data) == cycleRecordCount);
	}




	protected final long currentCycleTime() {
		return Times.currentCycleTimeMillis(currentTime(), cycle);
	}
	protected long currentTime() {
		return System.currentTimeMillis();
	}







	public long getCycle()  		  { return cycle; }
	public int  getCycleRecordCount() { return cycleRecordCount; }




	public long getStartTime() {
		this.updateAndGetIndex();
		long[] loaded = dataCache;
		return _getStartTime(loaded);
	}


	public long getRecordValueSum() {
		this.updateAndGetIndex();
		long[] data = dataCache;
		long sum = 0;
		for (int i = 0; i < _getRecordCount(data); i++) {
			sum += _getRecordValue(data, i);
		}
		return sum;
	}
	public long getRecordValueAvg() {
		return  getRecordValueSum() / getCycleRecordCount();
	}
	public long[] getRecordValues() {
		this.updateAndGetIndex();
		long[] loaded = dataCache;
		return _getRecordValues(loaded);
	}






	public Records getRecords() {
		int recordIndex = this.updateAndGetIndex();
		long[] data = dataCache;
		return new Records(data, recordIndex);
	}
	public static class Records {
		private final long[] records;
		private final int    recordIndex;

		Records(long[] records, int recordIndex) {
			this.records     = records;
			this.recordIndex = recordIndex;
		}


		public int  getCycleIndex() 	{ return recordIndex; }
		public long getCycle() 		 	{ return _getCycle(records); }
		public long getStartTime()  	{ return _getStartTime(records); }

		public int  count()				 	    { return _getRecordCount(records);}

		public long get(int index)				{ return _getRecordValue(records, index);}
		public void set(int index, long value)  { _setRecordValue(records, index, value);}

		public long[] toArray() { return _getRecordValues(records); }


		public int    getValidOffset()	{ return 0; }
		public int    getValidCount() 	{ return recordIndex + 1; }
		public long   getValidRecordValueSum() {
			long sum = 0;
			for (int i = 0; i < getValidCount(); i++) {
				sum += _getRecordValue(records, getValidOffset() + i);
			}
			return sum;
		}
		public long   getValidRecordValueAvg() {
			return getValidRecordValueSum() / getValidCount();
		}
		public long[] getValidRecordValues() {
			long[] values = new long[getValidCount()];
			for (int i = 0; i < values.length; i++) {
				values[i] = _getRecordValue(records, getValidOffset() + i);
			}
			return values;
		}
	}


	public long getCurrentRecordValue() {
		int recordIndex = this.updateAndGetIndex();
		long[] data = dataCache;
		return data[presetCount + recordIndex];
	}





	int updateAndGetIndex() {
		long[] data = dataCache;
		long startTime   = _getStartTime(data);
		long currentTime = currentCycleTime();
		int recordIndex;
		long cedCount = (currentTime - startTime) / cycle;
		if  (cedCount    >= cycleRecordCount) {
			if (cedCount >= cycleRecordCount * 2L - 1) {
				_reset(data, currentTime, cycle);
				recordIndex = 0;
			} else {
				int left = (int) (cedCount + 1 - cycleRecordCount);
				leftMove(data, presetCount, data.length, left);
				startTime += (left * cycle);
				_setStartTime(data, startTime);
				recordIndex = cedCount > cycleRecordCount ? cycleRecordCount - 1: (cycleRecordCount - left);
			}
			save(this.dataCache = data);
		} else {
			if (cedCount >= 0) {
				recordIndex = (int) cedCount;
			} else {
				int abeCedCount = (int) Math.abs(cedCount);
				if (abeCedCount >= cycleRecordCount) {
					_reset(data, currentTime, cycle);
					recordIndex = 0;
				} else {
					int right = (abeCedCount);
					rightMove(data, presetCount, data.length, right);
					startTime -= (right * cycle);
					_setStartTime(data, startTime);
					recordIndex = 0;  //left no data
				}
				save(this.dataCache = data);
			}
		}
		return recordIndex;
	}
	public void setCurrentRecordValue(long v) {
		int recordIndex = this.updateAndGetIndex();
		long[] data   = dataCache;
		data[presetCount + recordIndex] = v;
		save(this.dataCache = data);
	}
	public void addCurrentRecordValue(long v) {
		int recordIndex = this.updateAndGetIndex();
		long[] data   = dataCache;
		data[presetCount + recordIndex] += v;
		save(this.dataCache = data);
	}


	public void clearRecordValue() {
		this.updateAndGetIndex();
		long[] data   = dataCache;
		_clearRecordValue(data);
		save(this.dataCache = data);
	}

	public void modifier(Modifier modifier) {
		int recordIndex = this.updateAndGetIndex();
		long[] data = dataCache;
		modifier.modify(new Records(data, recordIndex));
		save(this.dataCache = data);
	}
	public static interface Modifier {
		public void modify(Records record);
	}


	@Override
	public String toString() {
		// TODO: Implement this method
		int index = this.updateAndGetIndex();
		long[] loaded = dataCache;
		return "start=" + _getStartTime(loaded) + ", cycle=" + cycle  + ", data=" + Arrays.toString(_getRecordValues(loaded)) + ", currentCycleIndex=" + index;
	}


}


