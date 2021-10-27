package top.fols.atri.util;

public interface Releasable extends ReleasableIO {
	@Override 
	public boolean release();

	@Override
	public boolean released();
}
