import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NumberOfKilometersReportComponent} from './number-of-kilometers-report.component';

describe('NumberOfKilometersReportComponent', () => {
  let component: NumberOfKilometersReportComponent;
  let fixture: ComponentFixture<NumberOfKilometersReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberOfKilometersReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NumberOfKilometersReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
