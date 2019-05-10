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
package com.alipay.sofa.boot.autoconfigure.runtime;

import com.alipay.sofa.boot.constant.SofaBootConstants;
import com.alipay.sofa.boot.util.SofaBootEnvUtils;
import com.alipay.sofa.boot.log.SofaLogger;
import com.alipay.sofa.runtime.spring.initializer.RuntimeContextInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author xuanbei 18/3/13
 */
public class SofaRuntimeSpringContextInitializer
                                                implements
                                                ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        RuntimeContextInitializer.sofaRuntimeContext(environment
            .getProperty(SofaBootConstants.APP_NAME_KEY));
        RuntimeContextInitializer.initialize(applicationContext);
        if (SofaBootEnvUtils.isSpringCloudBootstrapEnvironment(environment)) {
            return;
        }
        SofaLogger.info("SOFABoot Runtime Starting!");
    }
}
