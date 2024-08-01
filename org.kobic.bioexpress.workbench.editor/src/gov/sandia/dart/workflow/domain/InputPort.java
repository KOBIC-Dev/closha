/**
 */
package gov.sandia.dart.workflow.domain;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gov.sandia.dart.workflow.domain.InputPort#getNode <em>Node</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.InputPort#getArcs <em>Arcs</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.InputPort#isTriggerOnly <em>Trigger Only</em>}</li>
 * </ul>
 *
 * @see gov.sandia.dart.workflow.domain.DomainPackage#getInputPort()
 * @model
 * @generated
 */
public interface InputPort extends Port {
	/**
	 * Returns the value of the '<em><b>Node</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.WFNode#getInputPorts <em>Input Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Node</em>' reference.
	 * @see #setNode(WFNode)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getInputPort_Node()
	 * @see gov.sandia.dart.workflow.domain.WFNode#getInputPorts
	 * @model opposite="inputPorts"
	 * @generated
	 */
	WFNode getNode();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.InputPort#getNode <em>Node</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Node</em>' reference.
	 * @see #getNode()
	 * @generated
	 */
	void setNode(WFNode value);

	/**
	 * Returns the value of the '<em><b>Arcs</b></em>' reference list.
	 * The list contents are of type {@link gov.sandia.dart.workflow.domain.WFArc}.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.WFArc#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arcs</em>' reference list.
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getInputPort_Arcs()
	 * @see gov.sandia.dart.workflow.domain.WFArc#getTarget
	 * @model opposite="target"
	 * @generated
	 */
	EList<WFArc> getArcs();

	/**
	 * Returns the value of the '<em><b>Trigger Only</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trigger Only</em>' attribute.
	 * @see #setTriggerOnly(boolean)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getInputPort_TriggerOnly()
	 * @model default="false" unique="false"
	 * @generated
	 */
	boolean isTriggerOnly();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.InputPort#isTriggerOnly <em>Trigger Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trigger Only</em>' attribute.
	 * @see #isTriggerOnly()
	 * @generated
	 */
	void setTriggerOnly(boolean value);

} // InputPort
