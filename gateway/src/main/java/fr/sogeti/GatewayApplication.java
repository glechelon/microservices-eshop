
package fr.sogeti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RestController;

@EnableHystrixDashboard
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@RestController
public class GatewayApplication {

	@Autowired
	private DiscoveryClient discoveryClient;

	public static void main(String[] args) {
		// new ConsulServiceDiscovery("http://10.226.159.191",
		// 8500).getServices();
		SpringApplication.run(GatewayApplication.class, args);
		// System.out.println(discovery);
	}

}
