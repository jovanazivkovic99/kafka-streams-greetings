package com.learnkafkastreams.topology;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

/**
 * The `GreetingsTopology` class is used for creating a topology for processing Kafka streams. This class defines the
 * flow from an input to an output topic, transforming messages from one form to another.
 */
public class GreetingsTopology {
    
    // Input topic
    public static String GREETINGS = "greetings";
    // Output topic
    public static String GREETINGS_UPPERCASE = "greetings_uppercase";
    
    /**
     * Method to build the Kafka Stream topology.
     *
     * @return Topology Object representing the defined stream processing topology.
     */
    public static Topology buildTopology () {
       /* With StreamBuilder we define source processor, stream processing logic
        and sync processor*/
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        
        /*Definition of the input stream
        - u KafkaStreamsAPI serializer i deserializer se kategorise kao Serdes
        - u pozadini ovo koristi ConsumerAPI*/
        KStream<String, String> greetingsStream = streamsBuilder
                .stream(GREETINGS, Consumed.with(Serdes.String(), Serdes.String()));
        
        // Logging the input stream.
        greetingsStream.print(Printed.<String, String>toSysOut().withLabel("greetingsStream"));
        
        /*Transforming messages to uppercase.
        mapValues() gives us values from kafka topic that we consumed*/
        KStream<String, String> modifiedStream = greetingsStream
                .mapValues((readOnlyKey, value) -> value.toUpperCase());
        
        // Logging the transformed stream.
        modifiedStream.print(Printed.<String, String>toSysOut().withLabel("modifiedStream"));
        
        /*Publishing messages to the output topic.
        u pozadini koristi ProducerAPI*/
        modifiedStream
                .to(GREETINGS_UPPERCASE, Produced.with(Serdes.String(), Serdes.String()));
        
        return streamsBuilder.build();
    }
}
