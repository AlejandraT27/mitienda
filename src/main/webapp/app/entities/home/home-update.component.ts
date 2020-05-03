import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IHome, Home } from 'app/shared/model/home.model';
import { HomeService } from './home.service';

@Component({
  selector: 'jhi-home-update',
  templateUrl: './home-update.component.html'
})
export class HomeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    adress: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    locality: []
  });

  constructor(protected homeService: HomeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ home }) => {
      this.updateForm(home);
    });
  }

  updateForm(home: IHome): void {
    this.editForm.patchValue({
      id: home.id,
      firstName: home.firstName,
      lastName: home.lastName,
      adress: home.adress,
      phone: home.phone,
      locality: home.locality
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const home = this.createFromForm();
    if (home.id !== undefined) {
      this.subscribeToSaveResponse(this.homeService.update(home));
    } else {
      this.subscribeToSaveResponse(this.homeService.create(home));
    }
  }

  private createFromForm(): IHome {
    return {
      ...new Home(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      adress: this.editForm.get(['adress'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      locality: this.editForm.get(['locality'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHome>>): void {
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
