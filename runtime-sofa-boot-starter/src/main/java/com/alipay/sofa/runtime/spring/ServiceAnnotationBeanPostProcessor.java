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
package com.alipay.sofa.runtime.spring;

import com.alipay.sofa.runtime.api.ServiceRuntimeException;
import com.alipay.sofa.runtime.api.annotation.SofaJvmReference;
import com.alipay.sofa.runtime.api.annotation.SofaJvmService;
import com.alipay.sofa.runtime.model.InterfaceMode;
import com.alipay.sofa.runtime.service.binding.JvmBinding;
import com.alipay.sofa.runtime.service.component.Reference;
import com.alipay.sofa.runtime.service.component.Service;
import com.alipay.sofa.runtime.service.component.ServiceComponent;
import com.alipay.sofa.runtime.service.component.impl.ReferenceImpl;
import com.alipay.sofa.runtime.service.component.impl.ServiceImpl;
import com.alipay.sofa.runtime.service.helper.ReferenceRegisterHelper;
import com.alipay.sofa.runtime.spi.binding.BindingAdapterFactory;
import com.alipay.sofa.runtime.spi.component.ComponentInfo;
import com.alipay.sofa.runtime.spi.component.DefaultImplementation;
import com.alipay.sofa.runtime.spi.component.Implementation;
import com.alipay.sofa.runtime.spi.component.SofaRuntimeContext;
import com.alipay.sofa.runtime.spring.config.SofaRuntimeProperties;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author xuanbei 18/5/9
 */
public class ServiceAnnotationBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {
    private SofaRuntimeContext    sofaRuntimeContext;
    private SofaRuntimeProperties sofaRuntimeProperties;
    private BindingAdapterFactory bindingAdapterFactory;

    public ServiceAnnotationBeanPostProcessor(SofaRuntimeContext sofaRuntimeContext,
                                              SofaRuntimeProperties sofaRuntimeProperties,
                                              BindingAdapterFactory bindingAdapterFactory) {
        this.sofaRuntimeContext = sofaRuntimeContext;
        this.sofaRuntimeProperties = sofaRuntimeProperties;
        this.bindingAdapterFactory = bindingAdapterFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
                                                                               throws BeansException {
        processSofaReference(bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
                                                                              throws BeansException {
        processSofaService(bean, beanName);
        return bean;
    }

    private void processSofaService(Object bean, String beanName) {
        final Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

        SofaJvmService sofaJvmServiceAnnotation = beanClass.getAnnotation(SofaJvmService.class);

        if (sofaJvmServiceAnnotation == null) {
            return;
        }

        Class<?> interfaceType = sofaJvmServiceAnnotation.interfaceType();

        if (interfaceType.equals(void.class)) {
            Class<?> interfaces[] = beanClass.getInterfaces();

            if (interfaces == null || interfaces.length == 0 || interfaces.length > 1) {
                throw new ServiceRuntimeException(
                    "Bean " + beanName
                            + " does not has any interface or has more than one interface.");
            }

            interfaceType = interfaces[0];
        }

        Implementation implementation = new DefaultImplementation(bean);
        Service service = new ServiceImpl(sofaJvmServiceAnnotation.uniqueId(), interfaceType,
            InterfaceMode.annotation, bean);
        service.addBinding(new JvmBinding());
        ComponentInfo componentInfo = new ServiceComponent(implementation, service,
            bindingAdapterFactory, sofaRuntimeContext);
        sofaRuntimeContext.getComponentManager().register(componentInfo);
    }

    private void processSofaReference(final Object bean) {
        final Class<?> beanClass = bean.getClass();

        ReflectionUtils.doWithFields(beanClass, new ReflectionUtils.FieldCallback() {

            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                SofaJvmReference sofaJvmReferenceAnnotation = field
                    .getAnnotation(SofaJvmReference.class);

                if (sofaJvmReferenceAnnotation == null) {
                    return;
                }

                Class<?> interfaceType = sofaJvmReferenceAnnotation.interfaceType();

                if (interfaceType.equals(void.class)) {
                    interfaceType = field.getType();
                }

                Reference reference = new ReferenceImpl(sofaJvmReferenceAnnotation.uniqueId(),
                    interfaceType, InterfaceMode.annotation, false, false);
                reference.addBinding(new JvmBinding());
                Object proxy = ReferenceRegisterHelper.registerReference(reference,
                    bindingAdapterFactory, sofaRuntimeProperties, sofaRuntimeContext);
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, proxy);
            }
        }, new ReflectionUtils.FieldFilter() {

            @Override
            public boolean matches(Field field) {
                return !Modifier.isStatic(field.getModifiers())
                       && field.isAnnotationPresent(SofaJvmReference.class);
            }
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}