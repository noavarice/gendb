
package com.gendb.dto;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypes.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="smallint"/&gt;
 *     &lt;enumeration value="int"/&gt;
 *     &lt;enumeration value="decimal"/&gt;
 *     &lt;enumeration value="char"/&gt;
 *     &lt;enumeration value="varchar"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
    @XmlEnumValue("decimal")
    DECIMAL("decimal"),
    @XmlEnumValue("char")
    CHAR("char"),
    @XmlEnumValue("varchar")
    VARCHAR("varchar");
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
