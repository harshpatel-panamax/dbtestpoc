package org.panamax.poc.configuration;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TagConfiguration {

    @JacksonXmlProperty(localName = "key")
    private String key;
    @JacksonXmlProperty(localName = "value")
    private String value;

    @Override
    public String toString() {
        return "TagConfiguration{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }
}
