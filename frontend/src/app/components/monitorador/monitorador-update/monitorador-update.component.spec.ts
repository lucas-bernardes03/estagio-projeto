import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoradorUpdateComponent } from './monitorador-update.component';

describe('MonitoradorUpdateComponent', () => {
  let component: MonitoradorUpdateComponent;
  let fixture: ComponentFixture<MonitoradorUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoradorUpdateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoradorUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
