import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CarMenuComponent} from './car-menu.component';

describe('CarMenuComponent', () => {
  let component: CarMenuComponent;
  let fixture: ComponentFixture<CarMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CarMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
