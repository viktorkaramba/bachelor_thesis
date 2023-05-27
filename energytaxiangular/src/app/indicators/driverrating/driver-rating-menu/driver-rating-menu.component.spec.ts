import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DriverRatingMenuComponent} from './driver-rating-menu.component';

describe('DriverRatingMenuComponent', () => {
  let component: DriverRatingMenuComponent;
  let fixture: ComponentFixture<DriverRatingMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverRatingMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverRatingMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
