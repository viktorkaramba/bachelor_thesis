import {ComponentFixture, TestBed} from '@angular/core/testing';

import {IncomeCarsReportComponent} from './income-cars-report.component';

describe('IncomeCarsReportComponent', () => {
  let component: IncomeCarsReportComponent;
  let fixture: ComponentFixture<IncomeCarsReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IncomeCarsReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncomeCarsReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
