import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouritedriversListComponent } from './favouritedrivers-list.component';

describe('FavouritedriversListComponent', () => {
  let component: FavouritedriversListComponent;
  let fixture: ComponentFixture<FavouritedriversListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavouritedriversListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouritedriversListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
