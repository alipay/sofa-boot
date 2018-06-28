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
package com.alipay.sofa.runtime.integration.base;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author qilong.zql
 * @since 2.3.1
 */
@ImportResource({ "classpath*:META-INF/spring/*.xml" })
@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan(basePackages = { "com.alipay.sofa.runtime.beans.configuration",
                               "com.alipay.sofa.runtime.integration.base" })
public class SofaBootTestApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SofaBootTestApplication.class);
        springApplication.run(args);
    }

}