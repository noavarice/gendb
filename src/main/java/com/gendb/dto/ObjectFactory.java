
package com.gendb.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gendb.dto package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Database_QNAME = new QName("", "database");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gendb.dto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DatabaseDto }
     * 
     */
    public DatabaseDto createDatabaseDto() {
        return new DatabaseDto();
    }

    /**
     * Create an instance of {@link TableDto }
     * 
     */
    public TableDto createTableDto() {
        return new TableDto();
    }

    /**
     * Create an instance of {@link DataTypeDto }
     * 
     */
    public DataTypeDto createDataTypeDto() {
        return new DataTypeDto();
    }

    /**
     * Create an instance of {@link ColumnDto }
     * 
     */
    public ColumnDto createColumnDto() {
        return new ColumnDto();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatabaseDto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "database")
    public JAXBElement<DatabaseDto> createDatabase(DatabaseDto value) {
        return new JAXBElement<DatabaseDto>(_Database_QNAME, DatabaseDto.class, null, value);
    }

}
