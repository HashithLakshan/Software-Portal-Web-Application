import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ZoomService {
  private apiUrl = 'http://localhost:8081/api/zoom'; // Update to match your API base URL

  constructor(private http: HttpClient) { }

  /**
   * Schedule a Zoom meeting.
   * 
   * @param topic - The topic of the meeting
   * @param startTime - The start time of the meeting in ISO format
   * @param duration - The duration of the meeting in minutes
   * @param systemProfilesId - The ID of the system profile
   * @param rollName 
   *  @param customerNumber
   *  @param customerAddress
   *  @param customerEmail
   *  @param customerName
   *  @param customerType
   *  @param companyRegNo

   * @returns

  customerAddress,customerEmail,customerName,customerType,companyRegNo
   */
  scheduleMeeting(
    perchaseId: string,
    zoomTimeSlotId: string,
    topic: string,
    startTime: string,
    duration: string,
    systemProfilesId: string,
    rollName: string,
    customerNumber: string,
    customerAddress: string,
    customerEmail: string,
    customerName: string,
    customerType: string,
    companyRegNo: string,

  ): Observable<any> {
    const params = new HttpParams()
      .set('perchaseId', perchaseId)
      .set('zoomTimeSlotId', zoomTimeSlotId)
      .set('topic', topic)
      .set('startTime', startTime)
      .set('duration', duration)
      .set('systemProfilesId', systemProfilesId)
      .set('rollName', rollName)
      .set('customerNumber', customerNumber)
      .set('customerAddress', customerAddress)
      .set('customerEmail', customerEmail)
      .set('customerName', customerName)
      .set('customerType', customerType)
      .set('companyRegNo', companyRegNo);

    return this.http.post(`${this.apiUrl}/schedule`, null, { params });
  }

  scheduleMeetingApproving(
    perchaseId: string,
    zoomTimeSlotId: string,
    topic: string,
    startTime: string,
    duration: string,
    systemProfilesId: string,
    rollName: string,
    customerNumber: string,
    customerAddress: string,
    customerEmail: string,
    customerName: string,
    customerType: string,
    companyRegNo: string,

  ): Observable<any> {
    const params = new HttpParams()
      .set('perchaseId', perchaseId)
      .set('zoomTimeSlotId', zoomTimeSlotId)
      .set('topic', topic)
      .set('startTime', startTime)
      .set('duration', duration)
      .set('systemProfilesId', systemProfilesId)
      .set('rollName', rollName)
      .set('customerNumber', customerNumber)
      .set('customerAddress', customerAddress)
      .set('customerEmail', customerEmail)
      .set('customerName', customerName)
      .set('customerType', customerType)
      .set('companyRegNo', companyRegNo);

    return this.http.post(`${this.apiUrl}/scheduleApproving`, null, { params });
  }

  pendingScheduleMeeting(
    zoomTimeSlotId: string,
    systemProfilesId: string,
    StartDate: string,
    topic: string,
    customerNumber: string,
    customerAddress: string,
    customerEmail: string,
    customerName: string,
    customerType: string,
    companyRegNo: string,

  ): Observable<any> {
    const params = new HttpParams()
      .set('zoomTimeSlotId', zoomTimeSlotId)
      .set('systemProfilesId', systemProfilesId)
      .set('StartDate', StartDate)
      .set('topic', topic)
      .set('customerNumber', customerNumber)
      .set('customerAddress', customerAddress)
      .set('customerEmail', customerEmail)
      .set('customerName', customerName)
      .set('customerType', customerType)
      .set('companyRegNo', companyRegNo);

    return this.http.post(`${this.apiUrl}/savePending`, null, { params });
  }

  getDashboard(action: any,commonStatus : any,requestStatus : any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getAllToday?action=${action}&commonStatus=${commonStatus}&requestStatus=${requestStatus}`);
  }
  zoomCallBack(code: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/zoom/callback?code=${code}`);
  }

  reminderMeetings(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/reminderMeting`);
  }

  getZoomIdSearch(zMeetingId: string, commonStatus: string, requestStatus: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getDetailsZoomId/${zMeetingId}/${commonStatus}/${requestStatus}`);
  }
  getPerchaseIdSearch(perchaseId: string, commonStatus: string, requestStatus: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getDetailsPerchaseId/${perchaseId}/${commonStatus}/${requestStatus}`);
  }
getZoomFilterToday(  requestStatus: string, commonStatus: string,page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getAllTodayPage?requestStatus=${requestStatus}&commonStatus=${commonStatus}&page=${page}&size=${size}`);
  }

  getZoomFilterStatus(commonStatus: string, requestStatus: string, page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getAllActiveProfilesPendingAndApprovedSuper?commonStatus=${commonStatus}&requestStatus=${requestStatus}&page=${page}&size=${size}`);
  }

  getZoomFilterDate(fromDate: string, toDate: string, commonStatus: string, requestStatus: string, page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getDetailsAllDateFilter?fromDate=${fromDate}&toDate=${toDate}&commonStatus=${commonStatus}&requestStatus=${requestStatus}&page=${page}&size=${size}`);
  }

  deleteZoomMeeting(meetingId: string): Observable<any> {
    const url = `${this.apiUrl}/delete-meeting/${meetingId}`;
    return this.http.delete<any>(url);
  }

  changeStatus(changeStatus: string, perchaseId: string): Observable<any> {
    const url = `${this.apiUrl}/updateStatus/${changeStatus}/${perchaseId}`;
    return this.http.put<any>(url, null);
  }


}