/**
 *
 * $Id$
 */
package gov.sandia.dart.workflow.domain.validation;

import gov.sandia.dart.workflow.domain.ResponseArc;

import org.eclipse.emf.common.util.EList;

/**
 * A sample validator interface for {@link gov.sandia.dart.workflow.domain.Response}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface ResponseValidator {
	boolean validate();

	boolean validateType(String value);
	boolean validateSource(EList<ResponseArc> value);
}
