import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {CampaignApiService} from './Campaign-api.service';

@NgModule({
  imports: [
    HttpClientModule,
  ],
  providers: [
    CampaignApiService
  ]
})
export class CampaignApiModule { }
