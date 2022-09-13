package com.eplugger.common.lang;

import java.io.IOException;

public class DefaultStringBuilder {
	Appendable sb;

	public DefaultStringBuilder() {
		this(new StringBuffer());
	}

	public DefaultStringBuilder(Appendable sb) {
		this.sb = sb;
	}
	
	
	public DefaultStringBuilder append(CharSequence csq) {
		try {
			sb.append(csq);
		} catch (IOException e) {
		}
		return this;
	}
	
	public DefaultStringBuilder appendln(CharSequence csq) {
		try {
			sb.append(csq).append(StringUtils.CRLF);
		} catch (IOException e) {
		}
		return this;
	}
	
	public DefaultStringBuilder appendln() {
		try {
			sb.append(StringUtils.CRLF);
		} catch (IOException e) {
		}
		return this;
	}
	
	public DefaultStringBuilder insert(int offset, String str) {
		if (sb instanceof StringBuffer) {
			((StringBuffer) sb).insert(offset, str);
		}
		if (sb instanceof StringBuilder) {
			((StringBuilder) sb).insert(offset, str);
		}
		return this;
	}
	
	public int length() {
		if (sb instanceof StringBuffer) {
			return ((StringBuffer) sb).length();
		}
		if (sb instanceof StringBuilder) {
			return ((StringBuilder) sb).length();
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
}
