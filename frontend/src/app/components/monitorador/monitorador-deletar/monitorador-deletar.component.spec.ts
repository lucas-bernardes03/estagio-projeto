import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoradorDeletarComponent } from './monitorador-deletar.component';

describe('MonitoradorDeletarComponent', () => {
  let component: MonitoradorDeletarComponent;
  let fixture: ComponentFixture<MonitoradorDeletarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoradorDeletarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoradorDeletarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
