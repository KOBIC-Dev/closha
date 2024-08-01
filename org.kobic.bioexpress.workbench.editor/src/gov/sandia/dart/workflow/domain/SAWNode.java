/**
 */
package gov.sandia.dart.workflow.domain;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAW Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gov.sandia.dart.workflow.domain.SAWNode#getModel <em>Model</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.SAWNode#getComponent <em>Component</em>}</li>
 * </ul>
 *
 * @see gov.sandia.dart.workflow.domain.DomainPackage#getSAWNode()
 * @model
 * @generated
 */
public interface SAWNode extends WFNode {
	/**
	 * Returns the value of the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' attribute.
	 * @see #setModel(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getSAWNode_Model()
	 * @model unique="false"
	 * @generated
	 */
	String getModel();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.SAWNode#getModel <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' attribute.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(String value);

	/**
	 * Returns the value of the '<em><b>Component</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component</em>' attribute.
	 * @see #setComponent(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getSAWNode_Component()
	 * @model unique="false"
	 * @generated
	 */
	String getComponent();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.SAWNode#getComponent <em>Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component</em>' attribute.
	 * @see #getComponent()
	 * @generated
	 */
	void setComponent(String value);

} // SAWNode
