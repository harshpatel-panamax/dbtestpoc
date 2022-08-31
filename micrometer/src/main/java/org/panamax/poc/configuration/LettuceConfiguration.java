package org.panamax.poc.configuration;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "lettuce")
public class LettuceConfiguration {

    @JacksonXmlProperty(localName = "matrix")
    private MatrixConfiguration configuration;

    @Override
    public String toString() {
        return "LettuceConfiguration{" + "matrixConfiguration=" + configuration + '}';
    }

    public MatrixConfiguration matrixConfiguration() {
        return configuration;
    }
}
