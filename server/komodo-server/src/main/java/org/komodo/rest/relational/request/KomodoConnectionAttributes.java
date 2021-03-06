/*
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
package org.komodo.rest.relational.request;

import javax.ws.rs.core.MediaType;

import org.komodo.rest.KRestEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * Object to be serialised by GSON that encapsulates a connection object
 */
@JsonSerialize
@JsonInclude(value=Include.NON_NULL)
public class KomodoConnectionAttributes implements KRestEntity {

    /**
     * Label for the description
     */
    public static final String DESCRIPTION_LABEL = "description"; //$NON-NLS-1$

    /**
     * Label for the serviceCatalogSource
     */
    public static final String DATA_SOURCE_LABEL = "dataSource"; //$NON-NLS-1$

    @JsonProperty(DESCRIPTION_LABEL)
    private String description;

    @JsonProperty(DATA_SOURCE_LABEL)
    private String dataSource;

    /**
     * Default constructor for deserialization
     */
    public KomodoConnectionAttributes() {
        // do nothing
    }

    @Override
    @JsonIgnore
    public boolean supports(MediaType mediaType) {
        return MediaType.APPLICATION_JSON_TYPE.equals(mediaType);
    }

    @Override
    @JsonIgnore
    public Object getXml() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the DataSource
     */
    public String getDataSource() {
        return this.dataSource;
    }

    /**
     * @param DataSource to set
     */
    public void setDataSource(String source) {
        this.dataSource = source;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataSource == null) ? 0 : dataSource.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KomodoConnectionAttributes other = (KomodoConnectionAttributes)obj;
        if (dataSource == null) {
            if (other.dataSource != null)
                return false;
        } else if (!dataSource.equals(other.dataSource))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "KomodoConnectionAttributes [description=" + this.description + ", ServiceCatalogSource=" + this.dataSource + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
