import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserrankachievementinfoListComponent } from './userrankachievementinfo-list.component';

describe('UserrankachievementinfoListComponent', () => {
  let component: UserrankachievementinfoListComponent;
  let fixture: ComponentFixture<UserrankachievementinfoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserrankachievementinfoListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserrankachievementinfoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
