import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PricePerKilometerByTariffMenuComponent} from './price-per-kilometer-by-tariff-menu.component';

describe('PricePerKilometerByTariffMenuComponent', () => {
  let component: PricePerKilometerByTariffMenuComponent;
  let fixture: ComponentFixture<PricePerKilometerByTariffMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PricePerKilometerByTariffMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricePerKilometerByTariffMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
