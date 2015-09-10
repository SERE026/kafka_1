package com.provider.service;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class KafkaProducerService{
	
	private final Producer<String,String> producer;
	
	public final static String TOPIC = "TEST-TOPIC";

	public KafkaProducerService() {
		super();
		
		Properties props = new Properties();
		//kafaka 端口
		props.put("metadata.broker.list", "192.168.1.222:9092");
		//配置value的序列化类
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		//配置key的序列化类
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		//request.required.acks
		props.put("request.required.acks", "-1");
		
		producer = new Producer<String,String>(new ProducerConfig(props));
		
	}
	
	
	void produce(){
		int messageNo = 1000;
		final int COUNT = 10000;
		while(messageNo < COUNT){
			String key = String.valueOf(messageNo);
			String data = "hello kafka message" + key;
			producer.send(new KeyedMessage<String,String>(TOPIC,key,data));
			System.out.println(data);
			messageNo ++;
			
		}
	}
	
	void close(){
		producer.close();
	}
	
	public static void main(String[] args) {
		new KafkaProducerService().produce();
	}
	
}

