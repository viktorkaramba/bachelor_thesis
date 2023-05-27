import {ComponentFixture, TestBed} from '@angular/core/testing';

import {IncomeCarsListComponent} from './incomecars-list.component';

describe('IncomeCarsListComponent', () => {
  let component: IncomeCarsListComponent;
  let fixture: ComponentFixture<IncomeCarsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IncomeCarsListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncomeCarsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
