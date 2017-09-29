/*
 * Originally copied from ModeShape (http://www.modeshape.org).
 *
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.komodo.modeshape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import org.komodo.modeshape.teiid.TeiidSqlNodeVisitor;
import org.komodo.spi.lexicon.TeiidSqlLexicon;
import org.komodo.spi.lexicon.TeiidSqlLexicon.AliasSymbol;
import org.komodo.spi.lexicon.TeiidSqlLexicon.Constant;
import org.komodo.spi.lexicon.TeiidSqlLexicon.ElementSymbol;
import org.komodo.spi.lexicon.TeiidSqlLexicon.ExpressionSymbol;
import org.komodo.spi.lexicon.TeiidSqlLexicon.GroupSymbol;
import org.komodo.spi.lexicon.TeiidSqlLexicon.JoinPredicate;
import org.komodo.spi.lexicon.TeiidSqlLexicon.JoinType;
import org.komodo.spi.lexicon.TeiidSqlLexicon.Symbol;
import org.komodo.spi.lexicon.TeiidSqlLexicon.UnaryFromClause;
import org.komodo.spi.query.JoinTypeTypes;
import org.komodo.spi.runtime.version.DefaultMetadataVersion;
import org.komodo.test.utils.AbstractSequencerTest;
/**
 * Class which serves as base for various sequencer unit tests. In addition to this, it uses the sequencing events fired by
 * ModeShape's {@link javax.jcr.observation.ObservationManager} to perform various assertions and therefore, acts as a test for
 * those as well.
 */
@SuppressWarnings( {"javadoc", "nls"} )
public abstract class AbstractTSqlSequencerTest extends AbstractSequencerTest {

    @Override
    protected String getTestConfigurationPath() {
        return "test-repository-config.json";
    }

    protected void verifyVersionType(Node node) throws RepositoryException {
        Property property = node.getProperty(TeiidSqlLexicon.LanguageObject.METADATA_VERSION_PROP_NAME);
        Value value = property.getValue();
        DefaultMetadataVersion version = new DefaultMetadataVersion(value.getString());
        assertEquals(getMetadataVersion(), version);
    }
    
    @Override
    protected void verifyBaseProperties( Node node, String primaryType, String mixinType) throws RepositoryException {
        super.verifyBaseProperties(node, primaryType, mixinType);

        if (mixinType != null && mixinType.startsWith(TeiidSqlLexicon.Namespace.PREFIX))
            verifyVersionType(node);
    }
    
    protected String deriveProcPrefix(boolean useNewLine) {
        StringBuilder builder = new StringBuilder();

        builder.append("BEGIN");

        if (!useNewLine)
            builder.append(SPACE);
        
        return builder.toString();
    }

    protected void verifyJoin(Node joinPredicate, JoinTypeTypes joinType) throws Exception {
        Node joinNode = verify(joinPredicate, JoinPredicate.JOIN_TYPE_REF_NAME, JoinType.ID);
        verifyProperty(joinNode, TeiidSqlLexicon.JoinType.KIND_PROP_NAME, joinType.name());
    }

    protected void verifyUnaryFromClauseGroup(Node jpNode, String refName, int refIndex, String... gSymbolProps) throws Exception {
        Node refNode = verify(jpNode, refName, refIndex, UnaryFromClause.ID);
        Node groupNode = verify(refNode, UnaryFromClause.GROUP_REF_NAME, GroupSymbol.ID);

        String name = gSymbolProps[0];
        verifyProperty(groupNode, Symbol.NAME_PROP_NAME, name);

        if (gSymbolProps.length > 1) {
            String definition = gSymbolProps[1];
            verifyProperty(groupNode, GroupSymbol.DEFINITION_PROP_NAME, definition);
        }
    }

    protected void verifyUnaryFromClauseGroup(Node jpNode, String refName, String... gSymbolProps) throws Exception {
        verifyUnaryFromClauseGroup(jpNode, refName, -1, gSymbolProps);
    }

    protected void verifyConstant(Node parentNode, String refName, int refIndex, String literal) throws Exception {
        Node constantNode = verify(parentNode, refName, refIndex, Constant.ID);
        verifyProperty(constantNode, Constant.VALUE_PROP_NAME, literal);
    }

    protected void verifyConstant(Node parentNode, String refName, String literal) throws Exception {
        verifyConstant(parentNode, refName, -1, literal);
    }

    protected void verifyConstant(Node parentNode, String refName, int refIndex, int literal) throws Exception {
        Node constantNode = verify(parentNode, refName, refIndex, Constant.ID);
        verifyProperty(constantNode, Constant.VALUE_PROP_NAME, literal);
    }

    protected void verifyConstant(Node parentNode, String refName, int literal) throws Exception {
        verifyConstant(parentNode, refName, -1, literal);
    }

    protected void verifyElementSymbol(Node parentNode, String refName, int refIndex, String elementSymbolName) throws Exception {
        Node elementSymbolNode = verify(parentNode, refName, refIndex, ElementSymbol.ID);
        verifyProperty(elementSymbolNode, Symbol.NAME_PROP_NAME, elementSymbolName);
    }

    protected void verifyElementSymbol(Node parentNode, String refName, String elementSymbolName) throws Exception {
        verifyElementSymbol(parentNode, refName, -1, elementSymbolName);
    }

    protected Node verifyAliasSymbol(Node parentNode, String refName, int refIndex, String aliasName, String symbolId) throws Exception {
        Node aliasNode = verify(parentNode, refName, refIndex, AliasSymbol.ID);
        verifyProperty(aliasNode, Symbol.NAME_PROP_NAME, aliasName);
        return verify(aliasNode, AliasSymbol.SYMBOL_REF_NAME, symbolId);
    }

    protected Node verifyAliasSymbol(Node parentNode, String refName, String aliasName, String symbolId) throws Exception {
        return verifyAliasSymbol(parentNode, refName, -1, aliasName, symbolId);
    }

    protected void verifyAliasSymbolWithElementSymbol(Node parentNode, String refName, int refIndex, String aliasName, String elementSymbolName) throws Exception {
        Node aliasNode = verify(parentNode, refName, refIndex, AliasSymbol.ID);
        verifyProperty(aliasNode, Symbol.NAME_PROP_NAME, aliasName);
        Node elementSymbolNode = verify(aliasNode, AliasSymbol.SYMBOL_REF_NAME, ElementSymbol.ID);
        verifyProperty(elementSymbolNode, Symbol.NAME_PROP_NAME, elementSymbolName);
    }

    protected Node verifyExpressionSymbol(Node parentNode, String refName, int refIndex, String expSymbolExpressionId) throws Exception {
        Node expSymbolNode = verify(parentNode, refName, refIndex, ExpressionSymbol.ID);

        Property property = expSymbolNode.getProperty(Symbol.NAME_PROP_NAME);
        Value value = property.isMultiple() ? property.getValues()[0] : property.getValue();
        assertTrue(value.toString().startsWith("expr"));

        return verify(expSymbolNode, ExpressionSymbol.EXPRESSION_REF_NAME, expSymbolExpressionId);
    }

    protected Node verifyExpressionSymbol(Node parentNode, String refName, String expSymbolExpressionId) throws Exception {
        return verifyExpressionSymbol(parentNode, refName, -1, expSymbolExpressionId);
    }

    protected void verifySql(String expectedSql, Node topNode) throws Exception {
        TeiidSqlNodeVisitor visitor = new TeiidSqlNodeVisitor(getMetadataVersion(), getDataTypeService());
        String actualSql = visitor.getTeiidSql(topNode);
        assertEquals(expectedSql, actualSql);
    }

}
