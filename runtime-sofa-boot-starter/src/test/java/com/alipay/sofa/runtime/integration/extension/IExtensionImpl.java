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
package com.alipay.sofa.runtime.integration.extension;

/**
 * @author khotyn
 * @since 2.6.0
 */
import com.alipay.sofa.service.api.component.Extension;

public class IExtensionImpl implements IExtension {

    private String word;

    private String clientWord;

    @Override
    public String say() {
        return word;
    }

    @Override
    public String sayFromClient() {
        return clientWord;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setClientWord(String clientWord) {
        this.clientWord = clientWord;
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
            if ("word".equals(extensionPoint)) {
                setWord(((ExtensionDescriptor) contribution).getValue());
            } else if ("clientWord".equals(extensionPoint)) {
                setClientWord(((ClientExtensionDescriptor) contribution).getValue());
            }
        }
    }
}
