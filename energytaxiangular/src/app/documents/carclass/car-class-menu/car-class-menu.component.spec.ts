import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CarClassMenuComponent} from './car-class-menu.component';

describe('CarClassMenuComponent', () => {
  let component: CarClassMenuComponent;
  let fixture: ComponentFixture<CarClassMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CarClassMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarClassMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
