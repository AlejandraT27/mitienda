import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.MitiendaProductModule)
      },
      {
        path: 'seller',
        loadChildren: () => import('./seller/seller.module').then(m => m.MitiendaSellerModule)
      },
      {
        path: 'shopping-car',
        loadChildren: () => import('./shopping-car/shopping-car.module').then(m => m.MitiendaShoppingCarModule)
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.MitiendaPaymentModule)
      },
      {
        path: 'home',
        loadChildren: () => import('./home/home.module').then(m => m.MitiendaHomeModule)
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.MitiendaStockModule)
      },
      {
        path: 'product-category',
        loadChildren: () => import('./product-category/product-category.module').then(m => m.MitiendaProductCategoryModule)
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.MitiendaCustomerModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MitiendaEntityModule {}
