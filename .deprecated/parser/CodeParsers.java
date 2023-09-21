package top.fols.box.lang.parser;

import top.fols.atri.lang.Finals;

import java.math.BigDecimal;

public class CodeParsers {




	/**
	 * 变量
	 */
	public static class VarObject extends CodeParser.Objects {
		public VarObject() 				{ super(); }
		String name;

		@Override
		public String toString() {
			// TODO: Implement this method
			return "var/" + (null == name ?"": name);
		}


		@Override
		public String string() {
			return String.valueOf(name);
		}
	}
	public static class VarParser extends CodeParser.Parser {
		public VarParser()  {
			super.head(Finals.EMPTY_CHAR_ARRAY);
			super.description("parse var");
		}


		@Override
		public boolean isContinuous(int ch) {
			// TODO: Implement this method
			return 
				ch >= '0' && ch <= '9' || 
				ch >= 'a' && ch <= 'z' || 
				ch >= 'A' && ch <= 'Z' ||
				(ch == '_' || ch == '$');
		}

		@Override
		public VarParser clone() {
			// TODO: Implement this method
			VarParser np;
			np = new VarParser();
			np.head = this.head;
			np.description = this.description;
			return np;
		}


		@Override
		public VarObject readObjects(CodeParser.Reader reader, int offset) {
			// TODO: Implement this method
			int start = offset;
			int end = reader.find_next_object_index(this);
			int length = end - start;

			try {
				VarObject no = new VarObject();
				no.name = reader.intern(start, length);
				no.line(reader);

				reader.skip(length);
				return no;
			} catch (Throwable e) {
				return null;
			}
		}
	}


	/**
	 * 数字
	 */
	public static class NumberObject extends CodeParser.Objects {
		public NumberObject() 				{ super(); }
		public NumberObject(BigDecimal value) 				{ this.value = value; }
		
		BigDecimal value;

		@Override
		public String toString() {
			// TODO: Implement this method
			return "num/" + (null == value ?String.valueOf(0): value.toString());
		}

		@Override
		public String string() {
			return String.valueOf(value);
		}
		
		
		public BigDecimal value(){
			return this.value;
		}
	}
	public static class NumberParser extends CodeParser.Parser {
		public NumberParser()  {
			super.head(Finals.EMPTY_CHAR_ARRAY);
			super.description("parse number");
		}


		@Override
		public boolean isContinuous(int ch) {
			// TODO: Implement this method
			return ch >= '0' && ch <= '9' || ch == '.';
		}

		@Override
		public NumberParser clone() {
			// TODO: Implement this method
			NumberParser np;
			np = new NumberParser();
			np.head = this.head;
			np.description = this.description;
			return np;
		}


		@Override
		public NumberObject readObjects(CodeParser.Reader reader, int offset) {
			// TODO: Implement this method
			int start  = offset;
			int end    = reader.find_next_object_index(this);
			int length = end - start;

			try {
				BigDecimal value = new BigDecimal(new String(reader.content, start, length));
				NumberObject no = new NumberObject();
				no.value = value;
				no.line(reader);

				reader.skip(length);
				return no;
			} catch (Throwable e) {
				return null;
			}
		}
	}










	/**
	 * 制表符
	 */
	public static class TabsObject extends CodeParser.Objects {
		public TabsObject() 				{ super(); }
		int length;

		@Override
		public String toString() {
			// TODO: Implement this method
			return "tabs/" + (length);
		}


		String value;
		@Override
		public String string() {
			return String.valueOf(value);
		}
	}
	public static class TabsParser extends CodeParser.Parser {
		public TabsParser()  {
			super.head(Finals.EMPTY_CHAR_ARRAY);
			super.description("parse tabs");
		}


		@Override
		public boolean isContinuous(int ch) {
			// TODO: Implement this method
			return 
				ch <= 0x20 && ch != '\n';
		}

		@Override
		public TabsParser clone() {
			// TODO: Implement this method
			TabsParser np;
			np = new TabsParser();
			np.head = this.head;
			np.description = this.description;
			return np;
		}


		@Override
		public TabsObject readObjects(CodeParser.Reader reader, int offset) {
			// TODO: Implement this method
			int start = offset;
			int end = reader.find_next_object_index(this);
			int length = end - start;

			try {
				TabsObject no = new TabsObject();
				no.value = reader.substring(start, length);
				no.length = length;
				no.line(reader);

				reader.skip(length);
				return no;
			} catch (Throwable e) {
				return null;
			}
		}
	}


	/*
	 * 换行符
	 */
	public static class LineObject extends CodeParser.Objects {
		public LineObject() 				{ super(); }
		int length;

		@Override
		public String toString() {
			// TODO: Implement this method
			return "line/" + (length) + "(" + ":" + line() + ")";
		}


		String value;
		@Override
		public String string() {
			return String.valueOf(value);
		}
	}
	public static class LineParser extends CodeParser.Parser {
		public LineParser()  {
			super.head(Finals.EMPTY_CHAR_ARRAY);
			super.description("parse line");
		}


		@Override
		public boolean isContinuous(int ch) {
			// TODO: Implement this method
			return 
				ch == '\n';
		}

		@Override
		public LineParser clone() {
			// TODO: Implement this method
			LineParser np;
			np = new LineParser();
			np.head = this.head;
			np.description = this.description;
			return np;
		}


		@Override
		public LineObject readObjects(CodeParser.Reader reader, int offset) {
			// TODO: Implement this method
			int start = offset;
			int end   = reader.find_next_object_index(this);
			int length = end - start;

			try {
				LineObject no = new LineObject();
				no.value = reader.substring(start, length);
				no.length = length;
				no.line(reader);

				reader.skip(length);
				return no;
			} catch (Throwable e) {
				return null;
			}
		}
	}





	/*
	 * 符号 
	 */
	public static class SingleSymbolObject extends CodeParser.Objects {
		public SingleSymbolObject() 				{ super(); }
		char symbol;

		@Override
		public String toString() {
			// TODO: Implement this method
			return "symbol/" + (symbol);
		}

		@Override
		public String string() {
			return String.valueOf(symbol);
		}
	}
	public static class SingleSymbolObjectParser extends CodeParser.Parser {
		char symbol;

		public SingleSymbolObjectParser(char symbol)  {
			super.head(new char[]{symbol});
			super.description("parse symbol");
			this.symbol = symbol;
		}


		@Override
		public boolean isContinuous(int ch) {
			// TODO: Implement this method
			return ch == symbol;
		}

		@Override
		public SingleSymbolObjectParser clone() {
			// TODO: Implement this method
			SingleSymbolObjectParser np;
			np = new SingleSymbolObjectParser(this.symbol);
			np.head = this.head;
			np.description = this.description;
			return np;
		}


		@Override
		public SingleSymbolObject readObjects(CodeParser.Reader reader, int offset) {
			// TODO: Implement this method
			int start = offset;
			int length = 1;

			try {
				SingleSymbolObject no = new SingleSymbolObject();
				no.symbol = reader.charAt(start);
				no.line(reader);

				reader.skip(length);
				return no;
			} catch (Throwable e) {
				return null;
			}
		}
	}






	/**
	 * 关键词
	 */
	public static class KeywordObject extends CodeParser.Objects {
		public KeywordObject() 				{ super(); }
		String keyword;

		@Override
		public String toString() {
			// TODO: Implement this method
			return "keyword/" + (keyword);
		}


		@Override
		public String string() {
			return String.valueOf(keyword);
		}
	}
	public static class KeywordObjectParser extends CodeParser.Parser {

		String keywords;
		public KeywordObjectParser(String keywords)  {
			super.head((CodeParser.intern(this.keywords = keywords)).toCharArray());
			super.description("parse keyword");
		}



		@Override
		public boolean isContinuous(int ch) {
			// TODO: Implement this method
			for (char headCh: head) {
				if (headCh == ch) return true;
			}
			return false;
		}

		@Override
		public KeywordObjectParser clone() {
			// TODO: Implement this method
			KeywordObjectParser np;
			np = new KeywordObjectParser(this.keywords);
			np.head = this.head;
			np.description = this.description;
			return np;
		}


		@Override
		public KeywordObject readObjects(CodeParser.Reader reader, int offset) {
			// TODO: Implement this method
			int start = offset;
			int end   = reader.find_next_object_index(this);
			int length = end - start;

			try {
				KeywordObject no = new KeywordObject();
				no.keyword = reader.intern(start, length);
				no.line(reader);

				reader.skip(length);
				return no;
			} catch (Throwable e) {
				return null;
			}
		}
	}

	
}
