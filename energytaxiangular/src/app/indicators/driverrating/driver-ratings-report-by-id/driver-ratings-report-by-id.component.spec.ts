import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DriverRatingsReportByIdComponent} from './driver-ratings-report-by-id.component';

describe('DriverRatingsReportByIdComponent', () => {
  let component: DriverRatingsReportByIdComponent;
  let fixture: ComponentFixture<DriverRatingsReportByIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverRatingsReportByIdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverRatingsReportByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
