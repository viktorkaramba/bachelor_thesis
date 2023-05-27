import {ComponentFixture, TestBed} from '@angular/core/testing';

import {IncomeCarsMenuComponent} from './income-cars-menu.component';

describe('IncomeCarsMenuComponent', () => {
  let component: IncomeCarsMenuComponent;
  let fixture: ComponentFixture<IncomeCarsMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IncomeCarsMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncomeCarsMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
