package com.learnkafkastreams.launcher;

import com.learnkafkastreams.topology.GreetingsTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class GreetingsStreamApp {

    public static void main(String[] args) {
        Properties properties = new Properties();
        // application id je jako bitan, on je pandam consumer grupi u Kafka Consumer
        // tako zna koje su poruke procitane u kafka topiku, tako da kad ponovo pokrenemo app
        // zna gde da pocne da cita iz tog kafka topika
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "greetings-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        
        createTopics(properties, List.of(GreetingsTopology.GREETINGS, GreetingsTopology.GREETINGS_UPPERCASE));
        Topology greetingsTopology = GreetingsTopology.buildTopology();
        
        // pokrece kafka app, kreira topologiju
        KafkaStreams kafkaStreams = new KafkaStreams(greetingsTopology, properties);
        
        // kad se aplikacija ugasi, treba da otpustimo sve resurse kafke
        Runtime.getRuntime()
               .addShutdownHook(new Thread(kafkaStreams::close));
        try {
            kafkaStreams.start();
        } catch (Exception e) {
            log.error("Exception in starting stream: {}", e.getMessage(), e);
        }
    }

    private static void createTopics(Properties config, List<String> greetings) {

        AdminClient admin = AdminClient.create(config);
        var partitions = 1;
        short replication  = 1;

        var newTopics = greetings
                .stream()
                .map(topic ->{
                    return new NewTopic(topic, partitions, replication);
                })
                .collect(Collectors.toList());

        var createTopicResult = admin.createTopics(newTopics);
        try {
           createTopicResult
                    .all().get();
            log.info("topics are created successfully");
        } catch (Exception e) {
            log.error("Exception creating topics : {} ",e.getMessage(), e);
        }
    }
}
