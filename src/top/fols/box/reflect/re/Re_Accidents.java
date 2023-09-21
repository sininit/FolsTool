package top.fols.box.reflect.re;

import top.fols.atri.lang.Strings;
import top.fols.box.lang.Throwables;

import java.util.Arrays;

public class Re_Accidents {

	public static String runtime_grammatical_error(Re_CodeLoader.Base[] bases, Re_CodeLoader.Base index) {
		return "runtime grammatical errors [" + index + "], in [" + Re_CodeLoader.Expression.getExpressionAsString(Arrays.asList(bases)) + "]"
				   + ", indexLine["+index.getLine()+"]";
	}


//	public static String stack_overflow(long now, long max) {
//		return "stack overflow, now==" + now + ", max=" + max;
//	}



	public static String reclass_initialized(Re_Class re_class) {
		return "re-class[" + (null == re_class ? null : re_class.getName()) + "]" + " initialized";
	}
	public static String reclass_initialize_failed(Re_Class re_class) {
		return "re-class[" + (null == re_class ? null : re_class.getName()) + "]" + " initialize failed";
	}

	public static String not_found_reclass(String name) {
		return "not found re-class: " + name;
	}

	public static String executor_no_bind_class() {
		return "executor no bind class";
	}
	public static String executor_no_bind_class_instance() {
		return "executor no bind class instance";
	}
	public static String executor_no_bind_class_loader() {
		return "executor no bind class loader";
	}

	public static String cannot_execute(String vName) {
		return vName + " is not a function";
	}

	public static String unable_to_process_parameters(String vName, int count) {
		return vName + " unable to process parameters"
				+ ", paramCount=" + count;
	}

	public static String unsupported_type(String type) {
		return "unsupported type: " + type;
	}
	public static String unsupported_type(String varName, String type, String need) {
		return "(" + varName + ")" + " unsupported type: " + type + ", need_type: " + need;
	}

	public static String undefined_object_var(String instanceName, Re_CodeLoader.Base name) {
		return "properties: " + (instanceName + '.' + name)  + " is not defined" +
				", indexLine=" + name.getLine();
	}
	public static String undefined_object_call(String instanceName, Re_CodeLoader.Base name, int paramCount) {
		return "properties: " + (instanceName + '.' + name)  + " is not defined" +
				", paramCount=" + paramCount + ", indexLine="+name.getLine();
	}
	public static String undefined(Re_IRe_Object reObject, String name) {
		return "properties: " + ((null == reObject ? null : reObject.getName()) + "." + name) + " is not defined";
	}
	public static String undefined(String name) {
		return "properties: " + (name) + " is not defined";
	}

	public static String assignment_to_final_variable(){
		return "assignment to final variable";
	}

	static String typeReOrJava(Object v) {
		return "[" + (Re_Utilities.isIReObject(v) ? "re" : "java") + "]";
	}




	static class Errors {
		public static String error_out_of_memory() {
			return "out_of_memory";
		}
	}










	public static class ExecuteException extends RuntimeException {
		public ExecuteException(String message)  { super(message); }
		public ExecuteException(String message, Throwable cause) {
			super(message, cause);
		}
		public ExecuteException(Throwable cause) {
			super(cause);
		}
	}

	public static class ReExecuteException extends Throwables.MessageException {
		public ReExecuteException(Re_NativeStack stack, boolean printJavaTrack) {
			this(stack.getThrow(), printJavaTrack);
		}
		public ReExecuteException(Re_PrimitiveClass_exception.Instance instance, boolean printJavaTrack) {
			StringBuilder sb = new StringBuilder();
			sb.append(instance.asString());

			if (printJavaTrack) {
				sb.append("\n");

				String JavaTrackPrefix = "JavaTrack: ";
				sb.append(JavaTrackPrefix).append(Strings.tabsFromSecondLineStart(JavaTrackPrefix.length(), Throwables.toString(new Throwable())));
			}

			this.message = sb.toString();
		}
	}



	//编译时异常
	public static class CompileTimeGrammaticalException extends RuntimeException implements Re_NativeStack.ITraceElement, Cloneable {
		String method;
		String filePath;
		int line;

		public CompileTimeGrammaticalException(String message,
											   String filePath, int line)  {
			super(message);

			this.method = Re_CodeFile.METHOD_NAME__EMPTY;
			this.filePath = filePath;
			this.line = line;
		}
		public CompileTimeGrammaticalException(String message,
											   String filePath, int line, Throwable cause)  {
			super(message, cause);

			this.method = Re_CodeFile.METHOD_NAME__EMPTY;
			this.filePath = filePath;
			this.line = line;
		}

		@Override
		public String getMethod() {
			return method;
		}
		@Override public String getFilePath() {
			return filePath;
		}
		@Override public int getLine() {
			return line;
		}

		@Override
		public Re_NativeStack.ITraceElement clone() {
			try {
				return (Re_NativeStack.ITraceElement) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	//理论上不会出现这种问题
	public static class CompileTimeExpressionProcessingError extends RuntimeException {
		public CompileTimeExpressionProcessingError(String message)  { super(message); }
	}




	static class RuntimeInternalError extends Throwables.EmptyStackException {
		public RuntimeInternalError(String message)  { super(message); }
	}
	//理论上不会出现这种问题
	static class RuntimeInternalMathError extends Throwables.EmptyStackException {
		public RuntimeInternalMathError(String message)  { super(message); }
	}















}

