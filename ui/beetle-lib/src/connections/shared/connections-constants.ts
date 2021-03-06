/**
 * @license
 * Copyright 2017 JBoss Inc
 *
 * Licensed under the Apache License, /
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { VerticalNavigationItem } from "patternfly-ng/navigation/vertical-navigation/vertical-navigation-item";

export class ConnectionsConstants {

  public static readonly connectionsRootRoute = "connections";
  public static readonly connectionsRootPath = "/" + ConnectionsConstants.connectionsRootRoute;

  public static readonly connectionRootRoute = "connection";
  public static readonly connectionRootPath = "/" + ConnectionsConstants.connectionRootRoute;

  public static readonly addConnectionRoute = ConnectionsConstants.connectionsRootRoute + "/add-connection";
  public static readonly addConnectionPath = ConnectionsConstants.connectionsRootPath + "/add-connection";

  public static readonly syndesisSourcesRootRoute = "syndesisSources";
  public static readonly syndesisSourcesRootPath = "/" + ConnectionsConstants.syndesisSourcesRootRoute;

  public static readonly syndesisSourceStatusesRootRoute = "syndesisSourceStatuses";
  public static readonly syndesisSourceStatusesRootPath = "/" + ConnectionsConstants.syndesisSourceStatusesRootRoute;

  public static readonly connectionType_postgresql = "postgresql";
  public static readonly connectionType_mysql = "mysql";
  public static readonly connectionType_mongodb = "mongodb";
  public static readonly connectionType_mariadb = "mariadb";
  public static readonly connectionType_salesforce = "salesforce";

  public static readonly connectionTypeDescription_postgresql = "PostgreSQL database";
  public static readonly connectionTypeDescription_mysql = "MySQL database";
  public static readonly connectionTypeDescription_mongodb = "MongoDB database";
  public static readonly connectionTypeDescription_mariadb = "MariaDB database";
  public static readonly connectionTypeDescription_salesforce = "SalesForce";

  public static readonly schemaNodeType_connection = "connection";

  public static readonly includeConnectionParameter = "include-connection";
  public static readonly includeSchemaStatusParameter = "include-schema-status";

  public static driverNamePropertyLabel = "Driver Name";
  public static jndiNamePropertyLabel = "JNDI Name";
  public static serviceCatalogSourceNameLabel = "Service Catalog Source";

  public static readonly connectionsNavItem: VerticalNavigationItem = {
    title: "Connections",
    iconStyleClass: "fa fa-fw fa-plug",
    url: ConnectionsConstants.connectionsRootPath
  };

}
