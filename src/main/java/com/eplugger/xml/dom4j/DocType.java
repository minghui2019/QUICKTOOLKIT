package com.eplugger.xml.dom4j;

import org.dom4j.tree.DefaultDocumentType;

/**
 * <p>
 * <code>DocumentType</code> is the DOM4J default implementation of an XML
 * document type.
 * </p>
 */
public class DocType extends DefaultDocumentType {
    private static final long serialVersionUID = -4242541910505181668L;

    /** The root element name of the document type */
    protected String elementName;

    /** Holds value of property publicID. */
    private String publicID;

    /** Holds value of property systemID. */
    private String systemID;

    public DocType() {
    }

    /**
     * <p>
     * This will create a new <code>DocumentType</code> with a reference to the
     * external DTD
     * </p>
     * 
     * @param elementName is the root element name of the document type
     * @param systemID    is the system ID of the external DTD
     */
    public DocType(String elementName, String systemID) {
        this.elementName = elementName;
        this.systemID = systemID;
    }

    /**
     * <p>
     * This will create a new <code>DocumentType</code> with a reference to the
     * external DTD
     * </p>
     * 
     * @param elementName is the root element name of the document type
     * @param publicID    is the public ID of the DTD
     * @param systemID    is the system ID of the DTD
     */
    public DocType(String elementName, String publicID, String systemID) {
        this.elementName = elementName;
        this.publicID = publicID;
        this.systemID = systemID;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the public ID of the document type
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * Sets the public ID of the document type
     * 
     * @param publicID DOCUMENT ME!
     */
    public void setPublicID(String publicID) {
        this.publicID = publicID;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the system ID of the document type
     */
    public String getSystemID() {
        return systemID;
    }

    /**
     * Sets the system ID of the document type
     * 
     * @param systemID DOCUMENT ME!
     */
    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }
}