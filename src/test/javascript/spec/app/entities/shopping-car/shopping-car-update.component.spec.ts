import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MitiendaTestModule } from '../../../test.module';
import { ShoppingCarUpdateComponent } from 'app/entities/shopping-car/shopping-car-update.component';
import { ShoppingCarService } from 'app/entities/shopping-car/shopping-car.service';
import { ShoppingCar } from 'app/shared/model/shopping-car.model';

describe('Component Tests', () => {
  describe('ShoppingCar Management Update Component', () => {
    let comp: ShoppingCarUpdateComponent;
    let fixture: ComponentFixture<ShoppingCarUpdateComponent>;
    let service: ShoppingCarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [ShoppingCarUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ShoppingCarUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShoppingCarUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShoppingCarService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ShoppingCar(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ShoppingCar();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
