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
package org.komodo.relational.model.internal;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.komodo.relational.RelationalModelTest;
import org.komodo.relational.internal.RelationalModelFactory;
import org.komodo.relational.internal.RelationalObjectImpl;
import org.komodo.relational.internal.RelationalObjectImpl.Filter;
import org.komodo.relational.model.Table;
import org.komodo.relational.model.TableConstraint;
import org.komodo.relational.vdb.internal.VdbImpl;
import org.komodo.spi.KException;
import org.komodo.spi.repository.KomodoType;
import org.teiid.modeshape.sequencer.ddl.TeiidDdlLexicon;
import org.teiid.modeshape.sequencer.ddl.TeiidDdlLexicon.Constraint;

@SuppressWarnings( { "javadoc", "nls" } )
public final class ForeignKeyImplTest extends RelationalModelTest {

    private static final String NAME = "foreignKey";

    private ForeignKeyImpl foreignKey;
    private TableImpl parentTable;
    private TableImpl refTable;

    @Before
    public void init() throws Exception {
        final VdbImpl vdb = createVdb();
        final ModelImpl model = createModel();
        this.parentTable = model.addTable( "parentTable" );

        final ModelImpl refModel = vdb.addModel( "refModel" );
        this.refTable = refModel.addTable( "refTable" );

        this.foreignKey = this.parentTable.addForeignKey( NAME, this.refTable );
        commit();
    }

    @Test
    public void shouldAddReferencesColumns() throws Exception {
        final ColumnImpl columnA = RelationalModelFactory.createColumn( getTransaction(), _repo, this.refTable, "columnRefA" );
        this.foreignKey.addReferencesColumn( columnA );

        final ColumnImpl columnB = RelationalModelFactory.createColumn( getTransaction(), _repo, this.refTable, "columnRefB" );
        this.foreignKey.addReferencesColumn( columnB );

        commit(); // must commit so that query used in getReferencesColumns will work

        assertThat( this.foreignKey.getReferencesColumns( ).length, is( 2 ) );
        assertThat( Arrays.asList( this.foreignKey.getReferencesColumns( ) ), hasItems( columnA, columnB ) );
    }

    @Test
    public void shouldBeChildRestricted() {
        assertThat( this.foreignKey.isChildRestricted(), is( true ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingNullColumn() throws Exception {
        this.foreignKey.addColumn( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingNullReferencesColumn() throws Exception {
        this.foreignKey.addReferencesColumn( null );
    }

    @Test
    public void shouldFailConstructionIfNotForeignKey() {
        if ( RelationalObjectImpl.VALIDATE_INITIAL_STATE ) {
            try {
                new ForeignKeyImpl( getTransaction(), _repo, this.parentTable.getAbsolutePath() );
                fail();
            } catch ( final KException e ) {
                // expected
            }
        }
    }

    @Test
    public void shouldHaveCorrectConstraintType() throws Exception {
        assertThat( this.foreignKey.getConstraintType(), is( TableConstraint.ConstraintType.FOREIGN_KEY ) );
        assertThat( this.foreignKey.getRawProperty( getTransaction(), TeiidDdlLexicon.Constraint.TYPE ).getStringValue( getTransaction() ),
                    is( TableConstraint.ConstraintType.FOREIGN_KEY.toValue() ) );
    }

    @Test
    public void shouldHaveCorrectDescriptor() throws Exception {
        assertThat( this.foreignKey.hasDescriptor( Constraint.FOREIGN_KEY_CONSTRAINT ), is( true ) );
    }

    @Test
    public void shouldHaveCorrectTypeIdentifier() throws Exception {
        assertThat(this.foreignKey.getTypeIdentifier( ), is(KomodoType.FOREIGN_KEY));
    }

    @Test
    public void shouldHaveMoreRawProperties() throws Exception {
        final String[] filteredProps = this.foreignKey.getPropertyNames( );
        final String[] rawProps = this.foreignKey.getRawPropertyNames( getTransaction() );
        assertThat( ( rawProps.length > filteredProps.length ), is( true ) );
    }

    @Test
    public void shouldHaveParentTableAfterConstruction() throws Exception {
        assertThat( this.foreignKey.getParent( ), is( instanceOf( Table.class ) ) );
        assertThat( this.foreignKey.getTable( ), is( this.parentTable ) );
    }

    @Test
    public void shouldHaveReferencesTableAfterConstruction() throws Exception {
        assertThat( this.foreignKey.getReferencesTable( ), is( this.refTable ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotAllowChildren() throws Exception {
        this.foreignKey.addChild( getTransaction(), "blah", null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullTableReference() throws Exception {
        this.foreignKey.setReferencesTable( null );
    }

    @Test
    public void shouldNotContainFilteredProperties() throws Exception {
        final String[] filteredProps = this.foreignKey.getPropertyNames( );
        final Filter[] filters = ((ForeignKeyImpl)this.foreignKey).getFilters();

        for ( final String name : filteredProps ) {
            for ( final Filter filter : filters ) {
                assertThat( filter.rejectProperty( name ), is( false ) );
            }
        }
    }

    @Test
    public void shouldNotHaveColumnReferencesAfterConstruction() throws Exception {
        assertThat( this.foreignKey.getReferencesColumns( ), is( notNullValue() ) );
        assertThat( this.foreignKey.getReferencesColumns( ).length, is( 0 ) );
    }

    @Test
    public void shouldNotHaveColumnsAfterConstruction() throws Exception {
        assertThat( this.foreignKey.getColumns( ), is( notNullValue() ) );
        assertThat( this.foreignKey.getColumns( ).length, is( 0 ) );
    }

    @Test
    public void shouldRemoveReferencesColumn() throws Exception {
        final ColumnImpl columnA = RelationalModelFactory.createColumn( getTransaction(), _repo, this.refTable, "removeRefColumnA" );
        this.foreignKey.addReferencesColumn( columnA );
        commit(); // must commit so that query used in next method will work

        this.foreignKey.removeReferencesColumn( columnA );
        assertThat( this.foreignKey.getReferencesColumns( ).length, is( 0 ) );
    }

    @Test
    public void shouldRename() throws Exception {
        final String newName = "blah";
        this.foreignKey.rename( getTransaction(), newName );
        assertThat( this.foreignKey.getName( ), is( newName ) );
    }

    @Test
    public void shouldSetTableReference() throws Exception {
        final TableImpl newTable = RelationalModelFactory.createTable( getTransaction(), _repo, mock( ModelImpl.class ), "newTable" );
        this.foreignKey.setReferencesTable( newTable );
        commit(); // must commit so that query used in next method will work

        assertThat( this.foreignKey.getReferencesTable( ), is( newTable ) );
    }

}