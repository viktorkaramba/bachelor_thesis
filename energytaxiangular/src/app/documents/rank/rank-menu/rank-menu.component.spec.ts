import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RankMenuComponent } from './rank-menu.component';

describe('RankMenuComponent', () => {
  let component: RankMenuComponent;
  let fixture: ComponentFixture<RankMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RankMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RankMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
