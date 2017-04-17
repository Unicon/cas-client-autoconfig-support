cas-client-autoconfig-support
=============================

Library providing annotation-based configuration support for CAS Java clients. Primarily designed for super easy CASification of Spring Boot apps.

> This project was developed as part of Unicon's [Open Source Support program](https://unicon.net/opensource).
Professional Support / Integration Assistance for this module is available. For more information [visit](https://unicon.net/opensource/cas).

## Current version
`1.4.0-GA`

## Usage

* Define a dependency:

> Maven:

  ```xml
  <dependency>
      <groupId>net.unicon.cas</groupId>
      <artifactId>cas-client-autoconfig-support</artifactId>
      <version>1.4.0-GA</version>      
  </dependency>
  ```

> Gradle:

  ```Groovy
  dependencies {
        ...
        compile 'net.unicon.cas:cas-client-autoconfig-support:1.3.0-GA'
        ...
  }
  ```

* Add the following required properties

> in Spring Boot's `application.properties` or `application.yml` Example:

```bash
   cas.server-url-prefix=https://cashost.com/cas
   cas.server-login-url=https://cashost.com/cas/login
   cas.client-host-url=https://casclient.com
```

* Annotate Spring Boot application (or any @Configuration class) with `@EnableCasClient` annotation

```java
    @SpringBootApplication
    @Controller
    @EnableCasClient
    public class MyApplication { .. }
```

> For CAS3 protocol (authentication and validation filters) - which is default if nothing is specified

```bash
   cas.validation-type=CAS3
```

> For CAS2 protocol (authentication and validation filters)

```bash
   cas.validation-type=CAS
```

> For SAML protocol (authentication and validation filters)

```bash
   cas.validation-type=SAML
```

## Available optional properties

* `cas.authentication-url-patterns`
* `cas.validation-url-patterns`
* `cas.request-wrapper-url-patterns`
* `cas.assertion-thread-local-url-patterns`
* `cas.gateway`
* `cas.use-session`
* `cas.redirect-after-validation`
* `cas.allowed-proxy-chains`
* `cas.proxy-callback-url`
* `cas.proxy-receptor-url`
* `cas.accept-any-proxy`
* `server.context-parameters.renew`

## Advanced configuration

This library does not expose ALL the CAS client configuration options via standard Spring property sources, but only most commonly used ones.
If there is a need however, to set any number of not exposed, 'exotic' properties, there is a way: just extend `CasClientConfigurerAdapter`
class in your `@EnableCasClient` annotated class and override appropriate configuration method(s) for CAS client filter(s) in question.
For example:

```java
    @SpringBootApplication
    @EnableCasClient
    class CasProtectedApplication extends CasClientConfigurerAdapter {    
        @Override
        void configureValidationFilter(FilterRegistrationBean validationFilter) {           
            validationFilter.getInitParameters().put("millisBetweenCleanUps", "120000");
        }        
        @Override
        void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
            authenticationFilter.getInitParameters().put("artifactParameterName", "casTicket");
            authenticationFilter.getInitParameters().put("serviceParameterName", "targetService");
        }                                
    }
```        
