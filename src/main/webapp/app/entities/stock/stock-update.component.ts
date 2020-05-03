import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStock, Stock } from 'app/shared/model/stock.model';
import { StockService } from './stock.service';

@Component({
  selector: 'jhi-stock-update',
  templateUrl: './stock-update.component.html'
})
export class StockUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numberProduct: [null, [Validators.required]],
    nameProduct: [null, [Validators.required]],
    description: [],
    inventory: []
  });

  constructor(protected stockService: StockService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stock }) => {
      this.updateForm(stock);
    });
  }

  updateForm(stock: IStock): void {
    this.editForm.patchValue({
      id: stock.id,
      numberProduct: stock.numberProduct,
      nameProduct: stock.nameProduct,
      description: stock.description,
      inventory: stock.inventory
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stock = this.createFromForm();
    if (stock.id !== undefined) {
      this.subscribeToSaveResponse(this.stockService.update(stock));
    } else {
      this.subscribeToSaveResponse(this.stockService.create(stock));
    }
  }

  private createFromForm(): IStock {
    return {
      ...new Stock(),
      id: this.editForm.get(['id'])!.value,
      numberProduct: this.editForm.get(['numberProduct'])!.value,
      nameProduct: this.editForm.get(['nameProduct'])!.value,
      description: this.editForm.get(['description'])!.value,
      inventory: this.editForm.get(['inventory'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>): void {
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
}
