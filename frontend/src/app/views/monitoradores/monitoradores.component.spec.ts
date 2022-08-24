import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoradoresComponent } from './monitoradores.component';

describe('MonitoradoresComponent', () => {
  let component: MonitoradoresComponent;
  let fixture: ComponentFixture<MonitoradoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoradoresComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoradoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
