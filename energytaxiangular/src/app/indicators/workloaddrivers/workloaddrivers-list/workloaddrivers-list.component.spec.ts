import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkLoadDriversListComponent} from './workloaddrivers-list.component';

describe('WorkLoadDriversListComponent', () => {
  let component: WorkLoadDriversListComponent;
  let fixture: ComponentFixture<WorkLoadDriversListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkLoadDriversListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkLoadDriversListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
