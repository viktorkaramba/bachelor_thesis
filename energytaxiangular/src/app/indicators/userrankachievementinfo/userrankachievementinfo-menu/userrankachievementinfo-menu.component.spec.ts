import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserrankachievementinfoMenuComponent } from './userrankachievementinfo-menu.component';

describe('UserrankachievementinfoMenuComponent', () => {
  let component: UserrankachievementinfoMenuComponent;
  let fixture: ComponentFixture<UserrankachievementinfoMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserrankachievementinfoMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserrankachievementinfoMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
