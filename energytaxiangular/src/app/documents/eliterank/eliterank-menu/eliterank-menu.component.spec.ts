import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EliterankMenuComponent } from './eliterank-menu.component';

describe('EliterankMenuComponent', () => {
  let component: EliterankMenuComponent;
  let fixture: ComponentFixture<EliterankMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EliterankMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EliterankMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
