import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class systemprofiles {


     constructor(private http: HttpClient) { }
      private apiUrl = 'http://localhost:8081/api/SystemProfile';

      getAllSystemProfies(requestStatus:any,commonStatus:any):Observable<any>{
        return this.http.get<any>(`${this.apiUrl}/getAllActiveProfilesPendingAndApproved/${requestStatus}/${commonStatus}`);
      }

      getBySystemProfile(systemProfilesId:any):Observable<any>{
        return this.http.get<any>(`${this.apiUrl}/getByIdSystem/${systemProfilesId}`);
      }

      getAllSystemProfiesCategoryId(requestStatus:any,commonStatus:any,categoryId : any, page: number, size: number):Observable<any>{
        return this.http.get<any>(`${this.apiUrl}/getAllActiveProfilesPendingAndApprovedCategory?requestStatus=${requestStatus}&commonStatus=${commonStatus}&categoryId=${categoryId}&page=${page}&size=${size}`);
      }

      getAllSystemProfiessliderWith(categoryId:any,value:number,requestStatus :string,commonStatus: string, page: number, size: number):Observable<any>{
        return this.http.get<any>(`${this.apiUrl}/getAllProfilesWithSliderBar?categoryId=${categoryId}&value=${value}&requestStatus=${requestStatus}&commonStatus=${commonStatus}&page=${page}&size=${size}`);
      }

      getSystemProfileFilterStatus(requestStatus: string, commonStatus: string, page: number, size: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/getAllActiveProfilesPendingAndApprovedSuper?requestStatus=${requestStatus}&commonStatus=${commonStatus}&page=${page}&size=${size}`);
      }

      getSystemProfileAllSuperAdmin(requestStatus: string, commonStatus: string, page: number, size: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/getAllActiveProfilesApprovedAndInactiveSuper?requestStatus=${requestStatus}&commonStatus=${commonStatus}&page=${page}&size=${size}`);
      }

      saveProfile(payload: any): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/save/Profile`, payload);
      }
      UpdateProfile(payload: any): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/Update/Profile`, payload);
      }
    
      updateStatusInactive(systemProfilesId:any): Observable<any> {
        const url = `${this.apiUrl}/Inactive?systemProfilesId=${systemProfilesId}`;
        return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
      }

      updateStatusActive(systemProfilesId:any): Observable<any> {
        const url = `${this.apiUrl}/Active?systemProfilesId=${systemProfilesId}`;
        return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
      }

      updateStatusApprove(systemProfilesId:any): Observable<any> {
        const url = `${this.apiUrl}/approve?systemProfilesId=${systemProfilesId}`;
        return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
      }

      deletePermanet(systemProfilesId:any): Observable<any> {
        const url = `${this.apiUrl}/DeletePermanent?systemProfilesId=${systemProfilesId}`;
        return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
      }

      geSystemFilterId(commonStatus: string, requestStatus: string, systemProfileId: String): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/getById?commonStatus=${commonStatus}&requestStatus=${requestStatus}&systemProfileId=${systemProfileId}`);
      }

      getEmployeesFilterId(commonStatus: string, requestStatus: string, employeeId: String): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/getByEmployeeId?commonStatus=${commonStatus}&requestStatus=${requestStatus}&employeeId=${employeeId}`);
      }

      
      getSystemProfileSeachBarTop( inPut: any, page: number, size: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/getAllSearching?inPut=${inPut}&page=${page}&size=${size}`);
      }
    }
    