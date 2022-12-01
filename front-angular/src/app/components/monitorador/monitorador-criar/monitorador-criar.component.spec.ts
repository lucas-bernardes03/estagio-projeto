import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoradorCriarComponent } from './monitorador-criar.component';

describe('MonitoradorCriarComponent', () => {
  let component: MonitoradorCriarComponent;
  let fixture: ComponentFixture<MonitoradorCriarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoradorCriarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoradorCriarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
