import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoradorLerComponent } from './monitorador-ler.component';

describe('MonitoradorLerComponent', () => {
  let component: MonitoradorLerComponent;
  let fixture: ComponentFixture<MonitoradorLerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoradorLerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoradorLerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
