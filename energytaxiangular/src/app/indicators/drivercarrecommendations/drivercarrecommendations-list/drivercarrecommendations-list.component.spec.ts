import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DriverCarRecommendationsListComponent} from './drivercarrecommendations-list.component';

describe('DriverCarRecommendationsListComponent', () => {
  let component: DriverCarRecommendationsListComponent;
  let fixture: ComponentFixture<DriverCarRecommendationsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverCarRecommendationsListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverCarRecommendationsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
