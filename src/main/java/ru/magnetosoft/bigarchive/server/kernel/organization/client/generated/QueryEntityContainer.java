
package ru.magnetosoft.bigarchive.server.kernel.organization.client.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryEntityContainer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryEntityContainer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="context" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="query" type="{http://organization.magnet.magnetosoft.ru/}PreparedQueryType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryEntityContainer", propOrder = {
    "context",
    "query"
})
public class QueryEntityContainer {

    @XmlElement(namespace = "http://organization.magnet.magnetosoft.ru/")
    protected String context;
    @XmlElement(namespace = "http://organization.magnet.magnetosoft.ru/")
    protected PreparedQueryType query;

    /**
     * Gets the value of the context property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets the value of the context property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContext(String value) {
        this.context = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link PreparedQueryType }
     *     
     */
    public PreparedQueryType getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreparedQueryType }
     *     
     */
    public void setQuery(PreparedQueryType value) {
        this.query = value;
    }

}
