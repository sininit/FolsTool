package top.fols.box.util.process_guard.android;

import java.util.*;
import top.fols.atri.lang.*;
import top.fols.box.util.process_guard.ProcessGuard;
import top.fols.box.util.process_guard.util.WinOrUnixCommandFindProcesser;

public class AndroidPackageFinder extends WinOrUnixCommandFindProcesser {
	public AndroidPackageFinder() {
		super.findParameter = new AndroidPackageFindParameter();
	}
	@Override
	public WinOrUnixCommandFindProcesser.Taskkill openTaskkill() {
		return new WinOrUnixCommandFindProcesser.Taskkill() {
			@Override
			public void killObject(ProcessGuard.ProcessObject po) {
				AndroidPackageProcessObject apo = (AndroidPackageProcessObject) po;

				// TODO: Implement this method
				fastCommand.execute("am force-stop " + apo.getPackageName());
			}
		};
	}

	@Override
	public AndroidPackageProcessObjectGroup findProcess(WinOrUnixCommandFindProcesser.Parameter parameter0) {
		// TODO: Implement this method
		AndroidPackageFindParameter fp = (AndroidPackageFindParameter) parameter0;

		String pk = fp.getPackageName();

		WinOrUnixCommandFindProcesser.Parameter parameter = new WinOrUnixCommandFindProcesser.Parameter();
		parameter.setCommand(pk);

		AndroidPackageProcessObjectGroup pog = new AndroidPackageProcessObjectGroup(this);
		Set<AndroidPackageProcessObject> result = new LinkedHashSet<>();
		ProcessElement[]   process = openFinder().findProcess(parameter);
		if (null != process) {
			for (ProcessElement p: process) {
				AndroidPackageProcessObject po = pog.newProcessObject();
				po.setPpid(Strings.cast(p.getParentProcessId()));
				po.setPid(Strings.cast(p.getProcessId()));
				po.setPackageName(pk);
				result.add(po);
			}
		}
		pog.setList((Set) result);
		return pog;
	}

	public static class AndroidPackageFindParameter extends WinOrUnixCommandFindProcesser.Parameter {
		String packageName;
		AndroidPackageFindParameter() {
			super();
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public String getPackageName() {
			return packageName;
		}
	}

	@Override
	public AndroidPackageFindParameter getParameter() {
		// TODO: Implement this method
		return (AndroidPackageFindParameter) super.getParameter();
	}
}
