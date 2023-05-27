import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NumberOfKilometersMenuComponent} from './number-of-kilometers-menu.component';

describe('NumberOfKilometersMenuComponent', () => {
  let component: NumberOfKilometersMenuComponent;
  let fixture: ComponentFixture<NumberOfKilometersMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberOfKilometersMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NumberOfKilometersMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
