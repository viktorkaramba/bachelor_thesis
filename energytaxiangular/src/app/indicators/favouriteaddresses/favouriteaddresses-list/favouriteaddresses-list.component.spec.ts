import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouriteaddressesListComponent } from './favouriteaddresses-list.component';

describe('FavouriteaddressesListComponent', () => {
  let component: FavouriteaddressesListComponent;
  let fixture: ComponentFixture<FavouriteaddressesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavouriteaddressesListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouriteaddressesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
