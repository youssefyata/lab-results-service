package com.yata.labingest.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SpaceConfig {

    @Value("${SPACE_KEY}")
    private String spaceKey;

    @Value("${SPACE_SECRET}")
    private String spaceSecret;

    @Value("${SPACE_ENDPOINT}")
    private String spaceEndpoint;

    @Value("${SPACE_REGION}")
    private String spaceRegion;

    @Bean
    @Profile("dev")
    public AmazonS3 getS3forDev() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(spaceKey, spaceSecret);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(spaceEndpoint, spaceRegion))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(true)
                .build();
    }

    @Bean
    @Profile("!dev")
    public AmazonS3 getS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(spaceKey, spaceSecret);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(spaceEndpoint, spaceRegion))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
