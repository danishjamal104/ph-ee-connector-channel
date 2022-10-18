package org.mifos.connector.channel.camel.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.component.undertow.UndertowComponent;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class CamelContextConfig {

    @Value("${camel.server-port}")
    private int serverPort;

    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                camelContext.setTracing(false);
                camelContext.setMessageHistory(false);
                camelContext.setStreamCaching(true);
                camelContext.disableJMX();

                RestConfiguration rest = new RestConfiguration();
                camelContext.setRestConfiguration(rest);
                rest.setComponent("undertow");
                rest.setProducerComponent("undertow");
                rest.setPort(serverPort);
                rest.setBindingMode(RestConfiguration.RestBindingMode.json);
                rest.setDataFormatProperties(new HashMap<>());
                rest.getDataFormatProperties().put("prettyPrint", "true");
                rest.setScheme("http");

                // add swagger api-doc out of the box
                rest.setContextPath("/api-doc");
                rest.setApiProperties(new HashMap<>());
                rest.getApiProperties().put("api.title", "API Title");
                rest.getApiProperties().put("api.version", "v1");
                // and enable CORS
                rest.getApiProperties().put("cors", "true");
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // empty
            }
        };
    }
}
