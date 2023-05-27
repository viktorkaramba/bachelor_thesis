import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RankFormComponent } from './rank-form.component';

describe('RankFormComponent', () => {
  let component: RankFormComponent;
  let fixture: ComponentFixture<RankFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RankFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RankFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
