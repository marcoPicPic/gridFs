import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CampaignCallQualificationData} from '../../models/CampaignCallQualificationData.model';
import { Operator } from 'src/app/models/Operator.model';


@Injectable({
  providedIn: 'root'
})
export class CampaignApiService {

  private campaignApiBaseUrl = '/jamc/resources/v1/web';
  private tenantUuid;

  constructor(private http: HttpClient) {
    const searchParams = new HttpParams({fromString: location.search.slice(1)});
    if (searchParams.has('tenantUuid')) {
      this.tenantUuid = searchParams.get('tenantUuid');
    }
  }

  getCampaignCallQualificationData(hideFinishedCampaigns: string,  agentIds: number[], startDate: string, endDate: string):
  Observable<HttpResponse<CampaignCallQualificationData[]>> {
    return this.http.post<CampaignCallQualificationData[]>(
      `${this.campaignApiBaseUrl}/tenant/${this.tenantUuid}/campaign/recalls/qualifications/start/${startDate}/end/${endDate}/${hideFinishedCampaigns}`,
       agentIds, { observe: 'response' });
  }

  public getOperators(): Observable<HttpResponse<Operator[]>> {
    return this.http.get<Operator[]>(
      `${this.campaignApiBaseUrl}/tenant/${this.tenantUuid}/campaign/operators/all`, { observe: 'response' });
  }


}
