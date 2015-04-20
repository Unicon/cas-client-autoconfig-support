package net.unicon.cas.client.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables CAS Java client Servlet Filters configuration facility.
 * To be used together with {@link org.springframework.context.annotation.Configuration Configuration}
 * or {@link org.springframework.boot.autoconfigure.SpringBootApplication SpringBootApplication} classes.
 *
 * <p>For those wishing to customize CAS filters during their creation, application configuration classes carrying this annotation
 * may implement the {@link net.unicon.cas.client.configuration.CasClientConfigurer} callback interface or simply
 * extend the {@link net.unicon.cas.client.configuration.CasClientConfigurerAdapter} and override only necessary methods.
 *
 * @author Dmitriy Kopylenko
 * @see net.unicon.cas.client.configuration.CasClientConfigurer
 * @see net.unicon.cas.client.configuration.CasClientConfiguration
 * @see net.unicon.cas.client.configuration.CasClientConfigurerAdapter
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CasClientConfiguration.class)
public @interface EnableCasClient {

    ValidationType validationType() default ValidationType.CAS;

    enum ValidationType {
        CAS,
        SAML
    }
}
