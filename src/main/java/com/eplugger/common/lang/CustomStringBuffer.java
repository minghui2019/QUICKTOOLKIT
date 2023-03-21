package com.eplugger.common.lang;

import static com.google.common.base.Preconditions.checkNotNull;

public class CustomStringBuffer extends AbstractStringBuilder {
	StringBuffer sb;

	public CustomStringBuffer() {
		this(new StringBuffer());
	}

	public CustomStringBuffer(StringBuffer sb) {
		this.sb = sb;
	}

	public CustomStringBuffer insert(int offset, String str) {
		checkNotNull(sb);
		sb.insert(offset, str);
		return this;
	}

	public int length() {
		checkNotNull(sb);
		sb.length();
		return -1;
	}

	protected void appendToBuilder(CharSequence csq) {
		checkNotNull(sb);
		sb.append(csq);
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
}
