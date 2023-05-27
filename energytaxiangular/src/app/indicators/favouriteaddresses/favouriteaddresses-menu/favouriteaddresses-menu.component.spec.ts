import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouriteaddressesMenuComponent } from './favouriteaddresses-menu.component';

describe('FavouriteaddressesMenuComponent', () => {
  let component: FavouriteaddressesMenuComponent;
  let fixture: ComponentFixture<FavouriteaddressesMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavouriteaddressesMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouriteaddressesMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
