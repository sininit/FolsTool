package top.fols.atri.interfaces.interfaces;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface IReleasable extends IReleasableIO {
	@Override 
	public boolean release();

	@Override
	public boolean released();
}
