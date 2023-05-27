import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DrivercarrecommendationsReportByIdComponent} from './drivercarrecommendations-report-by-id.component';

describe('DrivercarrecommendationsReportByIdComponent', () => {
  let component: DrivercarrecommendationsReportByIdComponent;
  let fixture: ComponentFixture<DrivercarrecommendationsReportByIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivercarrecommendationsReportByIdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivercarrecommendationsReportByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
