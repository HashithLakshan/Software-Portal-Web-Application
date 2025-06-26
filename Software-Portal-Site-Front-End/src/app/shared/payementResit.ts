export class PayemntResit{
id?:string;
saveDate?:string;
requestStatus?:String;
commonStatus?:String;
systemProfileDto?:{
    systemProfilesId?:string;
    systemProfilesName?:String;
    systemProfilesDiscription?:String;
    commonStatus?:String;
    requestStatus?:String;
    systemProfilesPrice?:String;
}
zoomDto?:{
    perchaseId?:String;
    sheduleTopic?: String;
    startDate?: String;
    startTime?: String;
    zoomLink?: String;
    meetingPassword?: String;
    meeting_Duration?: String;
    requestStatus?: String;
    commonStatus?: String;
    customerAddress?:String;
    customerNumber?:String;
    customerEmail?:String;
    customerName?:String;
    customerType?:String;
    companyRegNo?:String;
    zmeetingId?:String;
}
}