import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NumberOfKilometersReportByIdComponent} from './number-of-kilometers-report-by-id.component';

describe('NumberOfKilometersReportByIdComponent', () => {
  let component: NumberOfKilometersReportByIdComponent;
  let fixture: ComponentFixture<NumberOfKilometersReportByIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberOfKilometersReportByIdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NumberOfKilometersReportByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
