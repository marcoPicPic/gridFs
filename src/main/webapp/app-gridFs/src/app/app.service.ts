import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse , HttpHeaders} from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { Report } from './report';
import {Document} from './document';


@Injectable({ providedIn: 'root' })
export class AppService {
    public resourceUrl = 'http://localhost:9999';

    constructor(private http: HttpClient) {}

    public getMigrateFile(nbFile: string, little: boolean, medium: boolean, big: boolean): Observable<HttpResponse<Report>> {
        return this.http.get<Report>(
          `${this.resourceUrl}/all/nb/${nbFile}/little/${little}/medium/${medium}/big/${big}`, { observe: 'response' });
      }

      public getInfoDocumentByEmailId(emailId: number): Observable<HttpResponse<Document>> {
        return this.http.get<Document>(
          `${this.resourceUrl}/retrieve/emailId/${emailId}`, { observe: 'response' });
      }

      public downloadFile(emailId: number) {
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get( `${this.resourceUrl}/download/retrieve/emailId/${emailId}`, { headers: headers, responseType: 'blob' });
      }
}
