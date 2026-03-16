package gtanks.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaConfig {
    private Properties properties;
    private static final String KAFKA_SERVER = "KAFKA_SERVER";
    private static final String SECURITY_PROTOCOL = "SECURITY_PROTOCOL";
    private static final String SASL_MECHANISM = "SASL_MECHANISM";
    private static final String SASL_JAAS = "SASL_JAAS";
    private static final String KAFKA_USERNAME = "KAFKA_USERNAME";
    private static final String KAFKA_PASSWORD = "KAFKA_PASSWORD";
    private static final String GROUP_ID = "GROUP_ID";

    public KafkaConfig() {
        properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(KAFKA_SERVER));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, System.getenv(SECURITY_PROTOCOL));
        properties.put(SaslConfigs.SASL_MECHANISM, System.getenv(SASL_MECHANISM));
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, String.format("%s username=\"%s\" password=\"%s\";", System.getenv(SASL_JAAS),
                        System.getenv(KAFKA_USERNAME), System.getenv(KAFKA_PASSWORD)));
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, System.getenv(GROUP_ID));
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    }

    public Properties getProperties() {
        return properties;
    }

    public void setAdditionalProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getPropertyValue(String key) {
        return properties.getProperty(key);
    }
}

