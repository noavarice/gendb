
package com.gendb.dto;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="smallint"/>
 *     &lt;enumeration value="int"/>
 *     &lt;enumeration value="bigint"/>
 *     &lt;enumeration value="numeric"/>
 *     &lt;enumeration value="decimal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypes")
@XmlEnum
public enum DataTypes {

    @XmlEnumValue("smallint")
    SMALLINT("smallint"),
    @XmlEnumValue("int")
    INT("int"),
    @XmlEnumValue("bigint")
    BIGINT("bigint"),
    @XmlEnumValue("numeric")
    NUMERIC("numeric"),
    @XmlEnumValue("decimal")
    DECIMAL("decimal");
    private final String value;

    DataTypes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypes fromValue(String v) {
        for (DataTypes c: DataTypes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
