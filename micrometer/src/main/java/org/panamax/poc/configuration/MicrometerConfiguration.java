package org.panamax.poc.configuration;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import io.micrometer.jmx.JmxMeterRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MicrometerConfiguration {

    @JacksonXmlElementWrapper(localName = "registries")
    @JacksonXmlProperty(localName = "registry")
    private List<String> registries;

    public List<MeterRegistry> registries() {
        return registries.stream().map(Registry::fromName).collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return "MicrometerConfiguration{" + "registries=" + registries + '}';
    }

    public enum Registry {
        Log("log") {
            @Override
            public MeterRegistry create() {
                return LoggingMeterRegistry.builder(LoggingRegistryConfig.DEFAULT)
                                           .loggingSink(System.out::println)
                                           .clock(Clock.SYSTEM)
                                           .build();
            }
        },JMX("jmx") {
            @Override
            public MeterRegistry create() {
                return new JmxMeterRegistry(key -> null, Clock.SYSTEM);
            }
        };

        private static Map<String, Registry> nameToRegistry;
        private final String name;

        Registry(String name) {
            this.name = name;
        }

        static {
            nameToRegistry = Arrays.stream(values()).collect(Collectors.toMap(Registry::getName, Function.identity()));
        }

        public String getName() {
            return name;
        }

        public abstract  MeterRegistry create();

        public static MeterRegistry fromName(String name) {
            return nameToRegistry.get(name.toLowerCase()).create();
        }
    }
}
