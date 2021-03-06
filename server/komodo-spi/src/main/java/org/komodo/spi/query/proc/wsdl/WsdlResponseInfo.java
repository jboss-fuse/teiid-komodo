/*************************************************************************************
 * Copyright Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags and
 * the COPYRIGHT.txt file distributed with this work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.komodo.spi.query.proc.wsdl;

import java.util.Properties;

/**
 *
 */
public interface WsdlResponseInfo extends WsdlProcedureInfo {
    
    String SOAPENVELOPE_ROOTPATH = "/soap:Envelope";//$NON-NLS-1$
    
    String SOAPHEADER_ROOTPATH = "/soap:Header";//$NON-NLS-1$
    
    String SOAPBODY_ROOTPATH = "/soap:Body";//$NON-NLS-1$
    
    String DEFAULT_NS = "ns";//$NON-NLS-1$

    @Override
	String getDefaultProcedureName();

    String getSqlStringTemplate();

    String getSqlString(Properties properties);

}
