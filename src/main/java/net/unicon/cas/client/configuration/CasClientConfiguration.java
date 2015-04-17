package net.unicon.cas.client.configuration;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.authentication.Saml11AuthenticationFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Saml11TicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;

import static net.unicon.cas.client.configuration.EnableCasClient.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class providing default CAS client infrastructure filters.
 * This configuration facility is typically imported into Spring's Application Context via
 * {@link net.unicon.cas.client.configuration.EnableCasClient EnableCasClient} meta annotation.
 *
 * @author Dmitriy Kopylenko
 * @see net.unicon.cas.client.configuration.EnableCasClient
 * @since 1.0.0
 */
@Configuration
public class CasClientConfiguration implements ImportAware {

    @Value("${cas.server.url-prefix}")
    private String casServerUrlPrefix;

    @Value("${cas.server.login-url:/login}")
    private String casServerLoginUrl;

    @Value("${cas.client.service-url}")
    private String casClientServiceUrl;

    private CasClientConfigurer casClientConfigurer;

    private ValidationType validationType;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableCasClient.class.getName()));
        if (annotationAttributes == null) {
            throw new IllegalArgumentException("@EnableCasClient is not present on importing class " + importMetadata.getClassName());
        }
        this.validationType = annotationAttributes.getEnum("validationType");
    }

    @Bean
    public FilterRegistrationBean casAuthenticationFilter() {
        final FilterRegistrationBean authnFilter = new FilterRegistrationBean();
        final Filter targetCasAuthnFilter = this.validationType == ValidationType.CAS ? new AuthenticationFilter()
                : new Saml11AuthenticationFilter();

        authnFilter.setFilter(targetCasAuthnFilter);
        authnFilter.setUrlPatterns(Arrays.asList("/*"));
        authnFilter.setOrder(1);
        authnFilter.setInitParameters(constructInitParams("casServerLoginUrl->" + this.casServerUrlPrefix + this.casServerLoginUrl,
                this.casClientServiceUrl));

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureAuthenticationFilter(authnFilter);
        }
        return authnFilter;
    }

    @Bean
    public FilterRegistrationBean casValidationFilter() {
        final FilterRegistrationBean validationFilter = new FilterRegistrationBean();
        final Filter targetCasValidationFilter = this.validationType == ValidationType.CAS ? new Cas20ProxyReceivingTicketValidationFilter()
                : new Saml11TicketValidationFilter();

        validationFilter.setFilter(targetCasValidationFilter);
        validationFilter.setUrlPatterns(Arrays.asList("/*"));
        validationFilter.setOrder(2);
        validationFilter.setInitParameters(constructInitParams("casServerUrlPrefix->" + this.casServerUrlPrefix, this.casClientServiceUrl));

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureValidationFilter(validationFilter);
        }
        return validationFilter;
    }

    @Bean
    public FilterRegistrationBean casHttpServletRequestWrapperFilter() {
        final FilterRegistrationBean reqWrapperFilter = new FilterRegistrationBean();
        reqWrapperFilter.setFilter(new HttpServletRequestWrapperFilter());
        reqWrapperFilter.setUrlPatterns(Arrays.asList("/*"));
        reqWrapperFilter.setOrder(3);

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureHttpServletRequestWrapperFilter(reqWrapperFilter);
        }
        return reqWrapperFilter;
    }

    @Bean
    public FilterRegistrationBean casAssertionThreadLocalFilter() {
        final FilterRegistrationBean assertionTLFilter = new FilterRegistrationBean();
        assertionTLFilter.setFilter(new AssertionThreadLocalFilter());
        assertionTLFilter.setUrlPatterns(Arrays.asList("/*"));
        assertionTLFilter.setOrder(4);

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureAssertionThreadLocalFilter(assertionTLFilter);
        }
        return assertionTLFilter;
    }


    @Autowired(required = false)
    void setConfigurers(Collection<CasClientConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException(configurers.size() + " implementations of " +
                    "CasClientConfigurer were found when only 1 was expected. " +
                    "Refactor the configuration such that CasClientConfigurer is " +
                    "implemented only once or not at all.");
        }
        this.casClientConfigurer = configurers.iterator().next();
    }

    private Map<String, String> constructInitParams(String urlKeyAndVal, String serviceVal) {
        final String[] urlData = urlKeyAndVal.split("->");
        final Map<String, String> initParams = new HashMap<>(2);
        initParams.put(urlData[0], urlData[1]);
        initParams.put("service", serviceVal);
        return initParams;
    }
}
