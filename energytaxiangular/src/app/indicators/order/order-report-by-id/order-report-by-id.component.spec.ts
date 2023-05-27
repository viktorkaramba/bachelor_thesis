import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderReportByIdComponent } from './order-report-by-id.component';

describe('OrderReportByIdComponent', () => {
  let component: OrderReportByIdComponent;
  let fixture: ComponentFixture<OrderReportByIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderReportByIdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderReportByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
