import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { FormsModule } from "@angular/forms";
import { RouterTestingModule } from "@angular/router/testing";
import { AppSettingsService } from "../../core/app-settings.service";
import { CoreModule } from "../../core/core.module";
import { MockAppSettingsService } from "../../core/mock-app-settings.service";
import { Dataservice } from "../shared/dataservice.model";
import { DataserviceService } from "../shared/dataservice.service";
import { MockDataserviceService } from "../shared/mock-dataservice.service";
import { MockVdbService } from "../shared/mock-vdb.service";
import { NotifierService } from "../shared/notifier.service";
import { VdbService } from "../shared/vdb.service";
import { SqlControlComponent } from "../sql-control/sql-control.component";
import { TestDataserviceComponent } from "./test-dataservice.component";
import { CodemirrorModule } from "ng2-codemirror";
import {
  AboutModalModule,
  ActionModule,
  CardModule,
  EmptyStateModule,
  FilterModule,
  ListModule,
  SortModule,
  TableModule,
  WizardModule
} from "patternfly-ng";
import { SelectionService } from "../../core/selection.service";

describe("TestDataserviceComponent", () => {
  let component: TestDataserviceComponent;
  let fixture: ComponentFixture<TestDataserviceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        CoreModule,
        FormsModule,
        RouterTestingModule,
        CodemirrorModule,
        AboutModalModule,
        ActionModule,
        CardModule,
        EmptyStateModule,
        FilterModule,
        ListModule,
        SortModule,
        TableModule,
        WizardModule
      ],
      declarations: [ SqlControlComponent, TestDataserviceComponent ],
      providers: [
        NotifierService, SelectionService,
        { provide: AppSettingsService, useClass: MockAppSettingsService },
        { provide: DataserviceService, useClass: MockDataserviceService },
        { provide: VdbService, useClass: MockVdbService }
      ]
    })
      .compileComponents().then(() => {
      // nothing to do
    });
  }));

  beforeEach(() => {
    // select a dataservice before constructing component
    const service = TestBed.get( DataserviceService );
    let dataservices: Dataservice[];
    service.getAllDataservices().subscribe( ( values ) => { dataservices = values; } );

    const selService = TestBed.get( SelectionService );
    // noinspection JSUnusedAssignment
    selService.setSelectedVirtualization( dataservices[ 1 ] );

    fixture = TestBed.createComponent(TestDataserviceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should be created", () => {
    console.log("========== [TestDataserviceComponent] should be created");
    expect(component).toBeTruthy();
  });
});
