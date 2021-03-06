
package com.bigarchive.filemanager;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://filemanager.zdms_component.mndsc.ru/}responseHeaderType"/>
 *         &lt;element name="fileIndexEntries">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="indexedFileEntry" type="{http://filemanager.zdms_component.mndsc.ru/}indexedFileType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "fileIndexEntries"
})
@XmlRootElement(name = "getIndexedFilesResponseEnvelope")
public class GetIndexedFilesResponseEnvelope {

    @XmlElement(required = true)
    protected ResponseHeaderType header;
    @XmlElement(required = true)
    protected GetIndexedFilesResponseEnvelope.FileIndexEntries fileIndexEntries;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseHeaderType }
     *     
     */
    public ResponseHeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseHeaderType }
     *     
     */
    public void setHeader(ResponseHeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the fileIndexEntries property.
     * 
     * @return
     *     possible object is
     *     {@link GetIndexedFilesResponseEnvelope.FileIndexEntries }
     *     
     */
    public GetIndexedFilesResponseEnvelope.FileIndexEntries getFileIndexEntries() {
        return fileIndexEntries;
    }

    /**
     * Sets the value of the fileIndexEntries property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetIndexedFilesResponseEnvelope.FileIndexEntries }
     *     
     */
    public void setFileIndexEntries(GetIndexedFilesResponseEnvelope.FileIndexEntries value) {
        this.fileIndexEntries = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="indexedFileEntry" type="{http://filemanager.zdms_component.mndsc.ru/}indexedFileType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "indexedFileEntry"
    })
    public static class FileIndexEntries {

        @XmlElement(required = true)
        protected List<IndexedFileType> indexedFileEntry;

        /**
         * Gets the value of the indexedFileEntry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the indexedFileEntry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIndexedFileEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link IndexedFileType }
         * 
         * 
         */
        public List<IndexedFileType> getIndexedFileEntry() {
            if (indexedFileEntry == null) {
                indexedFileEntry = new ArrayList<IndexedFileType>();
            }
            return this.indexedFileEntry;
        }

    }

}
