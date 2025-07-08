import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  
     constructor(private http: HttpClient) { }
      private apiUrl = 'http://localhost:8081/api/employee/';
    
      saveUser(payload: any): Observable<any> {
        const rollName1 = ""; 
        let rollName = String(rollName1);
        console.log(rollName);
        console.log(payload);
        return this.http.post<any>(`${this.apiUrl}save/employee?rollName=${rollName}`, payload);
      }
      saveUserAdmin(payload: any): Observable<any> {
        const rollName1 = "superAdmin"; 
        let rollName = String(rollName1);
        return this.http.post<any>(`${this.apiUrl}save/employee?rollName=${rollName}`, payload);
      }
      
      
      getEmployeesFilterStatus(commonStatus: string, requestStatus: string, page: number, size: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}filtered?commonStatus=${commonStatus}&requestStatus=${requestStatus}&page=${page}&size=${size}`);
      }
      
      getEmployeesFilterId(commonStatus: string, requestStatus: string, employeeId: String): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}getDetailEmployeeId?commonStatus=${commonStatus}&requestStatus=${requestStatus}&employeeId=${employeeId}`);
      }

      getEmployeesFilterCompany(commonStatus: string, requestStatus: string, companyName: String): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}getDetailEmployeeUserName?commonStatus=${commonStatus}&requestStatus=${requestStatus}&companyName=${companyName}`);
      }

      getEmployeesUpdateStatus(employeeId: string, requestStatus: string, commonStatus: string): Observable<any> {
        const url = `${this.apiUrl}updateStatus?employeeId=${employeeId}&requestStatus=${requestStatus}&commonStatus=${commonStatus}`;
        return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
      }
      


}