package top.fols.box.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import top.fols.atri.net.URLs;

public class Jars {

	public static final char   SEPARATOR  = Zips.SEPARATOR;
	public static final String SEPARATORS = Zips.SEPARATORS;

	public static final String ROOT_DIRECTORY = Zips.ROOT_DIRECTORY;

	public static URL getJarURL(File file) throws MalformedURLException	{
		return getJarURL(file.toURI().toURL());
	}
	//jar:<url>!/{entry}
	@Deprecated
	public static URL getJarURL(URL jarFile) throws MalformedURLException	{
		return new URL("jar:" + jarFile.toExternalForm() + "!/");
	}

	

	public static URL getJarInnerFileURL(File jar, String relativePath) throws MalformedURLException	{
		return getJarInnerFileURL(getJarURL(jar), relativePath);
	}
	public static URL getJarInnerFileURL(URL jarFile, String relativePath) throws MalformedURLException	{
		return new URL(jarFile, URLs.formatToCanonicalRelativeURL(relativePath));
	}

	
//	public static URL formatJarInnerFileURL(File jar, String path) throws MalformedURLException	{
//		URL jarURL = getJarURL(jar);
//		return formatJarInnerFileURL(jarURL, path);
//	}
//	public static URL formatJarInnerFileURL(URL jarFile, String path) throws MalformedURLException	{
//		return new URL(jarFile, Zips.formatPath(path));
//	}
//	
//	public static URLConnection findOpenJarInnerFile(File jar, String path) throws MalformedURLException, IOException	{
//		URL jarURL = getJarURL(jar);
//		return findOpenJarInnerFile(jarURL, path);
//	}
//	public static URLConnection findOpenJarInnerFile(URL jarURL, String path) throws MalformedURLException, IOException	{
//		URLConnection connection = new URL(jarURL, path).openConnection();
//		if (connection.getContentLength() < 0) {
//			Streams.close(connection);
//			
//			URLConnection connection2 = null;
//			try {
//				connection2 = new URL(jarURL, Zips.formatPath(path)).openConnection();
//				if (connection2.getContentLength() < 0) {
//					Streams.close(connection2);
//				} else {
//					return connection2;
//				}
//			} catch (Exception e)	{
//				Streams.close(connection2);
//			}
//			
//			return new URL(jarURL, path).openConnection();
//		}
//		return connection;
//	}
//
}
