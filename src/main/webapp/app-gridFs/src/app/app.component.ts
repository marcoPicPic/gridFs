import { Component } from '@angular/core';
import { AppService } from './app.service';
import {HttpResponse} from '@angular/common/http';
import { Report } from './Report';
import {Document} from './document';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent {
  panelOpenState = false;
  resum: Report;
  documentInfo: Document;

  constructor(
    private appservice: AppService
  ) { }

  import(nbFile: number) {
    this.appservice.getMigrateFile(nbFile).subscribe(
        (res: HttpResponse<Report>) => this.onSuccessMigrate(res),
        (res: HttpResponse<any>) => this.onError(res)
    );
  }

  private onSuccessMigrate(data) {
    this.resum = data.body;
  }

  private onError(error) {
    // this.alertService.error(error.error, error.message, null);
  }

  infoFile(emailId: number) {
    this.appservice.getInfoDocumentByEmailId(emailId).subscribe(
        (res: HttpResponse<Document>) => this.onSuccessInfo(res),
        (res: HttpResponse<any>) => this.onError(res)
    );
  }

  private onSuccessInfo(data) {
    this.documentInfo = data.body;
  }





  /*downloadFile(id: number, contentType: string) {
    return this.http.get('http://localhost:9999//retrieve/emailId/12',
     { headers: new HttpHeaders().append('Content-Type', contentType), responseType: 'blob', observe: 'body' });
    }*/

    downloadFile(emailId: number) {

      return this.appservice.downloadFile(emailId).subscribe(
      res => {
      console.log('start download:', res);
      const url = window.URL.createObjectURL(res);
      const a = document.createElement('a');
      document.body.appendChild(a);
      a.setAttribute('style', 'display: none');
      a.href = url;
      a.download =  'test' + emailId + '.pdf';
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove(); // remove the element
      }, error => {
      console.log('download error:', JSON.stringify(error));
      }, () => {
      console.log('Completed file download.');
      });
      }
}
