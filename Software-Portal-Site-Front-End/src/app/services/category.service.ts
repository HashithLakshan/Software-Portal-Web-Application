import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

   constructor(private http: HttpClient) { }
     private apiUrl = 'http://localhost:8081/api/Category';

            getAllCategories(commonStatus:any):Observable<any>{
              return this.http.get<any>(`${this.apiUrl}/allCategories/${commonStatus}`);
            }

            getByIdCategory(categoryId:any):Observable<any>{
              return this.http.get<any>(`${this.apiUrl}/getByIdCat/${categoryId}`);
            }

            getAllCategoriesActive(commonStatus: string, page: number, size: number): Observable<any> {
              return this.http.get<any>(`${this.apiUrl}/filtered?commonStatus=${commonStatus}&page=${page}&size=${size}`);
            }
            searchCategoryName(categoryName: string): Observable<any> {
              return this.http.get<any>(`${this.apiUrl}/getByIdCatName?categoryName=${categoryName}`);
            }
           
            statusUpdate(categoryId:string,commonStatus: string): Observable<any> {
              const url = `${this.apiUrl}/delete?categoryId=${categoryId}&commonStatus=${commonStatus}`;
              return this.http.put<any>(url, {}); // Added empty body to comply with PUT request standards
            }
            saveCategory(payload: any): Observable<any> {
              
              return this.http.post<any>(`${this.apiUrl}/save`, payload);
            }
}
