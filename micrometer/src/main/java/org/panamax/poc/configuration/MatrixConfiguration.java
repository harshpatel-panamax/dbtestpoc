package org.panamax.poc.configuration;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.lettuce.core.metrics.MicrometerOptions;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.panamax.poc.configuration.TagConfiguration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;

@JacksonXmlRootElement(localName = "matrix")
public class MatrixConfiguration {

    @JacksonXmlProperty(localName = "enabled")
    private boolean enabled;

    @JacksonXmlProperty(localName = "histogram")
    private boolean histogram;
    @JacksonXmlProperty(localName = "local-socket-distinction")
    private boolean localSocketDistinction;
    @JacksonXmlProperty(localName = "max-latency-ms")
    private long maxLatencyMs;

    @JacksonXmlProperty(localName = "min-latency-ms")
    private long minLatencyMs;


    @JacksonXmlElementWrapper(localName = "tags")
    @JacksonXmlProperty(localName = "tag")
    private List<TagConfiguration> tags = new ArrayList<>();

    @JacksonXmlElementWrapper(localName = "latency")
    @JacksonXmlProperty(localName = "percentile")
    private List<Double> latencyPercentile = new ArrayList<>();

    @Override
    public String toString() {
        return "MatrixConfiguration{" + "enabled=" + enabled + ", histogram=" + histogram + ", localSocketDistinction=" + localSocketDistinction + ", maxLatencyMs=" + maxLatencyMs + ", minLatencyMs=" + minLatencyMs + ", tags=" + tags + ", latencyPercentile=" + latencyPercentile + '}';
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean histogram() {
        return histogram;
    }

    public boolean localSocketDistinction() {
        return localSocketDistinction;
    }

    public long maxLatencyMs() {
        return maxLatencyMs;
    }

    public long minLatencyMs() {
        return minLatencyMs;
    }

    public List<TagConfiguration> tags() {
        return tags;
    }

    public List<Double> latencyPercentile() {
        return latencyPercentile;
    }

    public MicrometerOptions toMicrometerOption() {

        if (!enabled) {
            return MicrometerOptions.disabled();
        }

        return MicrometerOptions.builder()
                                .histogram(histogram)
                                .localDistinction(localSocketDistinction)
                                .maxLatency(Duration.ofMillis(maxLatencyMs))
                                .minLatency(Duration.ofMillis(minLatencyMs))
                                .targetPercentiles(
                                        latencyPercentile.stream().mapToDouble(Double::doubleValue).map(
                                                operand -> operand / 100d).toArray())
                                .tags(Tags.of(tags.stream()
                                                  .map(conf -> Tag.of(conf.key(), conf.value()))
                                                  .collect(Collectors.toList())))
                                .build();

    }
}
