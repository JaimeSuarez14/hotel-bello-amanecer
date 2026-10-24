package pe.edu.hba.api.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AwsConfig {

  @Bean
  public SqsClient sqsClient(
      @Value("${aws.region}") String region,
      @Value("${aws.sqs.endpoint:}") String endpoint) {

    SqsClientBuilder builder = SqsClient.builder().region(Region.of(region));

    if (endpoint != null && !endpoint.isBlank()) {
      // Modo local (ElasticMQ)
      builder.endpointOverride(URI.create(endpoint))
          .credentialsProvider(StaticCredentialsProvider.create(
              AwsBasicCredentials.create("local", "local")));
    }
    // Si endpoint está vacío → modo AWS (usa el default credentials provider)
    return builder.build();
  }
}