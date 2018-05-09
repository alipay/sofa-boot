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
package com.alipay.sofa.runtime.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xuanbei 18/5/9
 */
@ConfigurationProperties(SofaRuntimeProperties.PREFIX)
public class SofaRuntimeProperties {
    static final String PREFIX                      = "com.alipay.sofa.runtime";

    private boolean     skipJvmReferenceHealthCheck = false;
    private boolean     disableLocalFirst           = false;

    public boolean isSkipJvmReferenceHealthCheck() {
        return skipJvmReferenceHealthCheck;
    }

    public void setSkipJvmReferenceHealthCheck(boolean skipJvmReferenceHealthCheck) {
        this.skipJvmReferenceHealthCheck = skipJvmReferenceHealthCheck;
    }

    public boolean isDisableLocalFirst() {
        return disableLocalFirst;
    }

    public void setDisableLocalFirst(boolean disableLocalFirst) {
        this.disableLocalFirst = disableLocalFirst;
    }
}
