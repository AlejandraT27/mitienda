import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MitiendaSharedModule } from 'app/shared/shared.module';
import { HomeComponent } from './home.component';
import { HomeDetailComponent } from './home-detail.component';
import { HomeUpdateComponent } from './home-update.component';
import { HomeDeleteDialogComponent } from './home-delete-dialog.component';
import { homeRoute } from './home.route';

@NgModule({
  imports: [MitiendaSharedModule, RouterModule.forChild(homeRoute)],
  declarations: [HomeComponent, HomeDetailComponent, HomeUpdateComponent, HomeDeleteDialogComponent],
  entryComponents: [HomeDeleteDialogComponent]
})
export class MitiendaHomeModule {}
