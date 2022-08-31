package org.panamax.poc.configuration;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.Duration;
import java.util.List;

public class ClusterConfiguration {

    @JacksonXmlProperty(localName = "connection-timeout-sec")
    private int connectionTimeout;

    @JacksonXmlElementWrapper(localName = "servers")
    @JacksonXmlProperty(localName = "server")
    private List<ServerConfiguration> serverConfigurations;

    public Duration connectionTimeout() {
        return Duration.ofSeconds(connectionTimeout);
    }

    public List<ServerConfiguration> serverConfigurations() {
        return serverConfigurations;
    }

    @Override
    public String toString() {
        return "ClusterConfiguration{" + "connectionTimeout=" + connectionTimeout + ", serverConfigurations=" + serverConfigurations + '}';
    }
}
