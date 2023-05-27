import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilitarybonusesMenuComponent } from './militarybonuses-menu.component';

describe('MilitarybonusesMenuComponent', () => {
  let component: MilitarybonusesMenuComponent;
  let fixture: ComponentFixture<MilitarybonusesMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MilitarybonusesMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MilitarybonusesMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
