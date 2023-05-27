import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DriverRatingsReportComponent} from './driver-ratings-report.component';

describe('DriverRatingsReportComponent', () => {
  let component: DriverRatingsReportComponent;
  let fixture: ComponentFixture<DriverRatingsReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverRatingsReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverRatingsReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
