import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MaintenanceCostsCarsMenuComponent} from './maintenance-costs-cars-menu.component';

describe('MaintenanceCostsCarsMenuComponent', () => {
  let component: MaintenanceCostsCarsMenuComponent;
  let fixture: ComponentFixture<MaintenanceCostsCarsMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MaintenanceCostsCarsMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MaintenanceCostsCarsMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
