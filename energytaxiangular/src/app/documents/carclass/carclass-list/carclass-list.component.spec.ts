import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CarClassListComponent} from './carclass-list.component';

describe('CarClassListComponent', () => {
  let component: CarClassListComponent;
  let fixture: ComponentFixture<CarClassListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CarClassListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarClassListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
