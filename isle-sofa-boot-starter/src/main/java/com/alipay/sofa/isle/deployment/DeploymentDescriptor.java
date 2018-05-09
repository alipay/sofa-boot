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
package com.alipay.sofa.isle.deployment;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

/**
 * SOFAIsle Module Deployment Descriptor.
 *
 * @author yangyanzhao
 * @version $Id: DeploymentDescriptor.java, v 0.1 2012-1-10 12:28:28 yangyanzhao Exp $
 */
public interface DeploymentDescriptor extends Comparable<DeploymentDescriptor> {
    /**
     * get SOFAIsle Module Name.
     *
     * @return SOFAIsle Module Name
     */
    String getModuleName();

    /**
     * get Deployment Descriptor Name.
     *
     * @return Deployment Descriptor Name
     */
    String getName();

    /**
     * get modules that dependent on.
     *
     * @return modules that dependent on.
     */
    List<String> getRequiredModules();

    /**
     * get Property.
     *
     * @param key property key
     * @return property value
     */
    String getProperty(String key);

    /**
     * get Spring Parent of this SOFAIsle module.
     *
     * @return Spring Parent of this SOFAIsle module
     */
    String getSpringParent();

    /**
     * get classloader of this SOFAIsle module.
     *
     * @return classloader of this SOFAIsle module
     */
    ClassLoader getClassLoader();

    /**
     * set application context of this SOFAIsle module.
     *
     * @param context application context of this SOFAIsle module.
     */
    void setApplicationContext(ApplicationContext context);

    /**
     * get application context of this SOFAIsle module.
     *
     * @return application context of this SOFAIsle module
     */
    ApplicationContext getApplicationContext();

    /**
     * add installed spring xml of this SOFAIsle module.
     *
     * @param fileName spring xml filename
     */
    void addInstalledSpringXml(String fileName);

    /**
     * get all installed spring xml files of this SOFAIsle module.
     *
     * @return
     */
    List<String> getInstalledSpringXml();

    /**
     * determine whether this SOFAIsle module is spring powered.
     *
     * @return true or false
     */
    boolean isSpringPowered();

    /**
     * invoke when deploy this SOFAIsle module.
     */
    void startDeploy();

    /**
     * invoke when deploy finished.
     */
    void deployFinish();

    /**
     * get Spring resources of this SOFAIsle module.
     *
     * @return
     */
    Map<String, Resource> getSpringResources();

    /**
     * get deploy elapsed time of this SOFAIsle module.
     *
     * @return
     */
    long getElapsedTime();

    /**
     * get start deploy time of this SOFAIsle module.
     *
     * @return
     */
    long getStartTime();
}
