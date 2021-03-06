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
package org.komodo.rest;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.komodo.metadata.TeiidConnectionProvider;
import org.komodo.spi.metadata.MetadataInstance.ConnectivityType;
import org.komodo.spi.outcome.Outcome;
import org.komodo.spi.outcome.OutcomeFactory;
import org.teiid.adminapi.Admin;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.jboss.AdminFactory;
import org.teiid.jdbc.TeiidDriver;

public class TeiidSwarmConnectionProvider implements TeiidConnectionProvider {
	private static final String PING_VDB = 
            "<vdb name=\"ping\" version=\"1\">" +
                "<model visible=\"true\" name=\"Foo\" type=\"PHYSICAL\" path=\"/dummy/Foo\">" +
                    "<source name=\"s\" translator-name=\"loopback\"/>" +
                    "<metadata type=\"DDL\">" +
                        "<![CDATA[CREATE FOREIGN TABLE G1 (e1 string, e2 integer);]]>" +
                    "</metadata>" +
                "</model>" +
            "</vdb>";	
	
	private Admin admin;
	
	public TeiidSwarmConnectionProvider() throws AdminException {
		connect();
	}

    protected synchronized void connect() throws AdminException {
        if (admin != null)
            return;

        this.admin = AdminFactory.getInstance().createAdmin("localhost", 9990, "admin", "admin".toCharArray());
    }

    protected synchronized void disconnect() throws AdminException {
        if (this.admin != null) {
            this.admin.close();
            this.admin = null;
        }
    }
    
	@Override
	public Admin getAdmin() throws AdminException {
		return admin;
	}

	@Override
	public Connection getConnection(String vdb, String version) throws SQLException {
		Properties props = new Properties();
		//TODO: when security working the user name needs to be passed in we need to work delegation model for security
		return new TeiidDriver().connect("jdbc:teiid:"+vdb+"."+version, props);
	}

	@Override
	public Outcome ping(ConnectivityType connectivityType) {
		try {
			if (connectivityType == ConnectivityType.ADMIN) {
				admin.getSessions();
			} else {
				new TeiidDriver().connect("jdbc:teiid:ping", new Properties());
			}
		} catch (AdminException | SQLException e) {
			return OutcomeFactory.getInstance().createError(e.getLocalizedMessage(), e);
		}
		return OutcomeFactory.getInstance().createOK();
	}
	
	@Override
	public void reconnect() throws Exception {
	    disconnect();

	    //
	    // Give disconnect and teiid connection time to clear up
	    //
	    Thread.sleep(500);

	    // Refresh is implied in the getting of the admin object since it will
	    // automatically load and refresh.
	    connect();
	}

	@Override
	public void onStart() {
		try {
			admin.deploy("ping-vdb.xml", new ByteArrayInputStream(PING_VDB.getBytes()));
		} catch (AdminException e) {
			
		}
	}

	@Override
	public void onShutdown() {
		// TODO Auto-generated method stub

	}
}
