/**
 */
package gov.sandia.dart.workflow.domain;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gov.sandia.dart.workflow.domain.Property#getType <em>Type</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.Property#getValue <em>Value</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.Property#getNode <em>Node</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.Property#isAdvanced <em>Advanced</em>}</li>
 * </ul>
 *
 * @see gov.sandia.dart.workflow.domain.DomainPackage#getProperty()
 * @model
 * @generated
 */
public interface Property extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getProperty_Type()
	 * @model default="" unique="false"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.Property#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getProperty_Value()
	 * @model unique="false"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.Property#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Node</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.NamedObjectWithProperties#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Node</em>' reference.
	 * @see #setNode(NamedObjectWithProperties)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getProperty_Node()
	 * @see gov.sandia.dart.workflow.domain.NamedObjectWithProperties#getProperties
	 * @model opposite="properties"
	 * @generated
	 */
	NamedObjectWithProperties getNode();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.Property#getNode <em>Node</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Node</em>' reference.
	 * @see #getNode()
	 * @generated
	 */
	void setNode(NamedObjectWithProperties value);

	/**
	 * Returns the value of the '<em><b>Advanced</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Advanced</em>' attribute.
	 * @see #setAdvanced(boolean)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getProperty_Advanced()
	 * @model default="false" unique="false"
	 * @generated
	 */
	boolean isAdvanced();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.Property#isAdvanced <em>Advanced</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Advanced</em>' attribute.
	 * @see #isAdvanced()
	 * @generated
	 */
	void setAdvanced(boolean value);

} // Property
