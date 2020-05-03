import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ISeller, Seller } from 'app/shared/model/seller.model';
import { SellerService } from './seller.service';
import { IStock } from 'app/shared/model/stock.model';
import { StockService } from 'app/entities/stock/stock.service';

@Component({
  selector: 'jhi-seller-update',
  templateUrl: './seller-update.component.html'
})
export class SellerUpdateComponent implements OnInit {
  isSaving = false;
  users: IStock[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    userId: []
  });

  constructor(
    protected sellerService: SellerService,
    protected stockService: StockService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seller }) => {
      this.updateForm(seller);

      this.stockService
        .query({ 'sellerId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IStock[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IStock[]) => {
          if (!seller.userId) {
            this.users = resBody;
          } else {
            this.stockService
              .find(seller.userId)
              .pipe(
                map((subRes: HttpResponse<IStock>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IStock[]) => (this.users = concatRes));
          }
        });
    });
  }

  updateForm(seller: ISeller): void {
    this.editForm.patchValue({
      id: seller.id,
      firstName: seller.firstName,
      userId: seller.userId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seller = this.createFromForm();
    if (seller.id !== undefined) {
      this.subscribeToSaveResponse(this.sellerService.update(seller));
    } else {
      this.subscribeToSaveResponse(this.sellerService.create(seller));
    }
  }

  private createFromForm(): ISeller {
    return {
      ...new Seller(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      userId: this.editForm.get(['userId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeller>>): void {
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

  trackById(index: number, item: IStock): any {
    return item.id;
  }
}
