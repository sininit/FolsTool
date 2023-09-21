package top.fols.box.util.process_guard.android;


import top.fols.box.util.process_guard.ProcessGuard;

public class AndroidPackageProcessObject extends ProcessGuard.ProcessObject {
	AndroidPackageProcessObject(AndroidPackageProcessObjectGroup g) {
		super(g);
	}
	String packageName;

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getPackageName() {
		return packageName;
	}
}
