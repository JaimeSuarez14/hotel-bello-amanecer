package main.java.pe.edu.hba.pagos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class AwsConfig {  
  @Bean
  public SqsClient sqsClient(
      @Value("${spring.cloud.aws.region.static}") String region,
      @Value("${spring.cloud.aws.sqs.endpoint:}") String endpoint) {

    var builder = SqsClient.builder().region(Region.of(region));
    if (endpoint != null && !endpoint.isBlank()) {
      builder.endpointOverride(URI.create(endpoint))
          .credentialsProvider(StaticCredentialsProvider.create(
              AwsBasicCredentials.create("local", "local")));
    }
    return builder.build();
  }
}