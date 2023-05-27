import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DriverRatingListComponent} from './driverrating-list.component';

describe('DriverRatingListComponent', () => {
  let component: DriverRatingListComponent;
  let fixture: ComponentFixture<DriverRatingListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverRatingListComponent ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(DriverRatingListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

