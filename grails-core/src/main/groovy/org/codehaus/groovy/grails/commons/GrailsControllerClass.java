/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.grails.commons;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a controller class in Grails.
 *
 * @author Steven Devijver
 */
public interface GrailsControllerClass extends InjectableGrailsClass {

    /**
     * The name of the index action.
     */
    String INDEX_ACTION = "index";

    /**
     * The name of the before interceptor property.
     */
    String BEFORE_INTERCEPTOR = "beforeInterceptor";

    /**
     * The name of the after interceptor property.
     */
    String AFTER_INTERCEPTOR = "afterInterceptor";

    /**
     * The general name to use when referring to controller artefacts.
     */
    String CONTROLLER = "controller";

    /**
     * The general name to use when referring to action artefacts.
     */
    String ACTION = "action";

    /**
     * The general name to use when referring to action view.
     */
    String VIEW = "view";

    /**
     * The name of the namespace property
     */
    String NAMESPACE_PROPERTY = "namespace";


    Set<String> getActions();

    /**
     * @return the namespace of this controller, null if none was specified
     */
    String getNamespace();


    /**
     * Returns the default action for this Controller.
     *
     * @return The default action
     */
    String getDefaultAction();

    /**
     * Initialize the controller class
     */
    void initialize();

    Object invoke(Object controller, String action) throws Throwable;
}
