import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PricePerKilometerByTariffListComponent} from './priceperkilometerbytariff-list.component';

describe('PricePerKilometerByTariffListComponent', () => {
  let component: PricePerKilometerByTariffListComponent;
  let fixture: ComponentFixture<PricePerKilometerByTariffListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PricePerKilometerByTariffListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricePerKilometerByTariffListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
