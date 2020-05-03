import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShoppingCar } from 'app/shared/model/shopping-car.model';

@Component({
  selector: 'jhi-shopping-car-detail',
  templateUrl: './shopping-car-detail.component.html'
})
export class ShoppingCarDetailComponent implements OnInit {
  shoppingCar: IShoppingCar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shoppingCar }) => (this.shoppingCar = shoppingCar));
  }

  previousState(): void {
    window.history.back();
  }
}
