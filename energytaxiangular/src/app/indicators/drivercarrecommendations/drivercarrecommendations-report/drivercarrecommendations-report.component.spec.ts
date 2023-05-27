import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DrivercarrecommendationsReportComponent} from './drivercarrecommendations-report.component';

describe('DrivercarrecommendationsReportComponent', () => {
  let component: DrivercarrecommendationsReportComponent;
  let fixture: ComponentFixture<DrivercarrecommendationsReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivercarrecommendationsReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivercarrecommendationsReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
