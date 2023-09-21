package top.fols.box.lang.parser;


import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Result;
import top.fols.box.util.Arrayx;

import java.util.ArrayList;
import java.util.List;

public class CodeParser {
	ParserList[] parserList = new ParserList[];

	static class ParserList {
		Parser[] parsers;

		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrayz.toString(this.parsers);
		}
	}




	public static abstract class Parser {
		char[]      head = Finals.EMPTY_CHAR_ARRAY;
		public void head(char[] chars) {
			this.head = chars;
		}
		public char[] head() {
			return this.head.clone();
		}


		String description;
		public void   description(String description) { this.description = description; }
		public String description() { return null == description ?"": description; }



		public abstract boolean isContinuous(int ch);



		public boolean empty() { return head.length == 0; }

		@Override 
		public abstract Parser clone();

		public abstract Objects readObjects(Reader reader, int offset);

		@Override
		public String toString() {
			// TODO: Implement this method
			return getClass().getSimpleName() + "(" + description() + ")" + ": " + Arrayz.toString(head);
		}
	}
	public static abstract class Objects {
		int line;
		protected void line(int line)    { this.line = line; }
		protected void line(Reader line) { this.line = line.line(); }
		public int     line()            { return this.line; }

		public abstract String string();
	}




	void addParser(ParserList list, Parser p) {
		list.parsers = Arrayx.add(list.parsers, 0, p);
	}
	ParserList getParser(char first) {
		ParserList  group = parserList[first];
		if (null == group) {
			parserList[first] = group = new ParserList();
		}
		if (null == group.parsers) {
			group.parsers = new Parser[]{};
		}
		return parserList[first];
	}
	ParserList releaseParser(char first) {
		ParserList  group = parserList[first];
		if (null == group) {
			return null;
		}
		if (null == group.parsers || group.parsers.length == 0) {
			parserList[first] = null;
			return null;
		}
		return group;
	}
//	private CodeParser.Parser[] sortParser(CodeParser.Parser[] parsers) {
//		if (null == parsers || parsers.length == 0) {
//			return parsers;
//		}
//		XArray.sort(parsers, new Comparator<CodeParser.Parser>() {
//				@Override
//				public int compare(CodeParser.Parser p1, CodeParser.Parser p2) {
//					// TODO: Implement this method
//					if (p1.head.length == p2.head.length) {
//						return 0;
//					} else if (p1.head.length > p2.head.length) {
//						return -1;
//					} else {
//						return 1;
//					}
//				}
//			});
//		return parsers;
//	}



	public static String toString(Objects[] objects) {
		if (null == objects) { return "[]"; }

		int lastLine = 1;
		String separator = ", ";
		StringBuilder strbuf = new StringBuilder();
		strbuf.append("[");
//		strbuf.w("\n");
		for (Objects object: objects) {
			int line = object.line();
			if (line != lastLine) {
				lastLine = line;
				strbuf.append("\n");
			}
			strbuf.append(object);

			strbuf.append(separator);
		}
		if (strbuf.length() >= separator.length()) {
			strbuf.setLength(strbuf.length() - separator.length());
		}
//		strbuf.w("\n");
		strbuf.append("]");
		return strbuf.toString();
	}




	public static class CharRange {
		public int left;
		public int right;

		public CharRange(int left, int right) {
			this.left = left;
			this.right = right;
		}
		public CharRange(int v) {
			this.left = v;
			this.right = v;
		}
		public CharRange() {}

		public boolean match(int ch) {
			return ch >= left && ch <= right;
		}
	}


	public boolean addParserForCharRange(Parser p, CharRange range) {
		if (null == p)   { return false; }
		if (contains(p)) { return false; }

		p = p.clone();
		p.head = Finals.EMPTY_CHAR_ARRAY;
		for (int i = 0; i < Character.MAX_VALUE; i++) {
			if (range.match(i)) {
				char first = (char) i;
				ParserList  group = getParser(first);
				addParser(group, p);
			}
		}
		return true;
	}


	public boolean addParserForIsContinuous(Parser p) {
		if (null == p)   { return false; }
		if (contains(p)) { return false; }

		p = p.clone();
		p.head = Finals.EMPTY_CHAR_ARRAY;
		for (int i = 0; i < Character.MAX_VALUE; i++) {
			if (p.isContinuous(i)) {
				char first = (char) i;
				ParserList  group = getParser(first);
				addParser(group, p);
			}
		}
		return true;
	}

	public boolean addParserForFirstChar(Parser p) {
		if (null == p || p.empty()) { return false; }
		if (contains(p)) { return false; }

		p = p.clone();
		char first = p.head[0];
		ParserList  group = getParser(first);
		addParser(group, p);
//		System.out.println(Arrayz.toString(group.parsers));
		return true;
	}
	public boolean remove(Parser p) {
		if (null == p || p.empty()) { return false; }
		char first = p.head[0];
		ParserList  group = releaseParser(first);
		if (null != group) {
			for (int i = 0; i < group.parsers.length; i++) {
				Parser parser = group.parsers[i];
				if (top.fols.atri.lang.Objects.equals(p, parser)) {
					group.parsers = Arrayx.remove(group.parsers, i, i + 1);
					releaseParser(first);
					return true;
				}
			}
		}
		return true;
	}
	public boolean contains(Parser p) {
		if (null == p || p.empty()) { return false; }
		char first = p.head[0];
		ParserList  group = parserList[first];
		if (null == group) {
			return false;
		}
		if (null == group.parsers) {
			parserList[first] = null;
			return false;
		}
		for (int i = 0; i < group.parsers.length; i++) {
			Parser parser = group.parsers[i];
			if (top.fols.atri.lang.Objects.equals(p, parser)) {
				return true;
			}
		}
		return false;
	}



	public static class ParseException extends RuntimeException {
		public ParseException() {
			super();
		}
		public ParseException(String message) {
			super(message);
		}
	}





	public Result<Objects[], Throwable>  parse(String content) {
		Result<Objects[], Throwable> result = new Result<Objects[], Throwable>();
		if (null == content || content.length() == 0) {
			result.set(null);
			return result;
		}
		String exception = null;
		try {
			Reader        reader = this.reader(content);
			List<Objects> records = new ArrayList<Objects>();
			for (Objects  read; reader.has();) {
				read = reader.accept();
//				System.out.println(read);
				if (reader.exceptioned()) {
					exception = reader.exception();
					break;
				}
				records.add(read);
			}
			result.set(records.toArray(new Objects[]{}));
		} catch (Throwable e) {
			result.setError(e);
		}
		if (null != exception && !result.isError()) {
			result.setError(new ParseException(exception));
		}
		return result;
	}










	public Reader reader(String content) {
		Reader reader = new Reader(content);
		return reader;
	}








	public static String intern(String str) {
		return str.intern();
	}



	public static final char LINE_SEPARATOR = '\n';
	public class Reader {
		char[] content;
		int    length;
		int    index;
		int    line;


		String exception; //exception

		public Reader(String content) {
			this(content.toCharArray());
		}
		public Reader(char[] content) {
			this.content = content;
			this.length  = content.length;
			this.index   = 0;
			this.line    = 1;
		}


		public String intern(int start, int length) {
			return CodeParser.intern(new String(content, start, length));
		}




		/**
		 * @param p you can delete to achieve exclusion
		 */
		public int find_next_object_index(Parser p) {
			int    i = this.index;
			for (; i < this.length && p.isContinuous(this.content[i]); i++) {
			}
			return i;
		}


		public String exception_cannot_parse(int index, String message) {
			int  i = 	 this.index;
			char first = this.content[i];
			ParserList   pl = parserList[first];

			String exception = 
				"parser[" + String.valueOf(pl) + "] " + " cannot parse " + (null != message && message.length() != 0 ?"\"" + message + "\"": "") + 
				"<line:" + line() + ", " + "index[" + (index - line_now_offset()) + "]" + ">" + " after that " + " " + "\"" + new String(content, index, line_now_end() - index).trim() + "\""  
				;
			return exception;
		}



		public char charAt(int index) { return content[index]; }


		/**
		 * no exception
		 */ 
		Objects accept() {
			this.exception = null;

			int  i = 	 this.index;
			char first = this.content[i];
			ParserList   pl;
			if (null != (pl = parserList[first])) {
				F: for (Parser p: pl.parsers) {
					if (i + p.head.length <= this.length) {
						for (int chi = 1; chi < p.head.length; chi++) 
							if (content[chi + i] != p.head[chi])
								continue F;
						//match head!
						Objects     result = p.readObjects(this, i);
						if (null != result)    return result;
						if (null != exception) return null;
					}
				}
			}

			this.exception = exception_cannot_parse(i,   null);
			return null;
		}


		public String  exception() { return this.exception; }
		public void    exception(String message) { this.exception = message; }
		public boolean exceptioned() { return null != exception; }


		public boolean empty() { return this.length == 0; }


		public String substring(int off, int len) {
			return new String(content, off, len);
		}

		public boolean has() {
			return index < length;
		}
		public char    now() {
			return content[index];
		}



		public boolean   has(int length) {
			return index + length < this.length;
		}
		public int       now(char[] buf, int offset, int length) {
			int size = Math.min(Math.min(length, buf.length - offset), (this.length - this.index));
			System.arraycopy(this.content, index, buf, offset, size);
			return size;
		}


		public int index() { return this.index; }


		public int seek(int newIndex) { 
			if (newIndex >= length) { newIndex = this.length; }
			if (newIndex < 0) 		{ newIndex = 0; }
			if (newIndex == this.index) {
			} else if (newIndex > this.index) {
				for (; newIndex > this.index; this.index++) {
					if (this.content[this.index] == LINE_SEPARATOR) {
						this.line++;
					}
				}
			} else {
				for (; newIndex < this.index; this.index--) {
					if (this.content[this.index] == LINE_SEPARATOR) {
						this.line--;
					}
				}
			}
			return newIndex;
		}
		public int skip(int add) {
			int newIndex = this.index + add;

			if (newIndex >= length) { newIndex = this.length; }
			if (newIndex < 0) 		{ newIndex = 0; }
			if (newIndex == this.index) {
			} else if (newIndex > this.index) {
				for (; newIndex > this.index; this.index++) {
					if (this.content[this.index] == LINE_SEPARATOR) {
						this.line++;
					}
				}
			} else {
				for (; newIndex < this.index; this.index--) {
					if (this.content[this.index] == LINE_SEPARATOR) {
						this.line--;
					}
				}
			}
			return newIndex;
		}



		public int line() { return this.line; }


		public int line_now_offset() {
			for (int i = this.index; i >= 0; i--) {
				if (this.content[i] == LINE_SEPARATOR) {
					return i + 1;
				}
			}
			return 0;
		}
		public int line_now_end() {
			for (int i = this.index; i < this.length; i++) {
				if (this.content[i] == LINE_SEPARATOR) {
					return i + 1;
				}
			}
			return this.length;
		}


		public String line_now() {
			if (length == 0) return "";
			int st = line_now_offset();
			int ed = line_now_end();
			return new String(content, st, ed - st);
		}

	}









}
