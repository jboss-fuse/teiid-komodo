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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.komodo.rest.KomodoRestV1Application.V1Constants;
import org.komodo.rest.RestProperty;
import org.komodo.rest.relational.connection.RestConnection;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.spi.repository.Repository.UnitOfWork.State;
import org.komodo.spi.repository.Repository.UnitOfWorkListener;
import org.mockito.Matchers;
import org.mockito.Mockito;

@SuppressWarnings( { "javadoc", "nls" } )
public final class RestConnectionTest implements V1Constants {

    private static final String NAME = "MyConnection";

    private static final String DRIVER_NAME = "MySql";

    private static final String JNDI_NAME = "MyConnectionJndiName";

    private UnitOfWork transaction;

    private Repository repository;

    private RestConnection connection;

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

        this.connection = new RestConnection();
        this.connection.setId(NAME);
        this.connection.setDriverName(DRIVER_NAME);
        this.connection.setJdbc(true);
        this.connection.setJndiName(JNDI_NAME);
    }

    @Test
    public void shouldBeEqual() {
        final RestConnection thatConnection = new RestConnection();
        thatConnection.setId(this.connection.getId());
        thatConnection.setDriverName(this.connection.getDriverName());
        thatConnection.setJndiName(this.connection.getJndiName());
        thatConnection.setJdbc(this.connection.isJdbc());
        thatConnection.setLinks( this.connection.getLinks() );

        assertThat( this.connection, is( thatConnection ) );
    }

    @Test
    public void shouldBeEqualWhenComparingEmptyConnections() {
        final RestConnection empty1 = new RestConnection();
        final RestConnection empty2 = new RestConnection();
        assertThat( empty1, is( empty2 ) );
    }

    @Test
    public void shouldConstructEmptyConnection() {
        final RestConnection empty = new RestConnection();
        assertThat( empty.getId(), is( nullValue() ) );
        assertThat( empty.getDriverName(), is( nullValue() ) );
        assertThat( empty.getJndiName(), is( nullValue() ) );
        assertThat( empty.isJdbc(), is( false ) );
        assertThat( empty.getProperties().isEmpty(), is( true ) );
        assertThat( empty.getLinks().size(), is( 0 ) );
    }

    @Test
    public void shouldHaveSameHashCode() {
        final RestConnection thatConnection = new RestConnection();
        thatConnection.setId(this.connection.getId());
        thatConnection.setDriverName(this.connection.getDriverName());
        thatConnection.setJndiName(this.connection.getJndiName());
        thatConnection.setJdbc(this.connection.isJdbc());
        thatConnection.setProperties( this.connection.getProperties() );
        thatConnection.setLinks( this.connection.getLinks() );

        assertThat( this.connection.hashCode(), is( thatConnection.hashCode() ) );
    }

    @Test
    public void shouldNotBeEqualWhenNameIsDifferent() {
        final RestConnection thatConnection = new RestConnection();
        thatConnection.setId(this.connection.getId() + "blah");
        thatConnection.setDriverName(this.connection.getDriverName());
        thatConnection.setJndiName(this.connection.getJndiName());
        thatConnection.setJdbc(this.connection.isJdbc());
        thatConnection.setProperties( this.connection.getProperties() );
        thatConnection.setLinks( this.connection.getLinks() );

        assertThat( this.connection.getId(), is( not( thatConnection.getId() ) ) );
        assertThat( this.connection, is( not( thatConnection ) ) );
    }

    @Test
    public void shouldNotBeEqualWhenPropertiesAreDifferent() {
        final RestConnection thatConnection = new RestConnection();
        thatConnection.setId(this.connection.getId());
        thatConnection.setDriverName(this.connection.getDriverName());
        thatConnection.setJndiName(this.connection.getJndiName());
        thatConnection.setJdbc(this.connection.isJdbc());
        thatConnection.setLinks( this.connection.getLinks() );

        List<RestProperty> props = new ArrayList<>();
        props.addAll(this.connection.getProperties() );
        props.add(new RestProperty("blah", "blah" ));
        thatConnection.setProperties( props );

        assertThat( this.connection, is( not( thatConnection ) ) );
    }

    @Test
    public void shouldNotBeEqualWhenJndiNameIsDifferent() {
        final RestConnection thatConnection = new RestConnection();
        thatConnection.setId(this.connection.getId());
        thatConnection.setId(this.connection.getId());
        thatConnection.setDriverName(this.connection.getDriverName());
        thatConnection.setJndiName(this.connection.getJndiName() + "blah");
        thatConnection.setJdbc(this.connection.isJdbc());
        thatConnection.setProperties( this.connection.getProperties() );
        thatConnection.setLinks( this.connection.getLinks() );

        assertThat( this.connection.getJndiName(), is( not( thatConnection.getJndiName() ) ) );
        assertThat( this.connection, is( not( thatConnection ) ) );
    }

    //
    // TODO
    //
    // Fix when embedded server supports connections
    //
//    @Test
//    public void shouldHaveCorrectLinks() throws Exception {
//        String name = "mysql";
//        String dataPath = "/teiidCache/" + TEIID_SERVER + FORWARD_SLASH + name;
//        KomodoType kType = KomodoType.CONNECTION;
//        boolean hasChildren = false;
//
//        Descriptor cachedTeiidType = new DescriptorImpl(repository, KomodoLexicon.CachedTeiid.NODE_TYPE);
//        Descriptor folderType = new DescriptorImpl(repository, KomodoLexicon.Folder.NODE_TYPE);
//        CachedTeiid cachedTeiid = mock(CachedTeiid.class);
//        Folder connectionFolder = mock(Folder.class);
//        when(cachedTeiid.getName(transaction)).thenReturn(TEIID_SERVER);
//        when(cachedTeiid.getPrimaryType(transaction)).thenReturn(cachedTeiidType);
//        when(connectionFolder.getName(transaction)).thenReturn(CachedTeiid.CONNECTIONS_FOLDER);
//        when(connectionFolder.getPrimaryType(transaction)).thenReturn(folderType);
//        
//        Connection connection = mock(Connection.class);
//        when(connection.getName(transaction)).thenReturn(name);
//        when(connection.getAbsolutePath()).thenReturn(dataPath);
//        when(connection.getParent(transaction)).thenReturn(connectionFolder);
//        when(connectionFolder.getParent(transaction)).thenReturn(cachedTeiid);
//        when(connection.getTypeIdentifier(transaction)).thenReturn(kType);
//        when(connection.hasChildren(transaction)).thenReturn(hasChildren);
//        when(connection.getRepository()).thenReturn(repository);
//        when(connection.getPropertyNames(transaction)).thenReturn(new String[0]);
//        when(connection.getPropertyDescriptors(transaction)).thenReturn(new PropertyDescriptor[0]);
//
//        RestConnection restConnection = new RestConnection(MY_BASE_URI, connection, transaction);
//
//        Collection<RestLink> links = restConnection.getLinks();
//        assertEquals(3, links.size());
//
//        int linkCounter = 0;
//        for (RestLink link : links) {
//            String href = link.getHref().toString();
//
//            if (LinkType.SELF.equals(link.getRel())) {
//                linkCounter++;
//                assertEquals(BASE_URI_PREFIX +
//                                         FORWARD_SLASH + METADATA_SEGMENT +
//                                         FORWARD_SLASH + CachedTeiid.CONNECTIONS_FOLDER +
//                                         FORWARD_SLASH + name, href);
//            } else if (LinkType.PARENT.equals(link.getRel())) {
//                linkCounter++;
//                assertEquals(BASE_URI_PREFIX +
//                                         FORWARD_SLASH + METADATA_SEGMENT +
//                                         FORWARD_SLASH + CachedTeiid.CONNECTIONS_FOLDER, href);
//            } else if (LinkType.CHILDREN.equals(link.getRel())) {
//                linkCounter++;
//            }
//        }
//
//        assertEquals(3, linkCounter);
//    }
}
