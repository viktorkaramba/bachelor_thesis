import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EliterankListComponent } from './eliterank-list.component';

describe('EliterankListComponent', () => {
  let component: EliterankListComponent;
  let fixture: ComponentFixture<EliterankListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EliterankListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EliterankListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
