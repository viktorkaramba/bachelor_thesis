import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkLoadDriversReportComponent} from './work-load-drivers-report.component';

describe('WorkLoadDriversReportComponent', () => {
  let component: WorkLoadDriversReportComponent;
  let fixture: ComponentFixture<WorkLoadDriversReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkLoadDriversReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkLoadDriversReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
