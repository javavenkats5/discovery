package com.sv.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.netflix.appinfo.AmazonInfo;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class DiscoveryApplication {

	@Value("${server.port:8765}")
	private int port;

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryApplication.class, args);
	}

	@Bean
	@Autowired
	@Profile({ "aws", "cloud-qa" })
	public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils) {
		EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
		AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
		config.setHostname(info.get(AmazonInfo.MetaDataKey.publicHostname));
		config.setIpAddress(info.get(AmazonInfo.MetaDataKey.publicIpv4));
		config.setNonSecurePort(port);
		config.setDataCenterInfo(info);
		return config;
	}

}
