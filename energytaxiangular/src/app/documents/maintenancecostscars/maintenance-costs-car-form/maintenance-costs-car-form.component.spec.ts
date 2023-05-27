import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MaintenanceCostsCarFormComponent} from './maintenance-costs-car-form.component';

describe('MaintenanceCostsCarFormComponent', () => {
  let component: MaintenanceCostsCarFormComponent;
  let fixture: ComponentFixture<MaintenanceCostsCarFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MaintenanceCostsCarFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MaintenanceCostsCarFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
