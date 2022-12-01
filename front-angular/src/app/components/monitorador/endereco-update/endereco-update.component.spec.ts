import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnderecoUpdateComponent } from './endereco-update.component';

describe('EnderecoUpdateComponent', () => {
  let component: EnderecoUpdateComponent;
  let fixture: ComponentFixture<EnderecoUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnderecoUpdateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EnderecoUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
