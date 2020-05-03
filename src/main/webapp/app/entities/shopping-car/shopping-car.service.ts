import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IShoppingCar } from 'app/shared/model/shopping-car.model';

type EntityResponseType = HttpResponse<IShoppingCar>;
type EntityArrayResponseType = HttpResponse<IShoppingCar[]>;

@Injectable({ providedIn: 'root' })
export class ShoppingCarService {
  public resourceUrl = SERVER_API_URL + 'api/shopping-cars';

  constructor(protected http: HttpClient) {}

  create(shoppingCar: IShoppingCar): Observable<EntityResponseType> {
    return this.http.post<IShoppingCar>(this.resourceUrl, shoppingCar, { observe: 'response' });
  }

  update(shoppingCar: IShoppingCar): Observable<EntityResponseType> {
    return this.http.put<IShoppingCar>(this.resourceUrl, shoppingCar, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShoppingCar>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShoppingCar[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
