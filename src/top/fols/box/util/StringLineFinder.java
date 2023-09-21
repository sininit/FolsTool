package top.fols.box.util;

import top.fols.atri.io.Delimiter;
import top.fols.atri.lang.Finals;

public class StringLineFinder {
	static final Delimiter.ICharsDelimiter lineDelimiter = Finals.LineSeparator.lineCharDelimit();
	static final char[][] SEPARATOR = lineDelimiter.cloneSeparators();
	static final int OFFSET_LINE  = 1;

	CharSequence content;
	int length;

	int lastFindIndex;
	int lastFindLine;

	public StringLineFinder(CharSequence content) {
		this.content = content;
		this.length  = content.length();
		this.reset();
	}


	public int find(int index) {
		if (index > lastFindIndex) {
			int l = lastFindLine;
			int i = lastFindIndex;
			while (i <= index) {
				int sIndex = lineDelimiter.assertSeparator(content, i, index);
				if (sIndex != -1) {
					l++;
					i += SEPARATOR[sIndex].length;
				} else {
					i++;
				}
			}
			lastFindLine  = l;
			lastFindIndex = i;
			return l;
		} else if (index == lastFindIndex) {
			return lastFindLine;
		} 
		int l = OFFSET_LINE;
		int i = 0;
		while (i <= index) {
			int sIndex = lineDelimiter.assertSeparator(content, i, index);
			if (sIndex != -1) {
				l++;
				i += SEPARATOR[sIndex].length;
			} else {
				i++;
			}
		}
		return l;
	}
	public void reset(){
		lastFindIndex = 0;
	    lastFindLine  = OFFSET_LINE;
	}

}

