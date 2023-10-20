package com.eplugger.common.lang;

public abstract class AbstractStringBuilder {
//    protected String lineSeparator = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
    protected String lineSeparator = System.lineSeparator();
    public AbstractStringBuilder append(CharSequence csq) {
        appendToBuilder(csq);
        return this;
    }

    public AbstractStringBuilder appendln() {
        newLine();
        return this;
    }

    public AbstractStringBuilder appendln(CharSequence csq) {
        appendToBuilder(csq);
        newLine();
        return this;
    }

    protected void newLine() {
        appendToBuilder(lineSeparator);
    }

    protected abstract void appendToBuilder(CharSequence csq);
}
