/**
 */
package gov.sandia.dart.workflow.domain;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gov.sandia.dart.workflow.domain.NamedObject#getId <em>Id</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.NamedObject#getName <em>Name</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.NamedObject#getDesc <em>Desc</em>}</li>
 * </ul>
 *
 * @see gov.sandia.dart.workflow.domain.DomainPackage#getNamedObject()
 * @model abstract="true"
 * @generated
 */
public interface NamedObject extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getNamedObject_Id()
	 * @model unique="false"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.NamedObject#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getNamedObject_Name()
	 * @model unique="false"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.NamedObject#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Desc</em>' attribute.
	 * @see #setDesc(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getNamedObject_Desc()
	 * @model unique="false"
	 * @generated
	 */
	String getDesc();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.NamedObject#getDesc <em>Desc</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Desc</em>' attribute.
	 * @see #getDesc()
	 * @generated
	 */
	void setDesc(String value);

} // NamedObject
