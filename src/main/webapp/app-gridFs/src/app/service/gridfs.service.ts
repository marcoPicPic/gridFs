import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

//import { SERVER_API_URL } from 'app/app.constants';
//import { createRequestOption } from 'app/shared/util/request-util';
//import { IUser } from './user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
    public resourceUrl =  'http://localhost:9999/api/users';

    constructor(private http: HttpClient) {}

    /*find(login: string): Observable<HttpResponse<IUser>> {
        return this.http.get<IUser>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }*/



 
}
