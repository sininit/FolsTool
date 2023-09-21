package top.fols.atri.interfaces.interfaces;

public interface IConvert<P, R> extends ICaller<P, R> {
    @Override R next(P param);
}
