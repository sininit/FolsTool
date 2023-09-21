package top.fols.box.util.process_guard.util;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import top.fols.atri.assist.util.*;
import top.fols.atri.io.*;
import top.fols.atri.io.util.*;
import top.fols.atri.lang.*;

import top.fols.atri.lang.Objects;
import top.fols.box.util.process_guard.ProcessGuard;

@Deprecated
public class WinOrUnixCommandFindProcesser implements ProcessGuard.FindProcesser {
	protected final Object lock = new Object();



	public static class Parameter {
		String caption;
		String command;

		public void   setCaption(String caption) { this.caption = caption;}
		public String getCaption() { return caption; }

		public void   setCommand(String command) { this.command = command; }
		public String getCommand() { return command; }
	}
	protected Parameter findParameter = new Parameter();

	public Parameter getParameter() { return findParameter; }



	public class PortProcess {
		protected int port;
		protected final WinOrUnixCommandFindProcesser dfp;
		PortProcess(WinOrUnixCommandFindProcesser dfp) {
			this.dfp = dfp;
		}

		protected void setPort(int p) { this.port = p; }
		public int     getPort() {return port; }

		Set<String> pids;
		protected void     setPids(String[] pid) { this.pids = new LinkedHashSet<>(Arrays.asList(pid)); }
		public Set<String> getPids() { return (null == this.pids) ? new LinkedHashSet<String>() : this.pids;}

		transient ProcessGuard.ProcessObjectGroup pogCache;
		public ProcessGuard.ProcessObjectGroup findProcessObjectGroupOrGetCache() {
			if (pogCache == null) {
				Set<String> sets = getPids();

				Parameter parameter = new Parameter();
				Set<ProcessGuard.ProcessObject> result = new LinkedHashSet<>();

				ProcessGuard.ProcessObjectGroup find = findProcess(parameter);
				Set<ProcessGuard.ProcessObject> findList =find.getList();
				for (ProcessGuard.ProcessObject po: findList) {
					if (sets.contains(po.getPid())) {
						result.add(po);
					}
				}
				ProcessGuard.ProcessObjectGroup pog;
				pog = new ProcessGuard.ProcessObjectGroup(WinOrUnixCommandFindProcesser.this);
				pog.setList(result);
				pogCache = pog;
			}
			return pogCache;
		}
	}

	public PortProcess findPordProcessId(int port) {
		String[] pid = openNetstater().getPortProcessPid(port);

		PortProcess pp = new PortProcess(this);
		pp.setPids(pid);
		pp.setPort(port);

		return   pp;
	}


	public ProcessGuard.ProcessObjectGroup findProcess(Parameter parameter) {
		// TODO: Implement this method
		ProcessGuard.ProcessObjectGroup pog = new ProcessGuard.ProcessObjectGroup(this);
		Set<ProcessGuard.ProcessObject> result = new LinkedHashSet<>();
		ProcessElement[]   process = openFinder().findProcess(parameter);
		if (null != process) {
			for (ProcessElement p: process) {
				ProcessGuard.ProcessObject po = pog.newProcessObject();
				po.setPpid(Strings.cast(p.getParentProcessId()));
				po.setPid(Strings.cast(p.getProcessId()));

				result.add(po);
			}
		}
		pog.setList(result);
		return pog;
	}

	@Override
	public ProcessGuard.ProcessObjectGroup findProcess() {
		// TODO: Implement this method
		return findProcess(getParameter());
	}



	@Override
	public void kill(ProcessGuard.ProcessObjectGroup group) {
		// TODO: Implement this method
		if (null != group) {
			openTaskkill().kill(group);
		}
	}

//	class ProcessObject extends ProcessGuard.ProcessObject {
//		ProcessObject() {
//			super(DefaultFindProcesser.this);
//		}
//	}

	public boolean isForceKill() { return true; }



	@Override
	public void exit() {
		// TODO: Implement this method
		synchronized (lock) {
			Streams.close(netstaterCache);
			Streams.close(finderCache);
			Streams.close(taskkillCache);
		}
	}


	protected static final OSInfo.OSType system = OSInfo.getOSType();


	protected Netstater netstaterCache;
	protected Netstater openNetstater() {
		synchronized (lock)	{
			if (netstaterCache == null) {
				if (OSInfo.isWindows(system)) {
					netstaterCache = new WindowNetstater();
				} else if (OSInfo.isUnix(system)) {
					netstaterCache = new UnixNetstater();
				} else {
					throw new UnsupportedOperationException(String.valueOf(system));
				}
			}
			return netstaterCache;
		}
	}

	protected Finder finderCache;
	protected Finder openFinder() {
		synchronized (lock)	{
			if (finderCache == null) {
				if (OSInfo.isWindows(system)) {
					finderCache = new WindowFinder();
				} else if (OSInfo.isUnix(system)) {
					finderCache = new UnixFinder();
				} else {
					throw new UnsupportedOperationException(String.valueOf(system));
				}
			}
			return finderCache;
		}
	}


	Taskkill  taskkillCache;
	protected Taskkill openTaskkill() {
		synchronized (lock)	{
			if (taskkillCache == null) {
				if (OSInfo.isWindows(system)) {
					taskkillCache = new WindowTaskkill();
				} else if (OSInfo.isUnix(system)) {
					taskkillCache = new UnixTaskkill();
				} else {
					throw new UnsupportedOperationException(String.valueOf(system));
				}
			}
			return taskkillCache;
		}
	}


	protected FastCommand newFastCommand() {
		return FastCommand.openProcess();
	}



	public static class ProcessElement {
        String CommandLine;
        long ParentProcessId;
        long ProcessId;

        List<ProcessElement> subProcess;

        public ProcessElement(String CommandLine, long ParentProcessId,  long ProcessId) {
            this.CommandLine = CommandLine;
            this.ParentProcessId = ParentProcessId;
            this.ProcessId = ProcessId;
        }

        public String getCommandLine()          { return this.CommandLine; }
        public Long getParentProcessId()    { return this.ParentProcessId; }
        public Long getProcessId()          { return this.ProcessId; }



        List<ProcessElement> initSubProcessList() {
            List<ProcessElement> subProcess = this.subProcess;
            if (null ==   subProcess) {
                this.subProcess = subProcess = new ArrayList<>();
            }
            return subProcess;
        }

        public List<ProcessElement> getSubProcessList()   { return Collections.unmodifiableList(initSubProcessList()); }
        public void addSubProcess(ProcessElement process) { initSubProcessList().add(process); }


        @Override
        public String toString() {
            return "Process{" +
				"ParentProcessId=" + ParentProcessId +
				", pid=" + ProcessId +
				", subProcess=" + subProcess +
				'}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProcessElement)) return false;
            ProcessElement process = (ProcessElement) o;
            return  ParentProcessId == process.ParentProcessId &&
				ProcessId == process.ProcessId &&
				Objects.equals(CommandLine, process.CommandLine);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(CommandLine, ParentProcessId, ProcessId);
        }

        public long[] listPid() {
            if (null == subProcess) {
                return new long[] {this.getProcessId()};
            } else {
                long[] list = new long[1 + subProcess.size()];
                int  index = 0;
                list[index++] = this.getProcessId();
                for (ProcessElement process : subProcess) {
                    list[index++] = process.getProcessId();
                }
                return list;
            }
        }
    }


	protected boolean is_match_command(ProcessElement process, Parameter parameter) {
		String caption = parameter.getCaption();
		String command = parameter.getCommand();
		if (null == command || command.isEmpty()) {
			return true;
		} else {
			if (process.getCommandLine().contains(command)) {
				return true; 
			}
			for (ProcessElement sub_process : process.getSubProcessList()) {
				if (sub_process.getCommandLine().contains(command)) { 
					return true; 
				}
			}
			return false;
		}
	}
	
	
	
	
	
	//************************



	public abstract class Netstater implements Closeable {
		abstract public String[] getPortProcessPid(int port);

		@Override
		abstract public void close();
	}

	public class WindowNetstater extends Netstater {
		FastCommand fastCommand = newFastCommand();
		public WindowNetstater() {
			try {
				FastCommand.findCommand("netstat");
			} catch (FileNotFoundException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		@Override
		public void close() {
			// TODO: Implement this method
			fastCommand.close();
		}


		@Override
		public String[] getPortProcessPid(int port) {
			String echoValue = "------------------------";
			String result = fastCommand.execute(String.format("echo %s & netstat -ano", echoValue));

			StringReaders charBufferOption = new StringReaders();
			charBufferOption.setDelimiter(Delimiter.lineCharDelimit());
			charBufferOption.buffer(result, result.length());

			result = "";

			Set<String> pids = new LinkedHashSet<>();

			boolean join = false;
			while (true) {
				char[]      s = charBufferOption.readNextLine(false);
				if (null == s)
					break;

				String line = new String(s);
				line = line.replaceAll("\\s+", " ");
				if (join) {
					result += (line + "\n");
					/**
					 *  TCP 127.0.0.1:3266 0.0.0.0:0 LISTENING 7088
					 *  TCP 127.0.0.1:3266 127.0.0.1:53657 ESTABLISHED 7088
					 *  TCP 127.0.0.1:53636 127.0.0.1:3266 TIME_WAIT 0
					 *  TCP 127.0.0.1:53657 127.0.0.1:3266 ESTABLISHED 5844
					 *  TCP [::1]:3266 [::]:0 LISTENING 7088
					 */
					line = line.trim();
					String[] valueSeparator = line.split("\\s+");
					if (valueSeparator.length >= 2) {
						if (valueSeparator[1].contains(":" + port)) {
							String pid = valueSeparator[valueSeparator.length - 1];
							if ("0".equals(pid)) {
								continue;
							}
							pids.add(pid);
						}
					}
				}
				if (line.contains(echoValue)) { join = true; }
			}

			return pids.toArray(new String[pids.size()]);
		}
	}
	public class UnixNetstater extends Netstater {
		FastCommand fastCommand = newFastCommand();
		public UnixNetstater() {
			try {
				FastCommand.findCommand("netstat");
			} catch (FileNotFoundException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		@Override
		public void close() {
			// TODO: Implement this method
			fastCommand.close();
		}

		final Pattern findProcessParse_Pattern = Pattern.compile(
			"\\s*(.*?)\\s+(.*?)\\s+(.*?)\\s+(.*?)\\s+(.*?)\\s+(.*?)\\s+(.*?)\\s+(.*?)");
		@Override
		public String[] getPortProcessPid(int port) {
			String result = fastCommand.execute("netstat -tunlp");

			StringReaders charBufferOption = new StringReaders();
			charBufferOption.setDelimiter(Delimiter.lineCharDelimit());
			charBufferOption.buffer(result, result.length());

			Set<String> pids = new LinkedHashSet<>();
			while (true) {
				char[]      s = charBufferOption.readNextLine(false);
				if (null == s)
					break;

				/*
				 Active Internet connections (only servers)                                                    
				 Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name                                                                                               
				 tcp        0      0 127.177.130.137:34685   0.0.0.0:*               LISTEN      8442/com.github.kr3
				 */

				String line = new String(s);
				String[] splits = line.split("\\s+");
				if (splits.length >= 7) {
					String pidAndName = splits[7 - 1];
					int index = pidAndName.indexOf("/");
					if (index > -1) {
						String address    = splits[3];
						if (address.contains(":" + port)) {
							String pid = pidAndName.substring(0, index);
							if ("0".equals(pid)) {
								continue;
							}
							pids.add(pid);
						}
					}
				}
			}

			return pids.toArray(new String[pids.size()]);
		}
	}
	


	public abstract class Finder implements Closeable {
		abstract public ProcessElement[] findProcess(Parameter parameter);

		@Override
		abstract public void close();
	}

	public class WindowFinder extends Finder {
		FastCommand fastCommand = newFastCommand();
		public WindowFinder() {
			try {
				FastCommand.findCommand("wmic");
			} catch (FileNotFoundException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		@Override
		public ProcessElement[] findProcess(Parameter parameter) {
			String caption = parameter.getCaption();

			String result;
			try {
				result = fastCommand.execute(String.format("wmic process %s get commandline,ParentProcessId,processid /value"
						, (null == caption || caption.isEmpty()) ? "": " where caption=\""+caption+"\" "));
			} catch (Throwable e) {
				String message = e.getMessage();
				if (message == null)
					message = "";
				if (message.contains("没有") || message.contains("no"))
					return null;
				throw e;
			}

			Set<ProcessElement> pids = new HashSet<>();
			ProcessElement[] processes = findProcessParse(result);
			if (null != processes) {
				for (ProcessElement process : processes) {
					if (is_match_command(process, parameter)) { pids.add(process); }
				}
			}
			return pids.toArray(new ProcessElement[]{});
		}

		final Pattern findProcessParse_Pattern = Pattern.compile(
            "^" +
			"CommandLine\\s*=\\s*(.*?)" + "\\s*" +
			"ParentProcessId\\s*=\\s*(.*?)" + "\\s*" +
			"ProcessId\\s*=\\s*(.*?)" + "$"
            , Pattern.MULTILINE);

		ProcessElement[] findProcessParse(String content) {
			Matcher matcher = findProcessParse_Pattern.matcher(content);
			Map<Long, ProcessElement> rootM  = new LinkedHashMap<>();   //all process

			while (matcher.find()) {
				String CommandLine = matcher.group(1);
				long ParentProcessId = Long.parseLong(matcher.group(2));
				long ProcessId = Long.parseLong(matcher.group(3));

				ProcessElement process = new ProcessElement(CommandLine, ParentProcessId, ProcessId);

				/*
				 * 假定此process 是一个（chrome.exe）主进程
				 * 由于过滤了caption（映像名称）（chrome.exe）所以 所有进程都是 caption（chrome.exe）
				 *
				 * 由于 主进程 必定 是第一个启动的，然后 主进程启动子进程
				 * 所以返回的查询结果 只需要按照顺时针顺序读取即可
				 * 如果 rootM 不存在 process.ParentProcessId 就put(ProcessId, process)加入表 （这个肯定是主进程）
				 * 这样子后面读到process的子进程必定能 加入到这个process sub_process里
				 */
				ProcessElement root = rootM.get(ParentProcessId);
				if (null == root) {
					rootM.put(ProcessId, process);
				} else {
					root.addSubProcess(process);
				}
			}
			return rootM.values().toArray(new ProcessElement[]{});
		}

		@Override
		public void close() {
			// TODO: Implement this method
			fastCommand.close();
		}
	}

	public class UnixFinder extends Finder {
		FastCommand fastCommand = newFastCommand();
		public UnixFinder() {
			try {
				FastCommand.findCommand("ps");
				FastCommand.findCommand("grep");
			} catch (FileNotFoundException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		final Pattern findProcessParse_Pattern = Pattern.compile(
            "\\s*(\\d+)\\s+(\\d+)\\s+(.*)");
		ProcessElement[] findProcessParse(String result) {
			Map<Long, ProcessElement> rootM  = new LinkedHashMap<>();   //all process


			StringReaders charBufferOption = new StringReaders();
			charBufferOption.setDelimiter(Delimiter.lineCharDelimit());
			charBufferOption.buffer(result, result.length());
			while (true) {
				char[]      s = charBufferOption.readNextLine(false);
				if (null == s)
					break;

				String content = new String(s);
				Matcher matcher = findProcessParse_Pattern.matcher(content);

				while (matcher.find()) {
					String CommandLine = matcher.group(3);
					long ParentProcessId = Long.parseLong(matcher.group(1));
					long ProcessId       = Long.parseLong(matcher.group(2));

					String grep = "grep";
					if ((((CommandLine.startsWith(grep))))) {
						if (CommandLine.substring(grep.length(), Math.min(grep.length() + 1, CommandLine.length())).trim().isEmpty()) {
							continue;
						}
					}

					ProcessElement process = new ProcessElement(CommandLine, ParentProcessId, ProcessId);

					ProcessElement root = rootM.get(ParentProcessId);
					if (null == root) {
						rootM.put(ProcessId, process);
					} else {
						root.addSubProcess(process);
					}
				}
			}
			return rootM.values().toArray(new ProcessElement[]{});
		}

		@Override
		public ProcessElement[] findProcess(Parameter parameter) {
			// TODO: Implement this method
			String caption = parameter.getCaption();

			String result;
			try {
				String cmd = "ps -o ppid,pid,args " + ((null == caption || caption.isEmpty()) ? "": " | grep '" + caption + "'");
				result = fastCommand.execute(cmd);
			} catch (Throwable e) {
				throw e;
			}

//				System.out.println(cmd);

			Set<ProcessElement> pids = new HashSet<>();
			ProcessElement[] processes = findProcessParse(result);
			if (null != processes) {
				for (ProcessElement process : processes) {
					if (is_match_command(process, parameter)) { pids.add(process); }
				}
			}
			return pids.toArray(new ProcessElement[]{});
		}

		@Override
		public void close() {
			// TODO: Implement this method
			fastCommand.close();
		}
	}


	public abstract class Taskkill implements Closeable {
		protected FastCommand fastCommand = newFastCommand();

		abstract public void killObject(ProcessGuard.ProcessObject po);

		public int kill(ProcessGuard.ProcessObjectGroup pog) {
			if (null == pog || pog.isEmpty()) { return 0; }

			int count = 0;
			for (ProcessGuard.ProcessObject pid : pog.getList()) {
				try {
					killObject(pid); 
					count++;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			return count;
		}
		public boolean kill(ProcessGuard.ProcessObject pid) {
			try {
				killObject(pid);
			} catch (Throwable e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}


		@Override
		public void close() {
			// TODO: Implement this method
			fastCommand.close();
		}
	}


	public class UnixTaskkill extends Taskkill {
		public UnixTaskkill() {
		}


		@Override
		public void killObject(ProcessGuard.ProcessObject po) {
			// TODO: Implement this method
			try {
				fastCommand.execute(String.format("kill %s %s"
						, isForceKill() ?"-9": ""
						, po.getPid()));
			} catch (Throwable e) {
				String message = e.getMessage();
				if (message == null)
					message = "";
				throw e;
			}
		}
	}

	public class WindowTaskkill extends Taskkill {
		public WindowTaskkill() {}

		@Override
		public void killObject(ProcessGuard.ProcessObject po) {
			try {
				fastCommand.execute(String.format("taskkill %s /PID %s"
						, isForceKill() ? "/F": ""
						, po.getPid()));
			} catch (Throwable e) {
				String message = e.getMessage();
				if (message == null)
					message = "";
				if (message.contains("没有") || message.contains("no"))
					return;
				throw e;
			}
		}

	}




}
