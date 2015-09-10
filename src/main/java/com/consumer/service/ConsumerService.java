package com.consumer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import com.provider.service.ProducerService;

public class ConsumerService {

	private final ConsumerConnector consumer;
	
	private ConsumerService(){
		Properties props = new Properties();
		
		//zookeeper 配置
		props.put("zookeeper.connect","192.168.1.222:2181,192.168.1.222:2182,192.168.1.222:2183");
		
		// group 代表一个消费小组
		props.put("group.id", "jd-group2");
		
		//zk 连接超时
		props.put("zookeeper.session.timeout.ms", "10000");
		props.put("zookeeper.sync.tims.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");
		
		//序列化类
		props.put("serializer.class", "kafka.serializer.StringEnoder");
		
		ConsumerConfig config = new ConsumerConfig(props);
		
		consumer = Consumer.createJavaConsumerConnector(config);
		
	}
	
	void consume(){
		Map<String,Integer> topicCountMap = new HashMap<String,Integer>();
		topicCountMap.put(ProducerService.TOPIC,new Integer(1));
		
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
		
		Map<String,List<KafkaStream<String,String>>> consumerMap =
				consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
		
		KafkaStream<String,String> stream = consumerMap.get(ProducerService.TOPIC).get(0);
		
		ConsumerIterator it = stream.iterator();
		while(it.hasNext()){
			System.out.println("This is Client print--->"+it.next().message());
		}
	}
	
	void close(){
		consumer.shutdown();
	}
	
	public static void main(String[] args) {
//		ConsumerService consumerService = new ConsumerService();
//		consumerService.consume();
//		consumerService.close();
		new ConsumerService().consume();
	}
}
