import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsereliterankachievementinfoListComponent } from './usereliterankachievementinfo-list.component';

describe('UsereliterankachievementinfoListComponent', () => {
  let component: UsereliterankachievementinfoListComponent;
  let fixture: ComponentFixture<UsereliterankachievementinfoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsereliterankachievementinfoListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsereliterankachievementinfoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
