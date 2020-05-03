import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IShoppingCar, ShoppingCar } from 'app/shared/model/shopping-car.model';
import { ShoppingCarService } from './shopping-car.service';
import { ShoppingCarComponent } from './shopping-car.component';
import { ShoppingCarDetailComponent } from './shopping-car-detail.component';
import { ShoppingCarUpdateComponent } from './shopping-car-update.component';

@Injectable({ providedIn: 'root' })
export class ShoppingCarResolve implements Resolve<IShoppingCar> {
  constructor(private service: ShoppingCarService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShoppingCar> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((shoppingCar: HttpResponse<ShoppingCar>) => {
          if (shoppingCar.body) {
            return of(shoppingCar.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShoppingCar());
  }
}

export const shoppingCarRoute: Routes = [
  {
    path: '',
    component: ShoppingCarComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'mitiendaApp.shoppingCar.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ShoppingCarDetailComponent,
    resolve: {
      shoppingCar: ShoppingCarResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mitiendaApp.shoppingCar.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ShoppingCarUpdateComponent,
    resolve: {
      shoppingCar: ShoppingCarResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mitiendaApp.shoppingCar.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ShoppingCarUpdateComponent,
    resolve: {
      shoppingCar: ShoppingCarResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mitiendaApp.shoppingCar.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
