import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NumberOfKilometersListComponent} from './numberofkilometers-list.component';

describe('NumberOfKilometersListComponent', () => {
  let component: NumberOfKilometersListComponent;
  let fixture: ComponentFixture<NumberOfKilometersListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberOfKilometersListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NumberOfKilometersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
