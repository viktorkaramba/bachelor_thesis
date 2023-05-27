import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EliterankFormComponent } from './eliterank-form.component';

describe('EliterankFormComponent', () => {
  let component: EliterankFormComponent;
  let fixture: ComponentFixture<EliterankFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EliterankFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EliterankFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
