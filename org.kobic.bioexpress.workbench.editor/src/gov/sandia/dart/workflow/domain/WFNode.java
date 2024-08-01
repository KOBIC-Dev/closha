/**
 */
package gov.sandia.dart.workflow.domain;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>WF Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getInputPorts <em>Input Ports</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getOutputPorts <em>Output Ports</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#isStart <em>Start</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getType <em>Type</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getLabel <em>Label</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getUrl <em>Url</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getVersion <em>Version</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getStatus <em>Status</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#isPublic <em>Public</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#isMulticore <em>Multicore</em>}</li>
 *   <li>{@link gov.sandia.dart.workflow.domain.WFNode#getConductors <em>Conductors</em>}</li>
 * </ul>
 *
 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode()
 * @model
 * @generated
 */
public interface WFNode extends NamedObjectWithProperties {
	/**
	 * Returns the value of the '<em><b>Input Ports</b></em>' reference list.
	 * The list contents are of type {@link gov.sandia.dart.workflow.domain.InputPort}.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.InputPort#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Ports</em>' reference list.
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_InputPorts()
	 * @see gov.sandia.dart.workflow.domain.InputPort#getNode
	 * @model opposite="node"
	 * @generated
	 */
	EList<InputPort> getInputPorts();

	/**
	 * Returns the value of the '<em><b>Output Ports</b></em>' reference list.
	 * The list contents are of type {@link gov.sandia.dart.workflow.domain.OutputPort}.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.OutputPort#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Ports</em>' reference list.
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_OutputPorts()
	 * @see gov.sandia.dart.workflow.domain.OutputPort#getNode
	 * @model opposite="node"
	 * @generated
	 */
	EList<OutputPort> getOutputPorts();

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(boolean)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Start()
	 * @model default="false" unique="false"
	 * @generated
	 */
	boolean isStart();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#isStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #isStart()
	 * @generated
	 */
	void setStart(boolean value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Type()
	 * @model unique="false"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Label()
	 * @model default="" unique="false"
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Url</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Url</em>' attribute.
	 * @see #setUrl(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Url()
	 * @model default="" unique="false"
	 * @generated
	 */
	String getUrl();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#getUrl <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Url</em>' attribute.
	 * @see #getUrl()
	 * @generated
	 */
	void setUrl(String value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Version()
	 * @model default="" unique="false"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Status</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' attribute.
	 * @see #setStatus(String)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Status()
	 * @model default="" unique="false"
	 * @generated
	 */
	String getStatus();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#getStatus <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' attribute.
	 * @see #getStatus()
	 * @generated
	 */
	void setStatus(String value);

	/**
	 * Returns the value of the '<em><b>Public</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Public</em>' attribute.
	 * @see #setPublic(boolean)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Public()
	 * @model default="false" unique="false"
	 * @generated
	 */
	boolean isPublic();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#isPublic <em>Public</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Public</em>' attribute.
	 * @see #isPublic()
	 * @generated
	 */
	void setPublic(boolean value);

	/**
	 * Returns the value of the '<em><b>Multicore</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multicore</em>' attribute.
	 * @see #setMulticore(boolean)
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Multicore()
	 * @model default="false" unique="false"
	 * @generated
	 */
	boolean isMulticore();

	/**
	 * Sets the value of the '{@link gov.sandia.dart.workflow.domain.WFNode#isMulticore <em>Multicore</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multicore</em>' attribute.
	 * @see #isMulticore()
	 * @generated
	 */
	void setMulticore(boolean value);

	/**
	 * Returns the value of the '<em><b>Conductors</b></em>' reference list.
	 * The list contents are of type {@link gov.sandia.dart.workflow.domain.Conductor}.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.Conductor#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conductors</em>' reference list.
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getWFNode_Conductors()
	 * @see gov.sandia.dart.workflow.domain.Conductor#getNode
	 * @model opposite="node"
	 * @generated
	 */
	EList<Conductor> getConductors();

} // WFNode
