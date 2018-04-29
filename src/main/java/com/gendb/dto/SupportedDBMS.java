
package com.gendb.dto;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SupportedDBMS.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SupportedDBMS">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="mysql"/>
 *     &lt;enumeration value="postgres"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SupportedDBMS")
@XmlEnum
public enum SupportedDBMS {

    @XmlEnumValue("mysql")
    MYSQL("mysql"),
    @XmlEnumValue("postgres")
    POSTGRES("postgres");
    private final String value;

    SupportedDBMS(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SupportedDBMS fromValue(String v) {
        for (SupportedDBMS c: SupportedDBMS.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
