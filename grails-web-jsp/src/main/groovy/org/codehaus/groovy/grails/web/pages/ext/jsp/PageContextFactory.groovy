/*
 * Copyright 2004-2005 Graeme Rocher
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
package org.codehaus.groovy.grails.web.pages.ext.jsp

import javax.servlet.ServletContext
import javax.servlet.jsp.PageContext as PC

import org.codehaus.groovy.grails.web.pages.GroovyPageBinding
import org.codehaus.groovy.grails.web.pages.GroovyPagesServlet
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GAA
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

/**
 * Obtains a reference to the GroovyPagesPageContext class.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
class PageContextFactory {

    static GroovyPagesPageContext getCurrent() {
        GrailsWebRequest webRequest = RequestContextHolder.currentRequestAttributes()

        def request = webRequest.getCurrentRequest()

        def pageContext = request.getAttribute(PC.PAGECONTEXT)
        if (pageContext instanceof GroovyPagesPageContext) return pageContext

        ServletContext servletContext = webRequest.getServletContext()
        def gspServlet = servletContext.getAttribute(GroovyPagesServlet.SERVLET_INSTANCE)
        if (!gspServlet) {
            gspServlet = new GroovyPagesServlet()
            servletContext.setAttribute GroovyPagesServlet.SERVLET_INSTANCE, gspServlet
        }
        def pageScope = request.getAttribute(GAA.PAGE_SCOPE)
        if (!pageScope) {
            pageScope = new GroovyPageBinding()
            request.setAttribute(GAA.PAGE_SCOPE, pageScope)
        }

        pageContext = new GroovyPagesPageContext(gspServlet, pageScope)
        request.setAttribute(PC.PAGECONTEXT, pageContext)

        return pageContext
    }
}
