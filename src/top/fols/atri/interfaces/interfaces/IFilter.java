package top.fols.atri.interfaces.interfaces;


@SuppressWarnings({"UnnecessaryInterfaceModifier", "rawtypes"})
public interface IFilter<P> {
    public boolean next(P param);

    public static final IFilter[]   EMPTY_ARRAY    = new IFilter[0];
    public static final IFilter[][] EMPTY_ARRAY_2D = new IFilter[0][0];
}
