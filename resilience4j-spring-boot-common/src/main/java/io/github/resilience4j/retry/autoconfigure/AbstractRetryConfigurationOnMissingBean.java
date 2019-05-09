/*
 * Copyright 2019 Mahmoud Romeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.resilience4j.retry.autoconfigure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.github.resilience4j.consumer.EventConsumerRegistry;
import io.github.resilience4j.fallback.FallbackDecorators;
import io.github.resilience4j.recovery.autoconfigure.FallbackConfigurationOnMissingBean;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.configure.ReactorRetryAspectExt;
import io.github.resilience4j.retry.configure.RetryAspect;
import io.github.resilience4j.retry.configure.RetryAspectExt;
import io.github.resilience4j.retry.configure.RetryConfiguration;
import io.github.resilience4j.retry.configure.RetryConfigurationProperties;
import io.github.resilience4j.retry.configure.RxJava2RetryAspectExt;
import io.github.resilience4j.retry.event.RetryEvent;
import io.github.resilience4j.utils.ReactorOnClasspathCondition;
import io.github.resilience4j.utils.RxJava2OnClasspathCondition;

/**
 * {@link Configuration
 * Configuration} for resilience4j-retry.
 */
@Configuration
@Import(FallbackConfigurationOnMissingBean.class)
public abstract class AbstractRetryConfigurationOnMissingBean {

	protected final RetryConfiguration retryConfiguration;

	public AbstractRetryConfigurationOnMissingBean() {
		this.retryConfiguration = new RetryConfiguration();
	}

	/**
	 * @param retryConfigurationProperties retryConfigurationProperties retry configuration spring properties
	 * @param retryEventConsumerRegistry   the event retry registry
	 * @return the retry definition registry
	 */
	@Bean
	@ConditionalOnMissingBean
	public RetryRegistry retryRegistry(RetryConfigurationProperties retryConfigurationProperties, EventConsumerRegistry<RetryEvent> retryEventConsumerRegistry) {
		return retryConfiguration.retryRegistry(retryConfigurationProperties, retryEventConsumerRegistry);
	}

	/**
	 * @param retryConfigurationProperties retry configuration spring properties
	 * @param retryRegistry                retry in memory registry
	 * @return the spring retry AOP aspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public RetryAspect retryAspect(RetryConfigurationProperties retryConfigurationProperties,
								   RetryRegistry retryRegistry, @Autowired(required = false) List<RetryAspectExt> retryAspectExtList,
								   FallbackDecorators fallbackDecorators) {
		return retryConfiguration.retryAspect(retryConfigurationProperties, retryRegistry, retryAspectExtList, fallbackDecorators);
	}

	@Bean
	@Conditional(value = {RxJava2OnClasspathCondition.class})
	@ConditionalOnMissingBean
	public RxJava2RetryAspectExt rxJava2RetryAspectExt() {
		return retryConfiguration.rxJava2RetryAspectExt();
	}

	@Bean
	@Conditional(value = {ReactorOnClasspathCondition.class})
	@ConditionalOnMissingBean
	public ReactorRetryAspectExt reactorRetryAspectExt() {
		return retryConfiguration.reactorRetryAspectExt();
	}

}
