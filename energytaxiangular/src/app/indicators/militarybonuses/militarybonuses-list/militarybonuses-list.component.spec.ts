import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilitarybonusesListComponent } from './militarybonuses-list.component';

describe('MilitarybonusesListComponent', () => {
  let component: MilitarybonusesListComponent;
  let fixture: ComponentFixture<MilitarybonusesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MilitarybonusesListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MilitarybonusesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
