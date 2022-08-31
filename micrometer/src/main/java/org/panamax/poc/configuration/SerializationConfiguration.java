package org.panamax.poc.configuration;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SerializationConfiguration {

    @JacksonXmlProperty(localName = "max-buffer-size")
    private long maxBufferSize;

    public long bufferSize() {
        return maxBufferSize;
    }

    @Override
    public String toString() {
        return "SerializationConfiguration{" + "maxBufferSize=" + maxBufferSize + '}';
    }
}
