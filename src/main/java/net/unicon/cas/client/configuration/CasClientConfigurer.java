package net.unicon.cas.client.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * Callback interface to be implemented by {@link org.springframework.context.annotation.Configuration Configuration} classes annotated with
 * {@link net.unicon.cas.client.configuration.EnableCasClient} that wish or need to
 * explicitly configure or customize CAS client filters created by {@link net.unicon.cas.client.configuration.CasClientConfiguration
 * CasClientConfiguration}.
 *
 * Consider extending {@link net.unicon.cas.client.configuration.CasClientConfigurerAdapter},
 * which provides a noop stub implementation of all interface methods.
 *
 * @author Dmitriy Kopylenko
 * @see net.unicon.cas.client.configuration.CasClientConfigurerAdapter
 * @since 1.0.0
 */
public interface CasClientConfigurer {

    /**
     * Configure or customize CAS authentication filter.
     *
     * @param authenticationFilter
     */
    void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter);

    /**
     * Configure or customize CAS validation filter.
     *
     * @param validationFilter
     */
    void configureValidationFilter(FilterRegistrationBean validationFilter);

    /**
     * Configure or customize CAS http servlet wrapper filter.
     *
     * @param httpServletRequestWrapperFilter
     */
    void configureHttpServletRequestWrapperFilter(FilterRegistrationBean httpServletRequestWrapperFilter);

    /**
     * Configure or customize CAS assertion thread local filter.
     *
     * @param assertionThreadLocalFilter
     */
    void configureAssertionThreadLocalFilter(FilterRegistrationBean assertionThreadLocalFilter);
}
