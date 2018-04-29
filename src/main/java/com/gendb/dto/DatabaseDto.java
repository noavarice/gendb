
package com.gendb.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DatabaseDto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatabaseDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="table" type="{}TableDto" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{}Attribute" />
 *       &lt;attribute name="dbms" use="required" type="{}SupportedDBMS" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatabaseDto", propOrder = {
    "table"
})
public class DatabaseDto {

    @XmlElement(required = true)
    protected List<TableDto> table;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "dbms", required = true)
    protected SupportedDBMS dbms;

    /**
     * Gets the value of the table property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the table property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TableDto }
     * 
     * 
     */
    public List<TableDto> getTable() {
        if (table == null) {
            table = new ArrayList<TableDto>();
        }
        return this.table;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the dbms property.
     * 
     * @return
     *     possible object is
     *     {@link SupportedDBMS }
     *     
     */
    public SupportedDBMS getDbms() {
        return dbms;
    }

    /**
     * Sets the value of the dbms property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupportedDBMS }
     *     
     */
    public void setDbms(SupportedDBMS value) {
        this.dbms = value;
    }

}
