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
package org.komodo.rest.relational;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.junit.Before;
import org.junit.Test;
import org.komodo.core.repository.DescriptorImpl;
import org.komodo.relational.vdb.Translator;
import org.komodo.relational.vdb.Vdb;
import org.komodo.rest.KomodoRestV1Application.V1Constants;
import org.komodo.rest.RestLink;
import org.komodo.rest.RestLink.LinkType;
import org.komodo.rest.RestProperty;
import org.komodo.rest.relational.response.RestVdbTranslator;
import org.komodo.spi.lexicon.vdb.VdbLexicon;
import org.komodo.spi.repository.Descriptor;
import org.komodo.spi.repository.KomodoType;
import org.komodo.spi.repository.PropertyDescriptor;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.spi.repository.Repository.UnitOfWork.State;
import org.komodo.spi.repository.Repository.UnitOfWorkListener;
import org.mockito.Matchers;
import org.mockito.Mockito;

@SuppressWarnings( { "javadoc", "nls" } )
public final class RestVdbTranslatorTest implements V1Constants {

    private static final String DESCRIPTION = "my description";
    private static final String NAME = "MyTranslator";
    private static final String TYPE = "oracle";

    private static final List<RestProperty> PROPS = new ArrayList<>();
    static {
        PROPS.add(new RestProperty("larry", "bird"));
        PROPS.add(new RestProperty("magic", "johnson"));
        PROPS.add(new RestProperty("michael", "jordan"));
    }

    private static final String BASE_URI_PREFIX = "http://localhost:8081/v1";

    private static final URI MY_BASE_URI = UriBuilder.fromUri(BASE_URI_PREFIX).build();

    private static final String VDB_NAME = "vdb1";

    private static final String WKSP_VDB_DATA_PATH = "/workspace/vdbs/" + VDB_NAME;

    private UnitOfWork transaction;

    private Repository repository;

    private RestVdbTranslator translator;

    @Before
    public void init() throws Exception {
        transaction = Mockito.mock(UnitOfWork.class);
        when(transaction.getState()).thenReturn(State.NOT_STARTED);

        UnitOfWork uow = Mockito.mock(UnitOfWork.class);
        when(uow.getState()).thenReturn(State.NOT_STARTED);

        repository = Mockito.mock(Repository.class);
        UnitOfWorkListener listener = Matchers.any();
        when(repository.createTransaction(Matchers.anyString(),
                                                  Matchers.anyString(),
                                                  Matchers.anyBoolean(),
                                                  listener)).thenReturn(uow);

        this.translator = new RestVdbTranslator();
        this.translator.setId(NAME);
        this.translator.setType(TYPE);
        this.translator.setDescription( DESCRIPTION );
        this.translator.setProperties( PROPS );
    }

    @Test
    public void shouldBeEqual() {
        final RestVdbTranslator thatTranslator = new RestVdbTranslator();
        thatTranslator.setId(this.translator.getId());
        thatTranslator.setType(this.translator.getType());
        thatTranslator.setDescription( this.translator.getDescription() );
        thatTranslator.setProperties( this.translator.getProperties() );
        thatTranslator.setLinks( this.translator.getLinks() );

        assertThat( this.translator, is( thatTranslator ) );
    }

    @Test
    public void shouldBeEqualWhenComparingEmptyTranslators() {
        final RestVdbTranslator empty1 = new RestVdbTranslator();
        final RestVdbTranslator empty2 = new RestVdbTranslator();
        assertThat( empty1, is( empty2 ) );
    }

    @Test
    public void shouldConstructEmptyTranslator() {
        final RestVdbTranslator empty = new RestVdbTranslator();
        assertThat( empty.getDescription(), is( nullValue() ) );
        assertThat( empty.getId(), is( nullValue() ) );
        assertThat( empty.getType(), is( nullValue() ) );
        assertThat( empty.getProperties().isEmpty(), is( true ) );
        assertThat( empty.getLinks().size(), is( 0 ) );
    }

    @Test
    public void shouldHaveSameHashCode() {
        final RestVdbTranslator thatTranslator = new RestVdbTranslator();
        thatTranslator.setId(this.translator.getId());
        thatTranslator.setType(this.translator.getType());
        thatTranslator.setDescription( this.translator.getDescription() );
        thatTranslator.setProperties( this.translator.getProperties() );
        thatTranslator.setLinks( this.translator.getLinks() );

        assertThat( this.translator.hashCode(), is( thatTranslator.hashCode() ) );
    }

    @Test
    public void shouldNotBeEqualWhenNameIsDifferent() {
        final RestVdbTranslator thatTranslator = new RestVdbTranslator();
        thatTranslator.setId(this.translator.getId() + "blah");
        thatTranslator.setType(this.translator.getType());
        thatTranslator.setDescription( this.translator.getDescription() );
        thatTranslator.setProperties( this.translator.getProperties() );
        thatTranslator.setLinks( this.translator.getLinks() );

        assertThat( this.translator.getId(), is( not( thatTranslator.getId() ) ) );
        assertThat( this.translator, is( not( thatTranslator ) ) );
    }

    @Test
    public void shouldNotBeEqualWhenPropertiesAreDifferent() {
        final RestVdbTranslator thatTranslator = new RestVdbTranslator();
        thatTranslator.setId(this.translator.getId());
        thatTranslator.setType(this.translator.getType());
        thatTranslator.setDescription( this.translator.getDescription() );
        thatTranslator.setLinks( this.translator.getLinks() );

        List<RestProperty> props = new ArrayList<>();
        props.addAll(this.translator.getProperties() );
        props.add(new RestProperty("blah", "blah" ));
        thatTranslator.setProperties( props );

        assertThat( this.translator, is( not( thatTranslator ) ) );
    }

    @Test
    public void shouldNotBeEqualWhenTypeIsDifferent() {
        final RestVdbTranslator thatTranslator = new RestVdbTranslator();
        thatTranslator.setId(this.translator.getId());
        thatTranslator.setType(this.translator.getType() + "blah");
        thatTranslator.setDescription( this.translator.getDescription() );
        thatTranslator.setProperties( this.translator.getProperties() );
        thatTranslator.setLinks( this.translator.getLinks() );

        assertThat( this.translator.getType(), is( not( thatTranslator.getType() ) ) );
        assertThat( this.translator, is( not( thatTranslator ) ) );
    }

    @Test
    public void shouldSetDescription() {
        final String newDescription = "blah";
        this.translator.setDescription( newDescription );
        assertThat( this.translator.getDescription(), is( newDescription ) );
    }

    @Test
    public void shouldSetName() {
        final String newName = "blah";
        this.translator.setId( newName );
        assertThat( this.translator.getId(), is( newName ) );
    }

    @Test
    public void shouldSetProperties() {
        List<RestProperty> newProperties = new ArrayList<>();
        newProperties.add(new RestProperty("blah", "blah" ));
        this.translator.setProperties( newProperties );
        assertThat( this.translator.getProperties().size(), is( newProperties.size() ) );
    }

    @Test
    public void shouldSetType() {
        final String newType = "blah";
        this.translator.setType( newType );
        assertThat( this.translator.getType(), is( newType ) );
    }

    @Test
    public void shouldHaveCorrectLinks() throws Exception {
        String name = "mysql";
        String dataPath = WKSP_VDB_DATA_PATH + "/vdbTranslators/" + name;
        KomodoType kType = KomodoType.VDB_TRANSLATOR;
        boolean hasChildren = false;

        Descriptor vdbType = new DescriptorImpl(repository, VdbLexicon.Vdb.VIRTUAL_DATABASE);
        Vdb vdb = mock(Vdb.class);
        when(vdb.getName(transaction)).thenReturn(VDB_NAME);
        when(vdb.getPrimaryType(transaction)).thenReturn(vdbType);

        Translator translator = mock(Translator.class);
        when(translator.getName(transaction)).thenReturn(name);
        when(translator.getAbsolutePath()).thenReturn(dataPath);

        //
        // Translator implementation of getParent ignores the translators
        // grouping node and goes straight for the vdb
        //
        when(translator.getParent(transaction)).thenReturn(vdb);
        when(translator.getTypeIdentifier(transaction)).thenReturn(kType);
        when(translator.hasChildren(transaction)).thenReturn(hasChildren);
        when(translator.getRepository()).thenReturn(repository);
        when(translator.getPropertyNames(transaction)).thenReturn(new String[0]);
        when(translator.getPropertyDescriptors(transaction)).thenReturn(new PropertyDescriptor[0]);

        RestVdbTranslator restTranslator = new RestVdbTranslator(MY_BASE_URI, translator, transaction);

        Collection<RestLink> links = restTranslator.getLinks();
        assertEquals(3, links.size());

        int linkCounter = 0;
        for (RestLink link : links) {
            String href = link.getHref().toString();

            if (LinkType.SELF.equals(link.getRel())) {
                linkCounter++;
                assertEquals(BASE_URI_PREFIX + WKSP_VDB_DATA_PATH +
                                         FORWARD_SLASH + TRANSLATORS_SEGMENT +
                                         FORWARD_SLASH + name, href);
            } else if (LinkType.PARENT.equals(link.getRel())) {
                linkCounter++;
                assertEquals(BASE_URI_PREFIX + WKSP_VDB_DATA_PATH, href);
            } else if (LinkType.CHILDREN.equals(link.getRel())) {
                linkCounter++;
            }
        }

        assertEquals(3, linkCounter);
    }
}
