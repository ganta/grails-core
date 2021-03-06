package org.grails.boot.support

import grails.util.Holders
import groovy.transform.CompileStatic
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.spring.DefaultRuntimeSpringConfiguration
import org.codehaus.groovy.grails.lifecycle.ShutdownOperations
import org.codehaus.groovy.grails.plugins.DefaultGrailsPluginManager
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.web.context.ServletEnvironmentGrailsApplicationDiscoveryStrategy
import org.springframework.beans.BeansException
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ApplicationContextEvent
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.web.context.WebApplicationContext

/**
 * A {@link BeanDefinitionRegistryPostProcessor} that enhances any ApplicationContext with plugin manager capabilities
 *
 * @author Graeme Rocher
 * @since 3.0
 */
@CompileStatic
class GrailsApplicationPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, ApplicationListener<ApplicationContextEvent> {

    final GrailsApplication grailsApplication
    final GrailsPluginManager pluginManager

    GrailsApplicationPostProcessor(Class...classes) {
        grailsApplication = new DefaultGrailsApplication(classes as Class[])
        pluginManager = new DefaultGrailsPluginManager(grailsApplication)

        performGrailsInitializationSequence()
    }

    protected void performGrailsInitializationSequence() {
        pluginManager.loadPlugins()
        pluginManager.doArtefactConfiguration()
        grailsApplication.initialise()
        pluginManager.registerProvidedArtefacts(grailsApplication)
    }

    @Override
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        def springConfig = new DefaultRuntimeSpringConfiguration()
        springConfig.setBeanFactory((ListableBeanFactory) registry)
        pluginManager.doRuntimeConfiguration(springConfig)
        springConfig.registerBeansWithRegistry(registry)

    }

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerSingleton(GrailsApplication.APPLICATION_ID, grailsApplication)
        beanFactory.registerSingleton(GrailsPluginManager.BEAN_NAME, pluginManager)
    }

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(applicationContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext)applicationContext).addApplicationListener(this)
        }
    }

    @Override
    void onApplicationEvent(ApplicationContextEvent event) {
        def context = event.applicationContext
        if(event instanceof ContextRefreshedEvent) {
            pluginManager.setApplicationContext(context)
            pluginManager.doDynamicMethods()
            pluginManager.doPostProcessing(context)
            Holders.pluginManager = pluginManager
            if(context instanceof WebApplicationContext) {
                def servletContext = ((WebApplicationContext) context).servletContext
                Holders.setServletContext(servletContext);
                Holders.addApplicationDiscoveryStrategy(new ServletEnvironmentGrailsApplicationDiscoveryStrategy(servletContext));
            }

        }
        else if(event instanceof ContextClosedEvent) {
            pluginManager.shutdown()
            ShutdownOperations.runOperations()
            Holders.clear()
        }
    }

}