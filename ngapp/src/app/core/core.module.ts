/**
 * @license
 * Copyright 2017 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { CommonModule } from "@angular/common";
import { NgModule} from "@angular/core";
import {HttpModule} from "@angular/http";
import {RouterModule} from "@angular/router";
import { BreadcrumbComponent } from "@core/breadcrumbs/breadcrumb/breadcrumb.component";
import { BreadcrumbsComponent } from "@core/breadcrumbs/breadcrumbs.component";
import { LoggerService } from "@core/logger.service";
import { NavHeaderComponent } from "@core/nav-header/nav-header.component";
import { VerticalNavComponent } from "@core/vertical-nav/vertical-nav.component";

@NgModule({
  imports: [
    CommonModule,
    HttpModule,
    RouterModule
  ],
  declarations: [
    BreadcrumbComponent,
    BreadcrumbsComponent,
    NavHeaderComponent,
    VerticalNavComponent
  ],
  exports: [
    BreadcrumbComponent,
    BreadcrumbsComponent,
    NavHeaderComponent,
    VerticalNavComponent
  ],
  providers: [
    LoggerService
  ]
})
export class CoreModule { }
