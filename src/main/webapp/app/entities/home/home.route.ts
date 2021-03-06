import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHome, Home } from 'app/shared/model/home.model';
import { HomeService } from './home.service';
import { HomeComponent } from './home.component';
import { HomeDetailComponent } from './home-detail.component';
import { HomeUpdateComponent } from './home-update.component';

@Injectable({ providedIn: 'root' })
export class HomeResolve implements Resolve<IHome> {
  constructor(private service: HomeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHome> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((home: HttpResponse<Home>) => {
          if (home.body) {
            return of(home.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Home());
  }
}

export const homeRoute: Routes = [
  {
    path: '',
    component: HomeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'mitiendaApp.home.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HomeDetailComponent,
    resolve: {
      home: HomeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mitiendaApp.home.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HomeUpdateComponent,
    resolve: {
      home: HomeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mitiendaApp.home.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HomeUpdateComponent,
    resolve: {
      home: HomeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mitiendaApp.home.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
