/**
 */
package gov.sandia.dart.workflow.domain;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Object With Properties</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gov.sandia.dart.workflow.domain.NamedObjectWithProperties#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see gov.sandia.dart.workflow.domain.DomainPackage#getNamedObjectWithProperties()
 * @model abstract="true"
 * @generated
 */
public interface NamedObjectWithProperties extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Properties</b></em>' reference list.
	 * The list contents are of type {@link gov.sandia.dart.workflow.domain.Property}.
	 * It is bidirectional and its opposite is '{@link gov.sandia.dart.workflow.domain.Property#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' reference list.
	 * @see gov.sandia.dart.workflow.domain.DomainPackage#getNamedObjectWithProperties_Properties()
	 * @see gov.sandia.dart.workflow.domain.Property#getNode
	 * @model opposite="node"
	 * @generated
	 */
	EList<Property> getProperties();

} // NamedObjectWithProperties
