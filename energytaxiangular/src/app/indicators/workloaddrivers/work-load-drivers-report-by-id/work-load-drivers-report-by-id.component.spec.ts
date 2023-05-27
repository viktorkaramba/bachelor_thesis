import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkLoadDriversReportByIdComponent} from './work-load-drivers-report-by-id.component';

describe('WorkLoadDriversReportByIdComponent', () => {
  let component: WorkLoadDriversReportByIdComponent;
  let fixture: ComponentFixture<WorkLoadDriversReportByIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkLoadDriversReportByIdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkLoadDriversReportByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
