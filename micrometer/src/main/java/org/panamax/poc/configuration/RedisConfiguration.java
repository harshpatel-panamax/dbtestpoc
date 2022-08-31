package org.panamax.poc.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import reactor.util.Metrics;

import java.io.File;
import java.io.IOException;

@JacksonXmlRootElement(localName = "redis")
public class RedisConfiguration {

    @JacksonXmlProperty(localName = "lettuce")
    private LettuceConfiguration lettuceConfiguration;

    @JacksonXmlProperty(localName = "cluster")
    private ClusterConfiguration clusterConfiguration;

    @JacksonXmlProperty(localName = "serialization")
    private SerializationConfiguration serializationConfiguration;

    @JacksonXmlProperty(localName = "mircometer")
    private MicrometerConfiguration micrometerConfiguration;

    public static void main(String[] args) {
        RedisConfiguration.read();
    }

    public static RedisConfiguration read() {
        File file = new File(RedisConfiguration.class.getClassLoader().getResource("redis.xml").getFile());

        JacksonXmlModule xmlModule = new JacksonXmlModule();
        XmlMapper xmlMapper = new XmlMapper(xmlModule);
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        ObjectMapper om = xmlMapper;

        RedisConfiguration configuration = null;
        try {
            configuration = om.readValue(file, RedisConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Redis matrixConfiguration info " + configuration.toString());

        return configuration;
    }


    @Override
    public String toString() {
        return "RedisConfiguration{" + "lettuceConfiguration=" + lettuceConfiguration + ", clusterConfiguration=" + clusterConfiguration + ", serializationConfiguration=" + serializationConfiguration + ", micrometerConfiguration=" + micrometerConfiguration + '}';
    }

    public LettuceConfiguration lettuceConfiguration() {
        return lettuceConfiguration;
    }

    public ClusterConfiguration clusterConfiguration() {
        return clusterConfiguration;
    }

    public SerializationConfiguration serializationConfiguration() {
        return serializationConfiguration;
    }

    public MicrometerConfiguration micrometerConfiguration() {
        return micrometerConfiguration;
    }
}
