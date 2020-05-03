import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IShoppingCar } from 'app/shared/model/shopping-car.model';
import { ShoppingCarService } from './shopping-car.service';

@Component({
  templateUrl: './shopping-car-delete-dialog.component.html'
})
export class ShoppingCarDeleteDialogComponent {
  shoppingCar?: IShoppingCar;

  constructor(
    protected shoppingCarService: ShoppingCarService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shoppingCarService.delete(id).subscribe(() => {
      this.eventManager.broadcast('shoppingCarListModification');
      this.activeModal.close();
    });
  }
}
