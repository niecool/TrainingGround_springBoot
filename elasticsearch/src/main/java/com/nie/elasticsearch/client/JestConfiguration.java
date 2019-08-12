package com.nie.elasticsearch.client;

import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass(JestClient.class)
@EnableConfigurationProperties(JestProperties.class)
@AutoConfigureBefore(JestAutoConfiguration.class)
@AutoConfigureAfter(GsonAutoConfiguration.class)
public class JestConfiguration {

    private final JestProperties properties;

    private final ObjectProvider<Gson> gsonProvider;

    public JestConfiguration(JestProperties properties,
                             ObjectProvider<Gson> gsonProvider) {
        System.out.println("========123");
        this.properties = properties;
        this.gsonProvider = gsonProvider;
    }

    @Bean
    @ConditionalOnMissingBean
    public JestClient jestClient() {
        System.out.println("========456");
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(createHttpClientConfig());
        return factory.getObject();
    }

    protected HttpClientConfig createHttpClientConfig() {
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(
                this.properties.getUris());
        if (StringUtils.hasText(this.properties.getUsername())) {
            builder.defaultCredentials(this.properties.getUsername(),
                    this.properties.getPassword());
        }
        String proxyHost = this.properties.getProxy().getHost();
        if (StringUtils.hasText(proxyHost)) {
            Integer proxyPort = this.properties.getProxy().getPort();
            Assert.notNull(proxyPort, "Proxy port must not be null");
            builder.proxy(new HttpHost(proxyHost, proxyPort));
        }
        Gson gson = this.gsonProvider.getIfUnique();
        if (gson != null) {
            builder.gson(gson);
        }
        builder.multiThreaded(true);
        builder.maxTotalConnection(100);
        builder.defaultMaxTotalConnectionPerRoute(100);
        return builder.connTimeout(this.properties.getConnectionTimeout().getNano())
                .readTimeout(this.properties.getReadTimeout().getNano()).build();
    }
}
