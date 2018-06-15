package top.fols.box.util.sequence;

import top.fols.box.util.sequence.interfaces.XInterfaceSequenceBigByte;
import top.fols.box.util.sequence.interfaces.XInterfaceSequenceByte;

public class XSequenceByteUtils
{
	public static XInterfaceSequenceByte wrapByteSequence(final byte[] bytes)
	{
		return new XInterfaceSequenceByte()
		{
			@Override
			public int length()
			{
				return bytes.length;
			}
			@Override
			public byte byteAt(int p1)
			{
				// TODO: Implement this method
				return bytes[p1];
			}
		};
	}



	public static XInterfaceSequenceBigByte wrapBigByteSequence(final byte[] bytes)
	{
		return new XInterfaceSequenceBigByte()
		{
			@Override
			public long length()
			{
				return bytes.length;
			}
			@Override
			public byte byteAt(long p1)
			{
				// TODO: Implement this method
				return bytes[(int)p1];
			}
		};
	}
	public static XInterfaceSequenceBigByte wrapBigByteSequence(final XInterfaceSequenceByte bytes)
	{
		return new XInterfaceSequenceBigByte()
		{
			@Override
			public long length()
			{
				return bytes.length();
			}
			@Override
			public byte byteAt(long p1)
			{
				// TODO: Implement this method
				return bytes.byteAt((int)p1);
			}


		};
	}
}
