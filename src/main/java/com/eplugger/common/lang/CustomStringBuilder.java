package com.eplugger.common.lang;

import static com.google.common.base.Preconditions.checkNotNull;

public class CustomStringBuilder extends AbstractStringBuilder {
	private StringBuilder sb;

	public CustomStringBuilder() {
		this(new StringBuilder());
	}

	public CustomStringBuilder(StringBuilder sb) {
		this.sb = sb;
	}

	public CustomStringBuilder insert(int offset, String str) {
		checkNotNull(sb);
		sb.insert(offset, str);
		return this;
	}
	
	public int length() {
		checkNotNull(sb);
		return sb.length();
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
