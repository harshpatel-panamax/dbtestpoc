package org.panamax.poc;

import brave.Tracing;
import brave.http.HttpTracing;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.metrics.MicrometerCommandLatencyRecorder;
import io.lettuce.core.metrics.MicrometerOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.tracing.BraveTracing;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.panamax.poc.configuration.RedisConfiguration;
import org.panamax.poc.useraccountcachemanager.CacheMatrixIdGenerator;
import org.panamax.poc.useraccountcachemanager.DelegateUserAccountCacheManager;
import org.panamax.poc.useraccountcachemanager.ExceptionLoggingHandler;
import org.panamax.poc.useraccountcachemanager.MatrixIdGenerator;
import org.panamax.poc.useraccountcachemanager.MatrixRecorder;
import org.panamax.poc.useraccountcachemanager.MemoizedMatrixProvider;
import org.panamax.poc.useraccountcachemanager.RedisUserAccountCacheDriver;
import org.panamax.poc.useraccountcachemanager.UserAccountCacheManager;
import org.panamax.poc.useraccountcachemanager.UserAccountData;
import zipkin2.Endpoint;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        RedisConfiguration redisConfiguration = RedisConfiguration.read();

        List<MeterRegistry> registries = redisConfiguration.micrometerConfiguration().registries();
        CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry(Clock.SYSTEM, registries);

        ClientResources.Builder builder = ClientResources.builder();

        brave.Tracing clientTracing = Tracing.newBuilder()
                                             .localServiceName("testing")
                                             .localIp("127.0.0.1")
                                             .localPort(9411)
                                             .build();

        HttpTracing build = HttpTracing.newBuilder(clientTracing).build();

        BraveTracing tracing = BraveTracing.builder()
                                           .tracing(clientTracing)
                                           .excludeCommandArgsFromSpanTags()
                                           .endpointCustomizer(builder1 -> {
                                               builder1.ip("127.0.0.1").port(9411);
                                           })
                                           .serviceName("redis")
                                           .spanCustomizer((command, span) -> span.tag("cmd", command.getType().name()))
                                           .build();


        compositeMeterRegistry.config().meterFilter(new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                if (id.getName().startsWith("lettuce")) {
                    return id.withName(id.getName().replace("lettuce", "mobifin.redislib"));
                }
                return id;
            }
        });


        MicrometerOptions options = redisConfiguration.lettuceConfiguration()
                                                      .matrixConfiguration()
                                                      .toMicrometerOption();

        MicrometerCommandLatencyRecorder latencyRecorder = new MicrometerCommandLatencyRecorder(compositeMeterRegistry,
                                                                                                options);
        builder.commandLatencyRecorder(latencyRecorder);

        ClientResources resources = builder.tracing(tracing).build();


        List<RedisURI> redisURIS = redisConfiguration.clusterConfiguration()
                                                     .serverConfigurations()
                                                     .stream()
                                                     .map(serverConfiguration -> new RedisURI(serverConfiguration.ip(),
                                                                                              serverConfiguration.port(),
                                                                                              redisConfiguration.clusterConfiguration()
                                                                                                                .connectionTimeout()))
                                                     .collect(Collectors.toList());


        try (RedisClusterClient client = RedisClusterClient.create(resources, redisURIS)) {
            RedisCodec<String, byte[]> redisCodec = RedisCodec.of(StringCodec.UTF8, new ByteArrayCodec());
            StatefulRedisClusterConnection<String, byte[]> connection = client.connect(redisCodec);

            MatrixIdGenerator idGenerator = new CacheMatrixIdGenerator("useraccount");

            MatrixRecorder recorder = new MatrixRecorder(java.time.Clock.systemDefaultZone(),
                                                         new MemoizedMatrixProvider(compositeMeterRegistry));

            DelegateUserAccountCacheManager userAccountCacheManager = new DelegateUserAccountCacheManager(
                    new UserAccountCacheManager(new RedisUserAccountCacheDriver(new RedisCacheOperation(connection),
                                                                                Ttl.of(Duration.ofDays(10)),
                                                                                new KryoObjectCodec(recorder))),
                    new ExceptionLoggingHandler(), recorder, idGenerator);

            UserAccountData userAccountData = userAccountCacheManager.get("2417e3ab-aa3b-4bd6-a7b2-ce104a9b28dc");

            System.out.println("==================" + userAccountData);

            /*String id = UUID.randomUUID().toString();
            userAccountCacheManager.set(new UserAccountData(id));
            System.out.println("Adding..." + id);*/

            /*while (true) {
                String id = UUID.randomUUID().toString();
                userAccountCacheManager.set(new UserAccountData(id));
                userAccountCacheManager.get(id);
                System.out.println("Adding..." + id);
                Thread.sleep(Duration.ofSeconds(1).toMillis());

            }*/
        }

    }
}