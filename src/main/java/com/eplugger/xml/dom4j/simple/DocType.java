package com.eplugger.xml.dom4j.simple;

public class DocType {
    String elementName;
    String publicID;
    String systemID;

    public DocType() {
    }

    public DocType(String elementName, String systemID) {
        this.elementName = elementName;
        this.systemID = systemID;
    }

    public DocType(String elementName, String publicID, String systemID) {
        this.elementName = elementName;
        this.publicID = publicID;
        this.systemID = systemID;
    }
}