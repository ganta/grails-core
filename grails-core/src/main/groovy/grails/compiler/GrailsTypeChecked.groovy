package grails.compiler

import groovy.transform.AnnotationCollector
import groovy.transform.TypeChecked

/**
 * 
 * @since 2.4
 *
 */
@AnnotationCollector
@TypeChecked(extensions=['grails.compiler.ValidateableTypeCheckingExtension', 
                         'grails.compiler.CriteriaTypeCheckingExtension',
                         'grails.compiler.DynamicFinderTypeCheckingExtension',
                         'grails.compiler.DomainMappingTypeCheckingExtension'])
@interface GrailsTypeChecked {

}
