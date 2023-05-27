import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CarClassFormComponent} from './car-class-form.component';

describe('CarClassFormComponent', () => {
  let component: CarClassFormComponent;
  let fixture: ComponentFixture<CarClassFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CarClassFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarClassFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
