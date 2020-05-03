import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MitiendaSharedModule } from 'app/shared/shared.module';
import { ShoppingCarComponent } from './shopping-car.component';
import { ShoppingCarDetailComponent } from './shopping-car-detail.component';
import { ShoppingCarUpdateComponent } from './shopping-car-update.component';
import { ShoppingCarDeleteDialogComponent } from './shopping-car-delete-dialog.component';
import { shoppingCarRoute } from './shopping-car.route';

@NgModule({
  imports: [MitiendaSharedModule, RouterModule.forChild(shoppingCarRoute)],
  declarations: [ShoppingCarComponent, ShoppingCarDetailComponent, ShoppingCarUpdateComponent, ShoppingCarDeleteDialogComponent],
  entryComponents: [ShoppingCarDeleteDialogComponent]
})
export class MitiendaShoppingCarModule {}
