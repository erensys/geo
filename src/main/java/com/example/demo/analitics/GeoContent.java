package com.example.demo.analitics;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/geo")
public class GeoContent {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	/**
	 * Get Context for development branch.
	 *
	 * @return
	 * @throws MalformedURLException
	 */
	@GetMapping("/content")
	public String getContext() throws MalformedURLException {
		String url = "/solution/text";

		List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("geo-content");

		if (!Objects.isNull(serviceInstanceList) && !serviceInstanceList.isEmpty()) {
			ServiceInstance serviceIntance = serviceInstanceList.get(0);
			String path = serviceIntance.getUri().toString();

			System.out.println("Path ::: " + path);

			return restTemplate.getForObject(path + url, String.class);
		}
		return "";
	}

	/**
	 * Get Service instances for dev.
	 *
	 * Get service Instances for master.
	 *
	 * @param applicationName
	 * @return
	 */
	@GetMapping("/instances/{applicationName}")
	public List<ServiceInstance> getServiceInstances(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

}
