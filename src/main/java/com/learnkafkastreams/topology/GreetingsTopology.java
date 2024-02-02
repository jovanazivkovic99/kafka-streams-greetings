package com.learnkafkastreams.topology;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

public class GreetingsTopology {
    
    // source topic
    public static String GREETINGS = "greetings";
    // destination topic
    public static String GREETINGS_UPPERCASE = "greetings_uppercase";
    
    // Topology je klasa u kafka streamu koji ima citav flow kafka streama
    public static Topology buildTopology () {
        // definisemo uz pomoc njega soruce processor, stream processing logc and
        // sync processor
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        
        // source processor
        // u kafka stream api serializer i deserializer se kategorise kao Serdes
        // u pozadini ovo koristi ConsumerAPI
        KStream<String, String> greetingsStream = streamsBuilder
                .stream(GREETINGS, Consumed.with(Serdes.String(), Serdes.String()));
        
        // pretvaramo iz lowercase u uppercase
        // mapValues() dace nam pristup vrednosti iz kafka topika koji smo konsumovali
        KStream<String, String> modifiedStream = greetingsStream
                .mapValues((readOnlyKey, value) -> value.toUpperCase());
        
        // publish value to topic
        // u pozadini koristi ProducerAPI
        modifiedStream
                .to(GREETINGS_UPPERCASE, Produced.with(Serdes.String(), Serdes.String()));
        
        return streamsBuilder.build();
    }
}
