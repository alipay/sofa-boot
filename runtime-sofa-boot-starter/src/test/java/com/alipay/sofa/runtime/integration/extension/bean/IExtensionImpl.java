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
package com.alipay.sofa.runtime.integration.extension.bean;

import com.alipay.sofa.runtime.api.component.Extension;
import com.alipay.sofa.runtime.integration.extension.descriptor.ClientExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.ContextExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.MapExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.ParentExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.SimpleExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.ListExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.SpringListExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.SpringMapExtensionDescriptor;
import com.alipay.sofa.runtime.integration.extension.descriptor.SpringSimpleExtensionDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khotyn
 * @author ruoshan
 * @since 2.6.0
 */
public class IExtensionImpl implements IExtension {

    private String                           clientValue;

    private SimpleExtensionDescriptor        simpleExtensionDescriptor;

    private List<String>                     testList               = new ArrayList<>();

    private Map<String, String>              testMap                = new HashMap<>();

    private SimpleSpringBean                 simpleSpringBean;

    private List<SimpleSpringListBean>       simpleSpringListBeans  = new ArrayList<>();

    private Map<String, SimpleSpringMapBean> simpleSpringMapBeanMap = new HashMap<>();

    private String                           testContextValue;

    private String                           testParentValue;

    public SimpleExtensionDescriptor getSimpleExtensionDescriptor() {
        return simpleExtensionDescriptor;
    }

    @Override
    public String getClientValue() {
        return clientValue;
    }

    @Override
    public List<String> getTestList() {
        return testList;
    }

    @Override
    public Map<String, String> getTestMap() {
        return testMap;
    }

    @Override
    public SimpleSpringBean getSimpleSpringBean() {
        return simpleSpringBean;
    }

    @Override
    public List<SimpleSpringListBean> getSimpleSpringListBeans() {
        return simpleSpringListBeans;
    }

    @Override
    public Map<String, SimpleSpringMapBean> getSimpleSpringMapBeanMap() {
        return simpleSpringMapBeanMap;
    }

    public String getTestContextValue() {
        return testContextValue;
    }

    public String getTestParentValue() {
        return testParentValue;
    }

    /**
     * Component method, framework will invoke this method to contribute the extension to the existing extension point.
     *
     * @param extension extension
     * @throws Exception any exception
     */
    public void registerExtension(Extension extension) throws Exception {
        Object[] contributions = extension.getContributions();
        String extensionPoint = extension.getExtensionPoint();

        if (contributions == null) {
            return;
        }

        for (Object contribution : contributions) {
            if ("simple".equals(extensionPoint)) {
                simpleExtensionDescriptor = (SimpleExtensionDescriptor) contribution;
            } else if ("clientValue".equals(extensionPoint)) {
                clientValue = ((ClientExtensionDescriptor) contribution).getValue();
            } else if ("testList".equals(extensionPoint)) {
                testList.addAll(((ListExtensionDescriptor) contribution).getValues());
            } else if ("testMap".equals(extensionPoint)) {
                testMap.putAll(((MapExtensionDescriptor) contribution).getValues());
            } else if ("simpleSpring".equals(extensionPoint)) {
                simpleSpringBean = ((SpringSimpleExtensionDescriptor) contribution).getValue();
            } else if ("testSpringList".equals(extensionPoint)) {
                simpleSpringListBeans.addAll(((SpringListExtensionDescriptor) contribution)
                    .getValues());
            } else if ("testSpringMap".equals(extensionPoint)) {
                simpleSpringMapBeanMap.putAll(((SpringMapExtensionDescriptor) contribution)
                    .getValues());
            } else if ("testContext".equals(extensionPoint)) {
                testContextValue = ((ContextExtensionDescriptor) contribution).getContextValue();
            } else if ("testParent".equals(extensionPoint)) {
                testParentValue = ((ParentExtensionDescriptor) contribution)
                    .getSubExtensionDescriptor().getParentValue().getValue();
            }
        }
    }
}
