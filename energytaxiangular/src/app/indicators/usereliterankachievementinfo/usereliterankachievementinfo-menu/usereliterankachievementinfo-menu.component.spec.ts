import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsereliterankachievementinfoMenuComponent } from './usereliterankachievementinfo-menu.component';

describe('UsereliterankachievementinfoMenuComponent', () => {
  let component: UsereliterankachievementinfoMenuComponent;
  let fixture: ComponentFixture<UsereliterankachievementinfoMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsereliterankachievementinfoMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsereliterankachievementinfoMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
