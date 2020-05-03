import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IShoppingCar, ShoppingCar } from 'app/shared/model/shopping-car.model';
import { ShoppingCarService } from './shopping-car.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';

@Component({
  selector: 'jhi-shopping-car-update',
  templateUrl: './shopping-car-update.component.html'
})
export class ShoppingCarUpdateComponent implements OnInit {
  isSaving = false;
  users: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    numberProduct: [null, [Validators.required]],
    product: [null, [Validators.required]],
    description: [],
    quantity: [null, [Validators.required]],
    totalPurchase: [],
    userId: []
  });

  constructor(
    protected shoppingCarService: ShoppingCarService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shoppingCar }) => {
      this.updateForm(shoppingCar);

      this.customerService
        .query({ 'shoppingCarId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ICustomer[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICustomer[]) => {
          if (!shoppingCar.userId) {
            this.users = resBody;
          } else {
            this.customerService
              .find(shoppingCar.userId)
              .pipe(
                map((subRes: HttpResponse<ICustomer>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICustomer[]) => (this.users = concatRes));
          }
        });
    });
  }

  updateForm(shoppingCar: IShoppingCar): void {
    this.editForm.patchValue({
      id: shoppingCar.id,
      numberProduct: shoppingCar.numberProduct,
      product: shoppingCar.product,
      description: shoppingCar.description,
      quantity: shoppingCar.quantity,
      totalPurchase: shoppingCar.totalPurchase,
      userId: shoppingCar.userId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shoppingCar = this.createFromForm();
    if (shoppingCar.id !== undefined) {
      this.subscribeToSaveResponse(this.shoppingCarService.update(shoppingCar));
    } else {
      this.subscribeToSaveResponse(this.shoppingCarService.create(shoppingCar));
    }
  }

  private createFromForm(): IShoppingCar {
    return {
      ...new ShoppingCar(),
      id: this.editForm.get(['id'])!.value,
      numberProduct: this.editForm.get(['numberProduct'])!.value,
      product: this.editForm.get(['product'])!.value,
      description: this.editForm.get(['description'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      totalPurchase: this.editForm.get(['totalPurchase'])!.value,
      userId: this.editForm.get(['userId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShoppingCar>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICustomer): any {
    return item.id;
  }
}
