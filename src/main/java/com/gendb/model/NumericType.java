//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0-b170531.0717 
//         See <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
//         Any modifications to this file will be lost upon recompilation of the source schema. 
//         Generated on: 2018.04.27 at 09:34:17 PM MSK 
//


package com.gendb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NumericType complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NumericType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="type" type="{}NumericTypes" /&gt;
 *       &lt;attribute name="unsigned" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumericType")
public class NumericType {

    @XmlAttribute(name = "type")
    protected NumericTypes type;
    @XmlAttribute(name = "unsigned")
    protected Boolean unsigned;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link NumericTypes }
     *     
     */
    public NumericTypes getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumericTypes }
     *     
     */
    public void setType(NumericTypes value) {
        this.type = value;
    }

    /**
     * Gets the value of the unsigned property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUnsigned() {
        if (unsigned == null) {
            return false;
        } else {
            return unsigned;
        }
    }

    /**
     * Sets the value of the unsigned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUnsigned(Boolean value) {
        this.unsigned = value;
    }

}
