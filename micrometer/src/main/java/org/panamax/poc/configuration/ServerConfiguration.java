package org.panamax.poc.configuration;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ServerConfiguration {

    @JacksonXmlProperty(localName = "ip")
    private String ip;
    @JacksonXmlProperty(localName = "port")
    private int port;

    public String ip() {
        return ip;
    }

    public int port() {
        return port;
    }

    @Override
    public String toString() {
        return "ServerConfiguration{" + "ip='" + ip + '\'' + ", port='" + port + '\'' + '}';
    }
}
