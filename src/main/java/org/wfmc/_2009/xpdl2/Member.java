//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.12 at 02:47:27 PM BRT 
//


package org.wfmc._2009.xpdl2;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.wfmc.org/2009/XPDL2.2}DataTypes"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "basicType",
    "declaredType",
    "schemaType",
    "externalReference",
    "recordType",
    "unionType",
    "enumerationType",
    "arrayType",
    "listType"
})
@XmlRootElement(name = "Member")
public class Member {

    @XmlElement(name = "BasicType")
    protected BasicType basicType;
    @XmlElement(name = "DeclaredType")
    protected DeclaredType declaredType;
    @XmlElement(name = "SchemaType")
    protected SchemaType schemaType;
    @XmlElement(name = "ExternalReference")
    protected ExternalReference externalReference;
    @XmlElement(name = "RecordType")
    protected RecordType recordType;
    @XmlElement(name = "UnionType")
    protected UnionType unionType;
    @XmlElement(name = "EnumerationType")
    protected EnumerationType enumerationType;
    @XmlElement(name = "ArrayType")
    protected ArrayType arrayType;
    @XmlElement(name = "ListType")
    protected ListType listType;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the basicType property.
     * 
     * @return
     *     possible object is
     *     {@link BasicType }
     *     
     */
    public BasicType getBasicType() {
        return basicType;
    }

    /**
     * Sets the value of the basicType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicType }
     *     
     */
    public void setBasicType(BasicType value) {
        this.basicType = value;
    }

    /**
     * Gets the value of the declaredType property.
     * 
     * @return
     *     possible object is
     *     {@link DeclaredType }
     *     
     */
    public DeclaredType getDeclaredType() {
        return declaredType;
    }

    /**
     * Sets the value of the declaredType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeclaredType }
     *     
     */
    public void setDeclaredType(DeclaredType value) {
        this.declaredType = value;
    }

    /**
     * Gets the value of the schemaType property.
     * 
     * @return
     *     possible object is
     *     {@link SchemaType }
     *     
     */
    public SchemaType getSchemaType() {
        return schemaType;
    }

    /**
     * Sets the value of the schemaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SchemaType }
     *     
     */
    public void setSchemaType(SchemaType value) {
        this.schemaType = value;
    }

    /**
     * Gets the value of the externalReference property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalReference }
     *     
     */
    public ExternalReference getExternalReference() {
        return externalReference;
    }

    /**
     * Sets the value of the externalReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalReference }
     *     
     */
    public void setExternalReference(ExternalReference value) {
        this.externalReference = value;
    }

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link RecordType }
     *     
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordType }
     *     
     */
    public void setRecordType(RecordType value) {
        this.recordType = value;
    }

    /**
     * Gets the value of the unionType property.
     * 
     * @return
     *     possible object is
     *     {@link UnionType }
     *     
     */
    public UnionType getUnionType() {
        return unionType;
    }

    /**
     * Sets the value of the unionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnionType }
     *     
     */
    public void setUnionType(UnionType value) {
        this.unionType = value;
    }

    /**
     * Gets the value of the enumerationType property.
     * 
     * @return
     *     possible object is
     *     {@link EnumerationType }
     *     
     */
    public EnumerationType getEnumerationType() {
        return enumerationType;
    }

    /**
     * Sets the value of the enumerationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumerationType }
     *     
     */
    public void setEnumerationType(EnumerationType value) {
        this.enumerationType = value;
    }

    /**
     * Gets the value of the arrayType property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayType }
     *     
     */
    public ArrayType getArrayType() {
        return arrayType;
    }

    /**
     * Sets the value of the arrayType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayType }
     *     
     */
    public void setArrayType(ArrayType value) {
        this.arrayType = value;
    }

    /**
     * Gets the value of the listType property.
     * 
     * @return
     *     possible object is
     *     {@link ListType }
     *     
     */
    public ListType getListType() {
        return listType;
    }

    /**
     * Sets the value of the listType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListType }
     *     
     */
    public void setListType(ListType value) {
        this.listType = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}