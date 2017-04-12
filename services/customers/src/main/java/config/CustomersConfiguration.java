package config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class CustomersConfiguration extends Configuration {

	@NotEmpty
	private String RabbitMQ_URL;

	@JsonProperty
	public String getRabbitMQ_URL() {
		return RabbitMQ_URL;
	}

	@JsonProperty
	public void setRabbitMQ_URL(String rabbitMQ_URL) {
		RabbitMQ_URL = rabbitMQ_URL;
	}

}
