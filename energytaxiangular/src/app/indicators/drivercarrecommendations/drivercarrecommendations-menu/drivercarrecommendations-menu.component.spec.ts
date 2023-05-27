import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DrivercarrecommendationsMenuComponent} from './drivercarrecommendations-menu.component';

describe('DrivercarrecommendationsMenuComponent', () => {
  let component: DrivercarrecommendationsMenuComponent;
  let fixture: ComponentFixture<DrivercarrecommendationsMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivercarrecommendationsMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivercarrecommendationsMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
