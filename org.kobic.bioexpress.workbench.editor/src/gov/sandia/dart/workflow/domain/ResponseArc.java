/**
 */
package gov.sandia.dart.workflow.domain;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Response Arc</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gov.sandia.dart.workflow.domain.ResponseArc#getSource <em>Source</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.ResponseArc#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see gov.sandia.dart.workflow.domain.DomainPackage#getResponseArc()
 * @model
 * @generated
 */
public interface ResponseArc extends Arc {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.OutputPort#getResponseArcs <em>Response Arcs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(OutputPort)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getResponseArc_Source()
	 * @see gov.sandia.dart.workflow.domain.OutputPort#getResponseArcs
	 * @model opposite="responseArcs"
	 * @generated
	 */
	OutputPort getSource();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.ResponseArc#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(OutputPort value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.Response#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(Response)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getResponseArc_Target()
	 * @see gov.sandia.dart.workflow.domain.Response#getSource
	 * @model opposite="source"
	 * @generated
	 */
	Response getTarget();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.ResponseArc#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Response value);

} // ResponseArc
