import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnderecoDeletarComponent } from './endereco-deletar.component';

describe('EnderecoDeletarComponent', () => {
  let component: EnderecoDeletarComponent;
  let fixture: ComponentFixture<EnderecoDeletarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnderecoDeletarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EnderecoDeletarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
