import {ComponentFixture, TestBed} from '@angular/core/testing';

import {IncomeCarsReportByIdComponent} from './income-cars-report-by-id.component';

describe('IncomeCarsReportByIdComponent', () => {
  let component: IncomeCarsReportByIdComponent;
  let fixture: ComponentFixture<IncomeCarsReportByIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IncomeCarsReportByIdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncomeCarsReportByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
