import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkLoadDriversMenuComponent} from './work-load-drivers-menu.component';

describe('WorkLoadDriversMenuComponent', () => {
  let component: WorkLoadDriversMenuComponent;
  let fixture: ComponentFixture<WorkLoadDriversMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkLoadDriversMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkLoadDriversMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
