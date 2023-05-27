import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MaintenanceCostsCarsListComponent} from './maintenancecostscars-list.component';

describe('MaintenanceCostsCarsListComponent', () => {
  let component: MaintenanceCostsCarsListComponent;
  let fixture: ComponentFixture<MaintenanceCostsCarsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MaintenanceCostsCarsListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MaintenanceCostsCarsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
