import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthServiceService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

 constructor(private http: HttpClient,private httpClient: HttpClient,private userAuthService: AuthServiceService) { }

   requestHeader = new HttpHeaders({ 'No-Auth': 'True' }); d:any

  
  private apiUrl = 'http://localhost:8081/api/user/';

  private apiUrl2 = 'http://localhost:8081/api/auth/';


  registerAdmin(payload: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl2}signup/admin`, payload,{headers: this.requestHeader,});
  }

  adminLogin(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl2}signin`, payload,{headers: this.requestHeader,});
  }

  getUser(userName: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}findByUserName/${userName}`);
  }

//   forgetPasswordEmailSend(email: string): Observable<any> {
//     const url = `${this.apiUrl}forgetPassword`;
//     return this.http.post<any>(url, { email }); 
// }
 
// recoverPassword(sendCode: any, email: any, password: any): Observable<any> {
//     const url = `${this.apiUrl}recoverPassword`; 
//     return this.http.post<any>(url, { sendCode, email, password }); // Sending data in the body
// }

forgetPasswordEmailSend(
  email: any,
  ): Observable<any> {
    const params = new HttpParams()
      .set('email', email);
    return this.http.post(`${this.apiUrl}forgetPassword`, null, { params });



  } 
  
  recoverPassword(
    sendCode: any,
    email: any,
    password: any,
  
    ): Observable<any> {
      const params = new HttpParams()
        .set('sendCode', sendCode)
        .set('email', email)
        .set('password', password)
  
      return this.http.post(`${this.apiUrl}recover`, null, { params });
    }

   
}
