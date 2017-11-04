package com.ytx.dao;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerClient {

	public static void main(String[] args) {
		Properties pros = new Properties();
		pros.put("bootstrap.servers", "116.196.100.37:9092");
		pros.put("acks", "all");
		pros.put("retries", 0);
		pros.put("batch.size", 16384);
		pros.put("linger.ms", 1);
		pros.put("buffer.memory", 33554432);
		pros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		pros.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(pros);
		for (int i = 0; i < 100000; i++) {
			producer.send(new ProducerRecord<String, String>("my-replicated-topic", Integer.toString(i+10000),Integer.toString(i+10000)));
		}
		System.out.println("message sent successfully");
		producer.close();
		 
	}
	
}
