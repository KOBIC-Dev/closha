/**
 */
package gov.sandia.dart.workflow.domain;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.sandia.dart.workflow.domain.DomainFactory
 * @model kind="package"
 * @generated
 */
public interface DomainPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "domain";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://kobic.or.kr/bioexpress/domain";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.kobic.bioexpress.workbench.domain";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DomainPackage eINSTANCE = gov.sandia.dart.workflow.domain.impl.DomainPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.NamedObjectImpl <em>Named Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.NamedObjectImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getNamedObject()
	 * @generated
	 */
	int NAMED_OBJECT = 7;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT__DESC = 2;

	/**
	 * The number of structural features of the '<em>Named Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Named Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.ResponseImpl <em>Response</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.ResponseImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getResponse()
	 * @generated
	 */
	int RESPONSE = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE__ID = NAMED_OBJECT__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE__NAME = NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE__DESC = NAMED_OBJECT__DESC;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE__TYPE = NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE__SOURCE = NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Response</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_FEATURE_COUNT = NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Response</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_OPERATION_COUNT = NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.NamedObjectWithPropertiesImpl <em>Named Object With Properties</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.NamedObjectWithPropertiesImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getNamedObjectWithProperties()
	 * @generated
	 */
	int NAMED_OBJECT_WITH_PROPERTIES = 10;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_WITH_PROPERTIES__ID = NAMED_OBJECT__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_WITH_PROPERTIES__NAME = NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_WITH_PROPERTIES__DESC = NAMED_OBJECT__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_WITH_PROPERTIES__PROPERTIES = NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Named Object With Properties</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT = NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Named Object With Properties</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_WITH_PROPERTIES_OPERATION_COUNT = NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.WFNodeImpl <em>WF Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.WFNodeImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getWFNode()
	 * @generated
	 */
	int WF_NODE = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__ID = NAMED_OBJECT_WITH_PROPERTIES__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__NAME = NAMED_OBJECT_WITH_PROPERTIES__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__DESC = NAMED_OBJECT_WITH_PROPERTIES__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__PROPERTIES = NAMED_OBJECT_WITH_PROPERTIES__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Input Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__INPUT_PORTS = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__OUTPUT_PORTS = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__START = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__TYPE = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__LABEL = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__URL = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__VERSION = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__STATUS = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Public</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__PUBLIC = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Multicore</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__MULTICORE = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Conductors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE__CONDUCTORS = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>WF Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE_FEATURE_COUNT = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 11;

	/**
	 * The number of operations of the '<em>WF Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_NODE_OPERATION_COUNT = NAMED_OBJECT_WITH_PROPERTIES_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.ArcImpl <em>Arc</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.ArcImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getArc()
	 * @generated
	 */
	int ARC = 15;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC__ID = NAMED_OBJECT_WITH_PROPERTIES__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC__NAME = NAMED_OBJECT_WITH_PROPERTIES__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC__DESC = NAMED_OBJECT_WITH_PROPERTIES__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC__PROPERTIES = NAMED_OBJECT_WITH_PROPERTIES__PROPERTIES;

	/**
	 * The number of structural features of the '<em>Arc</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC_FEATURE_COUNT = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Arc</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC_OPERATION_COUNT = NAMED_OBJECT_WITH_PROPERTIES_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.WFArcImpl <em>WF Arc</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.WFArcImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getWFArc()
	 * @generated
	 */
	int WF_ARC = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC__ID = ARC__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC__NAME = ARC__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC__DESC = ARC__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC__PROPERTIES = ARC__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC__SOURCE = ARC_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC__TARGET = ARC_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>WF Arc</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC_FEATURE_COUNT = ARC_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>WF Arc</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WF_ARC_OPERATION_COUNT = ARC_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.PropertyImpl <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.PropertyImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getProperty()
	 * @generated
	 */
	int PROPERTY = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__ID = NAMED_OBJECT__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__NAME = NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__DESC = NAMED_OBJECT__DESC;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__TYPE = NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__VALUE = NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Node</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__NODE = NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Advanced</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__ADVANCED = NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_FEATURE_COUNT = NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_OPERATION_COUNT = NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.PortImpl <em>Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.PortImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getPort()
	 * @generated
	 */
	int PORT = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__ID = NAMED_OBJECT_WITH_PROPERTIES__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__NAME = NAMED_OBJECT_WITH_PROPERTIES__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DESC = NAMED_OBJECT_WITH_PROPERTIES__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__PROPERTIES = NAMED_OBJECT_WITH_PROPERTIES__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__TYPE = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_OPERATION_COUNT = NAMED_OBJECT_WITH_PROPERTIES_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.InputPortImpl <em>Input Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.InputPortImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getInputPort()
	 * @generated
	 */
	int INPUT_PORT = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__ID = PORT__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__NAME = PORT__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__DESC = PORT__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__PROPERTIES = PORT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__TYPE = PORT__TYPE;

	/**
	 * The feature id for the '<em><b>Node</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__NODE = PORT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arcs</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__ARCS = PORT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Trigger Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__TRIGGER_ONLY = PORT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Input Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT_FEATURE_COUNT = PORT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Input Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT_OPERATION_COUNT = PORT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.OutputPortImpl <em>Output Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.OutputPortImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getOutputPort()
	 * @generated
	 */
	int OUTPUT_PORT = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__ID = PORT__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__NAME = PORT__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__DESC = PORT__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__PROPERTIES = PORT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__TYPE = PORT__TYPE;

	/**
	 * The feature id for the '<em><b>Node</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__NODE = PORT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arcs</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__ARCS = PORT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Response Arcs</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__RESPONSE_ARCS = PORT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Output Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT_FEATURE_COUNT = PORT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Output Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT_OPERATION_COUNT = PORT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.SAWNodeImpl <em>SAW Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.SAWNodeImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getSAWNode()
	 * @generated
	 */
	int SAW_NODE = 8;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__ID = WF_NODE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__NAME = WF_NODE__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__DESC = WF_NODE__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__PROPERTIES = WF_NODE__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Input Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__INPUT_PORTS = WF_NODE__INPUT_PORTS;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__OUTPUT_PORTS = WF_NODE__OUTPUT_PORTS;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__START = WF_NODE__START;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__TYPE = WF_NODE__TYPE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__LABEL = WF_NODE__LABEL;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__URL = WF_NODE__URL;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__VERSION = WF_NODE__VERSION;

	/**
	 * The feature id for the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__STATUS = WF_NODE__STATUS;

	/**
	 * The feature id for the '<em><b>Public</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__PUBLIC = WF_NODE__PUBLIC;

	/**
	 * The feature id for the '<em><b>Multicore</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__MULTICORE = WF_NODE__MULTICORE;

	/**
	 * The feature id for the '<em><b>Conductors</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__CONDUCTORS = WF_NODE__CONDUCTORS;

	/**
	 * The feature id for the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__MODEL = WF_NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Component</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE__COMPONENT = WF_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>SAW Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE_FEATURE_COUNT = WF_NODE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>SAW Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAW_NODE_OPERATION_COUNT = WF_NODE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.NoteImpl <em>Note</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.NoteImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getNote()
	 * @generated
	 */
	int NOTE = 9;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__TEXT = 0;

	/**
	 * The feature id for the '<em><b>Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__COLOR = 1;

	/**
	 * The feature id for the '<em><b>Draw Border And Background</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__DRAW_BORDER_AND_BACKGROUND = 2;

	/**
	 * The number of structural features of the '<em>Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.ResponseArcImpl <em>Response Arc</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.ResponseArcImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getResponseArc()
	 * @generated
	 */
	int RESPONSE_ARC = 11;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC__ID = ARC__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC__NAME = ARC__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC__DESC = ARC__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC__PROPERTIES = ARC__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC__SOURCE = ARC_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC__TARGET = ARC_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Response Arc</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC_FEATURE_COUNT = ARC_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Response Arc</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESPONSE_ARC_OPERATION_COUNT = ARC_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.ConductorImpl <em>Conductor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.ConductorImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getConductor()
	 * @generated
	 */
	int CONDUCTOR = 12;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDUCTOR__ID = NAMED_OBJECT_WITH_PROPERTIES__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDUCTOR__NAME = NAMED_OBJECT_WITH_PROPERTIES__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDUCTOR__DESC = NAMED_OBJECT_WITH_PROPERTIES__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDUCTOR__PROPERTIES = NAMED_OBJECT_WITH_PROPERTIES__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Node</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDUCTOR__NODE = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Conductor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDUCTOR_FEATURE_COUNT = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Conductor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDUCTOR_OPERATION_COUNT = NAMED_OBJECT_WITH_PROPERTIES_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.ParameterImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 13;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__ID = NAMED_OBJECT__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__DESC = NAMED_OBJECT__DESC;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE = NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__VALUE = NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_OPERATION_COUNT = NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.RunnerImpl <em>Runner</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.RunnerImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getRunner()
	 * @generated
	 */
	int RUNNER = 14;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNNER__ID = NAMED_OBJECT_WITH_PROPERTIES__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNNER__NAME = NAMED_OBJECT_WITH_PROPERTIES__NAME;

	/**
	 * The feature id for the '<em><b>Desc</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNNER__DESC = NAMED_OBJECT_WITH_PROPERTIES__DESC;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNNER__PROPERTIES = NAMED_OBJECT_WITH_PROPERTIES__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNNER__TYPE = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Runner</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNNER_FEATURE_COUNT = NAMED_OBJECT_WITH_PROPERTIES_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Runner</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNNER_OPERATION_COUNT = NAMED_OBJECT_WITH_PROPERTIES_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.sandia.dart.workflow.domain.impl.ImageImpl <em>Image</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.sandia.dart.workflow.domain.impl.ImageImpl
	 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getImage()
	 * @generated
	 */
	int IMAGE = 16;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE__TEXT = 0;

	/**
	 * The feature id for the '<em><b>Zoom To Fit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE__ZOOM_TO_FIT = 1;

	/**
	 * The feature id for the '<em><b>Draw Border</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE__DRAW_BORDER = 2;

	/**
	 * The number of structural features of the '<em>Image</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Image</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Response <em>Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Response</em>'.
	 * @see gov.sandia.dart.workflow.domain.Response
	 * @generated
	 */
	EClass getResponse();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Response#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see gov.sandia.dart.workflow.domain.Response#getType()
	 * @see #getResponse()
	 * @generated
	 */
	EAttribute getResponse_Type();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.Response#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Source</em>'.
	 * @see gov.sandia.dart.workflow.domain.Response#getSource()
	 * @see #getResponse()
	 * @generated
	 */
	EReference getResponse_Source();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.WFNode <em>WF Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>WF Node</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode
	 * @generated
	 */
	EClass getWFNode();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.WFNode#getInputPorts <em>Input Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Input Ports</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getInputPorts()
	 * @see #getWFNode()
	 * @generated
	 */
	EReference getWFNode_InputPorts();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.WFNode#getOutputPorts <em>Output Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Output Ports</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getOutputPorts()
	 * @see #getWFNode()
	 * @generated
	 */
	EReference getWFNode_OutputPorts();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#isStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#isStart()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Start();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getType()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Type();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getLabel()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Label();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#getUrl <em>Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Url</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getUrl()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Url();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getVersion()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Version();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#getStatus <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Status</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getStatus()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Status();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#isPublic <em>Public</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Public</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#isPublic()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Public();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.WFNode#isMulticore <em>Multicore</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multicore</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#isMulticore()
	 * @see #getWFNode()
	 * @generated
	 */
	EAttribute getWFNode_Multicore();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.WFNode#getConductors <em>Conductors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Conductors</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFNode#getConductors()
	 * @see #getWFNode()
	 * @generated
	 */
	EReference getWFNode_Conductors();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.WFArc <em>WF Arc</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>WF Arc</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFArc
	 * @generated
	 */
	EClass getWFArc();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.WFArc#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFArc#getSource()
	 * @see #getWFArc()
	 * @generated
	 */
	EReference getWFArc_Source();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.WFArc#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see gov.sandia.dart.workflow.domain.WFArc#getTarget()
	 * @see #getWFArc()
	 * @generated
	 */
	EReference getWFArc_Target();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Property <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see gov.sandia.dart.workflow.domain.Property
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Property#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see gov.sandia.dart.workflow.domain.Property#getType()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Type();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Property#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see gov.sandia.dart.workflow.domain.Property#getValue()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Value();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.Property#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Node</em>'.
	 * @see gov.sandia.dart.workflow.domain.Property#getNode()
	 * @see #getProperty()
	 * @generated
	 */
	EReference getProperty_Node();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Property#isAdvanced <em>Advanced</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Advanced</em>'.
	 * @see gov.sandia.dart.workflow.domain.Property#isAdvanced()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Advanced();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Port <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port</em>'.
	 * @see gov.sandia.dart.workflow.domain.Port
	 * @generated
	 */
	EClass getPort();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Port#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see gov.sandia.dart.workflow.domain.Port#getType()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_Type();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.InputPort <em>Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Port</em>'.
	 * @see gov.sandia.dart.workflow.domain.InputPort
	 * @generated
	 */
	EClass getInputPort();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.InputPort#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Node</em>'.
	 * @see gov.sandia.dart.workflow.domain.InputPort#getNode()
	 * @see #getInputPort()
	 * @generated
	 */
	EReference getInputPort_Node();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.InputPort#getArcs <em>Arcs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Arcs</em>'.
	 * @see gov.sandia.dart.workflow.domain.InputPort#getArcs()
	 * @see #getInputPort()
	 * @generated
	 */
	EReference getInputPort_Arcs();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.InputPort#isTriggerOnly <em>Trigger Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Trigger Only</em>'.
	 * @see gov.sandia.dart.workflow.domain.InputPort#isTriggerOnly()
	 * @see #getInputPort()
	 * @generated
	 */
	EAttribute getInputPort_TriggerOnly();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.OutputPort <em>Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Port</em>'.
	 * @see gov.sandia.dart.workflow.domain.OutputPort
	 * @generated
	 */
	EClass getOutputPort();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.OutputPort#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Node</em>'.
	 * @see gov.sandia.dart.workflow.domain.OutputPort#getNode()
	 * @see #getOutputPort()
	 * @generated
	 */
	EReference getOutputPort_Node();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.OutputPort#getArcs <em>Arcs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Arcs</em>'.
	 * @see gov.sandia.dart.workflow.domain.OutputPort#getArcs()
	 * @see #getOutputPort()
	 * @generated
	 */
	EReference getOutputPort_Arcs();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.OutputPort#getResponseArcs <em>Response Arcs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Response Arcs</em>'.
	 * @see gov.sandia.dart.workflow.domain.OutputPort#getResponseArcs()
	 * @see #getOutputPort()
	 * @generated
	 */
	EReference getOutputPort_ResponseArcs();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Object</em>'.
	 * @see gov.sandia.dart.workflow.domain.NamedObject
	 * @generated
	 */
	EClass getNamedObject();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.NamedObject#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.sandia.dart.workflow.domain.NamedObject#getId()
	 * @see #getNamedObject()
	 * @generated
	 */
	EAttribute getNamedObject_Id();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.NamedObject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.sandia.dart.workflow.domain.NamedObject#getName()
	 * @see #getNamedObject()
	 * @generated
	 */
	EAttribute getNamedObject_Name();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.NamedObject#getDesc <em>Desc</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Desc</em>'.
	 * @see gov.sandia.dart.workflow.domain.NamedObject#getDesc()
	 * @see #getNamedObject()
	 * @generated
	 */
	EAttribute getNamedObject_Desc();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.SAWNode <em>SAW Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>SAW Node</em>'.
	 * @see gov.sandia.dart.workflow.domain.SAWNode
	 * @generated
	 */
	EClass getSAWNode();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.SAWNode#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model</em>'.
	 * @see gov.sandia.dart.workflow.domain.SAWNode#getModel()
	 * @see #getSAWNode()
	 * @generated
	 */
	EAttribute getSAWNode_Model();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.SAWNode#getComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Component</em>'.
	 * @see gov.sandia.dart.workflow.domain.SAWNode#getComponent()
	 * @see #getSAWNode()
	 * @generated
	 */
	EAttribute getSAWNode_Component();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Note <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Note</em>'.
	 * @see gov.sandia.dart.workflow.domain.Note
	 * @generated
	 */
	EClass getNote();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Note#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see gov.sandia.dart.workflow.domain.Note#getText()
	 * @see #getNote()
	 * @generated
	 */
	EAttribute getNote_Text();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Note#getColor <em>Color</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Color</em>'.
	 * @see gov.sandia.dart.workflow.domain.Note#getColor()
	 * @see #getNote()
	 * @generated
	 */
	EAttribute getNote_Color();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Note#isDrawBorderAndBackground <em>Draw Border And Background</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Draw Border And Background</em>'.
	 * @see gov.sandia.dart.workflow.domain.Note#isDrawBorderAndBackground()
	 * @see #getNote()
	 * @generated
	 */
	EAttribute getNote_DrawBorderAndBackground();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.NamedObjectWithProperties <em>Named Object With Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Object With Properties</em>'.
	 * @see gov.sandia.dart.workflow.domain.NamedObjectWithProperties
	 * @generated
	 */
	EClass getNamedObjectWithProperties();

	/**
	 * Returns the meta object for the reference list '{@link gov.sandia.dart.workflow.domain.NamedObjectWithProperties#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Properties</em>'.
	 * @see gov.sandia.dart.workflow.domain.NamedObjectWithProperties#getProperties()
	 * @see #getNamedObjectWithProperties()
	 * @generated
	 */
	EReference getNamedObjectWithProperties_Properties();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.ResponseArc <em>Response Arc</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Response Arc</em>'.
	 * @see gov.sandia.dart.workflow.domain.ResponseArc
	 * @generated
	 */
	EClass getResponseArc();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.ResponseArc#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see gov.sandia.dart.workflow.domain.ResponseArc#getSource()
	 * @see #getResponseArc()
	 * @generated
	 */
	EReference getResponseArc_Source();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.ResponseArc#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see gov.sandia.dart.workflow.domain.ResponseArc#getTarget()
	 * @see #getResponseArc()
	 * @generated
	 */
	EReference getResponseArc_Target();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Conductor <em>Conductor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Conductor</em>'.
	 * @see gov.sandia.dart.workflow.domain.Conductor
	 * @generated
	 */
	EClass getConductor();

	/**
	 * Returns the meta object for the reference '{@link gov.sandia.dart.workflow.domain.Conductor#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Node</em>'.
	 * @see gov.sandia.dart.workflow.domain.Conductor#getNode()
	 * @see #getConductor()
	 * @generated
	 */
	EReference getConductor_Node();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see gov.sandia.dart.workflow.domain.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Parameter#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see gov.sandia.dart.workflow.domain.Parameter#getType()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Type();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Parameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see gov.sandia.dart.workflow.domain.Parameter#getValue()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Value();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Runner <em>Runner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Runner</em>'.
	 * @see gov.sandia.dart.workflow.domain.Runner
	 * @generated
	 */
	EClass getRunner();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Runner#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see gov.sandia.dart.workflow.domain.Runner#getType()
	 * @see #getRunner()
	 * @generated
	 */
	EAttribute getRunner_Type();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Arc <em>Arc</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Arc</em>'.
	 * @see gov.sandia.dart.workflow.domain.Arc
	 * @generated
	 */
	EClass getArc();

	/**
	 * Returns the meta object for class '{@link gov.sandia.dart.workflow.domain.Image <em>Image</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Image</em>'.
	 * @see gov.sandia.dart.workflow.domain.Image
	 * @generated
	 */
	EClass getImage();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Image#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see gov.sandia.dart.workflow.domain.Image#getText()
	 * @see #getImage()
	 * @generated
	 */
	EAttribute getImage_Text();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Image#isZoomToFit <em>Zoom To Fit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Zoom To Fit</em>'.
	 * @see gov.sandia.dart.workflow.domain.Image#isZoomToFit()
	 * @see #getImage()
	 * @generated
	 */
	EAttribute getImage_ZoomToFit();

	/**
	 * Returns the meta object for the attribute '{@link gov.sandia.dart.workflow.domain.Image#isDrawBorder <em>Draw Border</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Draw Border</em>'.
	 * @see gov.sandia.dart.workflow.domain.Image#isDrawBorder()
	 * @see #getImage()
	 * @generated
	 */
	EAttribute getImage_DrawBorder();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DomainFactory getDomainFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.ResponseImpl <em>Response</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.ResponseImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getResponse()
		 * @generated
		 */
		EClass RESPONSE = eINSTANCE.getResponse();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESPONSE__TYPE = eINSTANCE.getResponse_Type();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESPONSE__SOURCE = eINSTANCE.getResponse_Source();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.WFNodeImpl <em>WF Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.WFNodeImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getWFNode()
		 * @generated
		 */
		EClass WF_NODE = eINSTANCE.getWFNode();

		/**
		 * The meta object literal for the '<em><b>Input Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WF_NODE__INPUT_PORTS = eINSTANCE.getWFNode_InputPorts();

		/**
		 * The meta object literal for the '<em><b>Output Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WF_NODE__OUTPUT_PORTS = eINSTANCE.getWFNode_OutputPorts();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__START = eINSTANCE.getWFNode_Start();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__TYPE = eINSTANCE.getWFNode_Type();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__LABEL = eINSTANCE.getWFNode_Label();

		/**
		 * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__URL = eINSTANCE.getWFNode_Url();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__VERSION = eINSTANCE.getWFNode_Version();

		/**
		 * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__STATUS = eINSTANCE.getWFNode_Status();

		/**
		 * The meta object literal for the '<em><b>Public</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__PUBLIC = eINSTANCE.getWFNode_Public();

		/**
		 * The meta object literal for the '<em><b>Multicore</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WF_NODE__MULTICORE = eINSTANCE.getWFNode_Multicore();

		/**
		 * The meta object literal for the '<em><b>Conductors</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WF_NODE__CONDUCTORS = eINSTANCE.getWFNode_Conductors();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.WFArcImpl <em>WF Arc</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.WFArcImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getWFArc()
		 * @generated
		 */
		EClass WF_ARC = eINSTANCE.getWFArc();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WF_ARC__SOURCE = eINSTANCE.getWFArc_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WF_ARC__TARGET = eINSTANCE.getWFArc_Target();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.PropertyImpl <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.PropertyImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getProperty()
		 * @generated
		 */
		EClass PROPERTY = eINSTANCE.getProperty();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__TYPE = eINSTANCE.getProperty_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY__NODE = eINSTANCE.getProperty_Node();

		/**
		 * The meta object literal for the '<em><b>Advanced</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__ADVANCED = eINSTANCE.getProperty_Advanced();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.PortImpl <em>Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.PortImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getPort()
		 * @generated
		 */
		EClass PORT = eINSTANCE.getPort();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__TYPE = eINSTANCE.getPort_Type();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.InputPortImpl <em>Input Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.InputPortImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getInputPort()
		 * @generated
		 */
		EClass INPUT_PORT = eINSTANCE.getInputPort();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_PORT__NODE = eINSTANCE.getInputPort_Node();

		/**
		 * The meta object literal for the '<em><b>Arcs</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_PORT__ARCS = eINSTANCE.getInputPort_Arcs();

		/**
		 * The meta object literal for the '<em><b>Trigger Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INPUT_PORT__TRIGGER_ONLY = eINSTANCE.getInputPort_TriggerOnly();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.OutputPortImpl <em>Output Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.OutputPortImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getOutputPort()
		 * @generated
		 */
		EClass OUTPUT_PORT = eINSTANCE.getOutputPort();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_PORT__NODE = eINSTANCE.getOutputPort_Node();

		/**
		 * The meta object literal for the '<em><b>Arcs</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_PORT__ARCS = eINSTANCE.getOutputPort_Arcs();

		/**
		 * The meta object literal for the '<em><b>Response Arcs</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_PORT__RESPONSE_ARCS = eINSTANCE.getOutputPort_ResponseArcs();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.NamedObjectImpl <em>Named Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.NamedObjectImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getNamedObject()
		 * @generated
		 */
		EClass NAMED_OBJECT = eINSTANCE.getNamedObject();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_OBJECT__ID = eINSTANCE.getNamedObject_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_OBJECT__NAME = eINSTANCE.getNamedObject_Name();

		/**
		 * The meta object literal for the '<em><b>Desc</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_OBJECT__DESC = eINSTANCE.getNamedObject_Desc();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.SAWNodeImpl <em>SAW Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.SAWNodeImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getSAWNode()
		 * @generated
		 */
		EClass SAW_NODE = eINSTANCE.getSAWNode();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SAW_NODE__MODEL = eINSTANCE.getSAWNode_Model();

		/**
		 * The meta object literal for the '<em><b>Component</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SAW_NODE__COMPONENT = eINSTANCE.getSAWNode_Component();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.NoteImpl <em>Note</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.NoteImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getNote()
		 * @generated
		 */
		EClass NOTE = eINSTANCE.getNote();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTE__TEXT = eINSTANCE.getNote_Text();

		/**
		 * The meta object literal for the '<em><b>Color</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTE__COLOR = eINSTANCE.getNote_Color();

		/**
		 * The meta object literal for the '<em><b>Draw Border And Background</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTE__DRAW_BORDER_AND_BACKGROUND = eINSTANCE.getNote_DrawBorderAndBackground();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.NamedObjectWithPropertiesImpl <em>Named Object With Properties</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.NamedObjectWithPropertiesImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getNamedObjectWithProperties()
		 * @generated
		 */
		EClass NAMED_OBJECT_WITH_PROPERTIES = eINSTANCE.getNamedObjectWithProperties();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NAMED_OBJECT_WITH_PROPERTIES__PROPERTIES = eINSTANCE.getNamedObjectWithProperties_Properties();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.ResponseArcImpl <em>Response Arc</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.ResponseArcImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getResponseArc()
		 * @generated
		 */
		EClass RESPONSE_ARC = eINSTANCE.getResponseArc();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESPONSE_ARC__SOURCE = eINSTANCE.getResponseArc_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESPONSE_ARC__TARGET = eINSTANCE.getResponseArc_Target();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.ConductorImpl <em>Conductor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.ConductorImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getConductor()
		 * @generated
		 */
		EClass CONDUCTOR = eINSTANCE.getConductor();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDUCTOR__NODE = eINSTANCE.getConductor_Node();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.ParameterImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__TYPE = eINSTANCE.getParameter_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__VALUE = eINSTANCE.getParameter_Value();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.RunnerImpl <em>Runner</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.RunnerImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getRunner()
		 * @generated
		 */
		EClass RUNNER = eINSTANCE.getRunner();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RUNNER__TYPE = eINSTANCE.getRunner_Type();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.ArcImpl <em>Arc</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.ArcImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getArc()
		 * @generated
		 */
		EClass ARC = eINSTANCE.getArc();

		/**
		 * The meta object literal for the '{@link gov.sandia.dart.workflow.domain.impl.ImageImpl <em>Image</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.sandia.dart.workflow.domain.impl.ImageImpl
		 * @see gov.sandia.dart.workflow.domain.impl.DomainPackageImpl#getImage()
		 * @generated
		 */
		EClass IMAGE = eINSTANCE.getImage();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMAGE__TEXT = eINSTANCE.getImage_Text();

		/**
		 * The meta object literal for the '<em><b>Zoom To Fit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMAGE__ZOOM_TO_FIT = eINSTANCE.getImage_ZoomToFit();

		/**
		 * The meta object literal for the '<em><b>Draw Border</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMAGE__DRAW_BORDER = eINSTANCE.getImage_DrawBorder();

	}

} //DomainPackage
