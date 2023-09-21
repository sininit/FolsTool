package top.fols.box.util.process_guard.android;

import java.util.*;
import top.fols.box.util.process_guard.ProcessGuard;

public class AndroidPackageProcessObjectGroup extends ProcessGuard.ProcessObjectGroup {
	AndroidPackageProcessObjectGroup(AndroidPackageFinder f) {
		super(f);
	}

	@Override
	public void setList(Set<ProcessGuard.ProcessObject> list) {
		// TODO: Implement this method
		super.setList(list);
	}

	@Override
	public AndroidPackageProcessObject newProcessObject() {
		// TODO: Implement this method
		return new AndroidPackageProcessObject(this);
	}
}
