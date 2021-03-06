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

import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { LoggerService } from "../../../../core/logger.service";
import { ViewEditorI18n } from "../view-editor-i18n";
import { ViewEditorService } from "../view-editor.service";
import { ViewEditorEvent } from "../event/view-editor-event";
import { Subscription } from "rxjs/Subscription";
import 'dragula/dist/dragula.css';

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'btl-view-property-editors',
  templateUrl: './view-property-editors.component.html',
  styleUrls: ['./view-property-editors.component.css']
})
export class ViewPropertyEditorsComponent implements OnInit, OnDestroy {

  public readonly columnsTabName = ViewEditorI18n.columnsTabName;
  public readonly propertiesTabName = ViewEditorI18n.propertiesTabName;

  private columnEditorIsEnabled = true;
  private readonly editorService: ViewEditorService;
  private readonly logger: LoggerService;
  private subscription: Subscription;
  private viewEditorIsEnabled = true;

  private readonly propertyIndex = 0;
  private readonly columnIndex = 1;

  /**
   * The tabs component configuration.
   */
  public tabs = [
    {
      "active": true // properties
    },
    {
      "active": false // columns
    },
  ];

  constructor( editorService: ViewEditorService,
               logger: LoggerService ) {
    this.editorService = editorService;
    this.logger = logger;
  }

  /**
   * @param {ViewEditorEvent} event the event being processed
   */
  public handleEditorEvent( event: ViewEditorEvent ): void {
    this.logger.debug( "ViewPropertyEditorsComponent received event: " + event.toString() );

    if ( event.typeIsCanvasSelectionChanged() ) {
      // if single item is selected, show the properties tab
      if ( event.args.length === 1 ) {
        this.tabs[ this.propertyIndex ].active = true;
        this.tabs[ this.columnIndex ].active = false;
      }
    }
  }

  /**
   * Cleanup code when destroying the view editor header.
   */
  public ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  /**
   * Initialization code run after construction.
   */
  public ngOnInit(): void {
    this.subscription = this.editorService.editorEvent.subscribe( ( event ) => this.handleEditorEvent( event ) );
  }

  /**
   * @param tab the editor tab being removed
   */
  public removeTab( tab: any ): void {
    this.tabs.splice( this.tabs.indexOf( tab ), 1);
  }

  /**
   * Callback for when a tab is clicked.
   *
   * @param tab the tab being select or deselected
   * @param selected `true` is selected
   */
  public tabSelected( tab, selected ): void {
    tab.active = selected;
  }

  public get numberSelectedItems(): number {
    const selections = this.editorService.getSelection();
    if (selections) {
      return selections.length;
    }
    return 0;
  }

}
