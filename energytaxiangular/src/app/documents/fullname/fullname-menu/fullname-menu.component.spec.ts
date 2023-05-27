import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FullNameMenuComponent} from './fullname-menu.component';

describe('FullNameMenuComponent', () => {
  let component: FullNameMenuComponent;
  let fixture: ComponentFixture<FullNameMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FullNameMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FullNameMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
