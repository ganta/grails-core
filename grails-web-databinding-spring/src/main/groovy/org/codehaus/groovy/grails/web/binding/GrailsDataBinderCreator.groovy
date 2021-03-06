/*
 * Copyright 2014 the original author or authors.
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
package org.codehaus.groovy.grails.web.binding

import groovy.transform.CompileStatic
import org.codehaus.groovy.grails.web.binding.spring.SpringWebDataBinder
import org.codehaus.groovy.grails.web.binding.spring.SpringWebDataBinderCreator

import javax.servlet.http.HttpServletRequest

/**
 * Factory for creating GrailsDataBinderCreator instances
 */
@CompileStatic
class GrailsDataBinderCreator implements SpringWebDataBinderCreator {
    @Override
    SpringWebDataBinder createBinder(Object targetObject, String name) {
        GrailsDataBinder.createBinder(targetObject, name)
    }

    @Override
    SpringWebDataBinder createBinder(Object targetObject, String name, HttpServletRequest request) {
        GrailsDataBinder.createBinder(targetObject, name, request)
    }
}
