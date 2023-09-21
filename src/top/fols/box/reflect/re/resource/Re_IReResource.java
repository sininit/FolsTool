package top.fols.box.reflect.re.resource;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

@SuppressWarnings({"UnnecessaryModifier", "UnnecessaryInterfaceModifier"})
public interface Re_IReResource {
	public static final Charset DEFAULT_CONTENT_CHARSET = Finals.Charsets.UTF_8;
	public Charset contentCharset()              ; //默认charset

	public URL getURL();

	/**
	 *
	 * @param path 文件路径
	 * @return 为空则代表资源不可读
	 */
	@Nullable
	public Re_IReResourceFile getFileResource(String path);
	/**
	 *
	 * @param path 文件路径
	 * @return 为空则代表资源不可读
	 */
	@Nullable
	public Long 			getFileSize(String path);
	/**
	 *
	 * @param path 文件路径
	 * @return 为空则代表资源不可读
	 */
	@Nullable
	public Long 			getFileLastModified(String path);
	/**
	 *
	 * @param path 文件路径
	 * @return 为空则代表资源不可读
	 */
	@Nullable
	public InputStream 		getFileInputStream(String path);


	/**
	 *
	 * @param className 类名
	 * @return 为空则代表没有这个资源或者没有办法读取
	 */
	@Nullable
	public Re_IReResourceFile findClassResource(String className); //为空则找不到该资源
}
