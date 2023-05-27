import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouritedriversMenuComponent } from './favouritedrivers-menu.component';

describe('FavouritedriversMenuComponent', () => {
  let component: FavouritedriversMenuComponent;
  let fixture: ComponentFixture<FavouritedriversMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavouritedriversMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouritedriversMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
