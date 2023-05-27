import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FullNameListComponent} from './fullname-list.component';

describe('FullNameListComponent', () => {
  let component: FullNameListComponent;
  let fixture: ComponentFixture<FullNameListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FullNameListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FullNameListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
