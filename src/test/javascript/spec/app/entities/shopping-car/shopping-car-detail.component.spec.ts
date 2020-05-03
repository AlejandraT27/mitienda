import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MitiendaTestModule } from '../../../test.module';
import { ShoppingCarDetailComponent } from 'app/entities/shopping-car/shopping-car-detail.component';
import { ShoppingCar } from 'app/shared/model/shopping-car.model';

describe('Component Tests', () => {
  describe('ShoppingCar Management Detail Component', () => {
    let comp: ShoppingCarDetailComponent;
    let fixture: ComponentFixture<ShoppingCarDetailComponent>;
    const route = ({ data: of({ shoppingCar: new ShoppingCar(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MitiendaTestModule],
        declarations: [ShoppingCarDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ShoppingCarDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ShoppingCarDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load shoppingCar on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.shoppingCar).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
