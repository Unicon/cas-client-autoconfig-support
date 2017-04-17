package net.unicon.cas.client.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * An implementation of {@link net.unicon.cas.client.configuration.CasClientConfigurer} with empty methods allowing
 * sub-classes to override only the methods they're interested in.
 *
 * @author Dmitriy Kopylenko
 * @since 1.0.0
 * @see net.unicon.cas.client.configuration.CasClientConfigurer
 */
public class CasClientConfigurerAdapter implements CasClientConfigurer {

    @Override
    public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
        //Noop. Designed to be overridden if necessary to ease plugging in custom configs.
    }

    @Override
    public void configureValidationFilter(FilterRegistrationBean validationFilter) {
        //Noop. Designed to be overridden if necessary to ease plugging in custom configs.
    }

    @Override
    public void configureHttpServletRequestWrapperFilter(FilterRegistrationBean httpServletRequestWrapperFilter) {
        //Noop. Designed to be overridden if necessary to ease plugging in custom configs.
    }

    @Override
    public void configureAssertionThreadLocalFilter(FilterRegistrationBean assertionThreadLocalFilter) {
        //Noop. Designed to be overridden if necessary to ease plugging in custom configs.
    }
}
