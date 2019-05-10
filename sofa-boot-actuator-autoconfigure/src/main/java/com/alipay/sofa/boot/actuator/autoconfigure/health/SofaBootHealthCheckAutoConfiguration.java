/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.boot.actuator.autoconfigure.health;

import com.alipay.sofa.boot.actuator.health.SofaModuleHealthIndicator;
import com.alipay.sofa.healthcheck.impl.DefaultRuntimeHealthChecker;
import com.alipay.sofa.boot.actuator.health.MultiApplicationHealthIndicator;
import com.alipay.sofa.boot.actuator.health.ReadinessEndpointWebExtension;
import com.alipay.sofa.boot.actuator.health.SofaBootHealthIndicator;
import com.alipay.sofa.boot.actuator.health.SofaBootReadinessEndpoint;
import com.alipay.sofa.healthcheck.impl.SofaComponentHealthChecker;
import com.alipay.sofa.boot.actuator.health.SofaComponentHealthIndicator;
import com.alipay.sofa.healthcheck.AfterReadinessCheckCallbackProcessor;
import com.alipay.sofa.healthcheck.core.HealthChecker;
import com.alipay.sofa.healthcheck.HealthCheckerProcessor;
import com.alipay.sofa.healthcheck.HealthIndicatorProcessor;
import com.alipay.sofa.healthcheck.ReadinessCheckListener;
import com.alipay.sofa.healthcheck.impl.SofaModuleHealthChecker;
import com.alipay.sofa.isle.stage.ModelCreatingStage;
import com.alipay.sofa.runtime.spi.component.SofaRuntimeContext;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorProperties;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.health.HealthStatusHttpMapper;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qilong.zql
 * @since 2.5.0
 */
@Configuration
@ConditionalOnClass(HealthChecker.class)
public class SofaBootHealthCheckAutoConfiguration {

    @Bean
    public ReadinessCheckListener readinessCheckListener() {
        return new ReadinessCheckListener();
    }

    @Bean
    public HealthCheckerProcessor healthCheckerProcessor() {
        return new HealthCheckerProcessor();
    }

    @Bean
    public HealthIndicatorProcessor healthIndicatorProcessor() {
        return new HealthIndicatorProcessor();
    }

    @Bean
    public AfterReadinessCheckCallbackProcessor afterReadinessCheckCallbackProcessor() {
        return new AfterReadinessCheckCallbackProcessor();
    }

    @Bean
    public SofaBootHealthIndicator sofaBootHealthIndicator() {
        return new SofaBootHealthIndicator();
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public DefaultRuntimeHealthChecker defaultRuntimeHealthChecker(SofaRuntimeContext sofaRuntimeContext) {
        return new DefaultRuntimeHealthChecker(sofaRuntimeContext);
    }

    @Bean
    public MultiApplicationHealthIndicator multiApplicationHealthIndicator() {
        return new MultiApplicationHealthIndicator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledEndpoint(endpoint = SofaBootReadinessEndpoint.class)
    public SofaBootReadinessEndpoint sofaBootReadinessCheckEndpoint() {
        return new SofaBootReadinessEndpoint();
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public SofaComponentHealthIndicator sofaComponentHealthIndicator(SofaRuntimeContext sofaRuntimeContext) {
        return new SofaComponentHealthIndicator(sofaRuntimeContext);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public SofaComponentHealthChecker sofaComponentHealthChecker(SofaRuntimeContext sofaRuntimeContext) {
        return new SofaComponentHealthChecker(sofaRuntimeContext);
    }

    @Configuration
    @ConditionalOnBean(ModelCreatingStage.class)
    public static class SofaModuleHealthIndicatorConfiguration {
        @Bean
        public SofaModuleHealthIndicator sofaModuleHealthIndicator() {
            return new SofaModuleHealthIndicator();
        }

        @Bean
        public SofaModuleHealthChecker sofaModuleHealthChecker() {
            return new SofaModuleHealthChecker();
        }
    }

    @Configuration
    @AutoConfigureBefore(HealthEndpointAutoConfiguration.class)
    @ConditionalOnClass(HealthChecker.class)
    public static class ReadinessCheckExtensionConfiguration {
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnEnabledEndpoint(endpoint = ReadinessEndpointWebExtension.class)
        public ReadinessEndpointWebExtension readinessEndpointWebExtension() {
            return new ReadinessEndpointWebExtension();
        }

        @Bean
        @ConditionalOnMissingBean
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        public HealthStatusHttpMapper createHealthStatusHttpMapper(HealthIndicatorProperties healthIndicatorProperties) {
            HealthStatusHttpMapper statusHttpMapper = new HealthStatusHttpMapper();
            if (healthIndicatorProperties.getHttpMapping() != null) {
                statusHttpMapper.addStatusMapping(healthIndicatorProperties.getHttpMapping());
            }
            statusHttpMapper.addStatusMapping(Status.UNKNOWN,
                WebEndpointResponse.STATUS_INTERNAL_SERVER_ERROR);
            return statusHttpMapper;
        }
    }
}