package com.ytx.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
/*
 * 
 */
public class KafkaConsumerClient {
	public static void main(String[] args) {
		boolean running = true;
		//String topicName = "dual-part-topic";
		String topicName = "my-replicated-topic";
		Properties props = new Properties();
		
		props.put("bootstrap.servers", "116.196.100.37:9092");
		props.put("group.id", "testdualpart");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer",   
		 "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", 
		 "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topicName));
		System.out.println("Subscribed to topic" + topicName);
		/*
		 * 自动控制offset
		 */
		/*try {
			while(running) {
				ConsumerRecords<String, String> records = consumer.poll(100);
				for (ConsumerRecord<String, String> record : records) {
					System.out.printf("offset = %d ,key = %s, value = %s, time = %s\n",
							record.offset(),record.key(),record.value(),(new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss").format(new Date(record.timestamp()))));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}*/
		
		/*
		 * 手动控制offset\n
		 * 已提交的offset应始终是你的程序将读取的下一条消息的offset。\n
		 * 因此，调用commitSync（offsets）时，你应该加1个到最后处理的消息的offset。
		 */
		try {
	        while(running) {
	            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
	            for (TopicPartition partition : records.partitions()) {
	                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
	                for (ConsumerRecord<String, String> record : partitionRecords) {
	                    System.out.println(record.offset() + ": " + record.value());
	                }
	                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
	                System.out.println("the lastOffset = " + lastOffset);
	                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
	            }
	        }
	    } finally {
	    	consumer.close();
	    }
	}
}
