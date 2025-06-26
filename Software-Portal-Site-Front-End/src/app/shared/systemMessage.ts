export class SystemMessageIssues{
    issueId?:string;
    subject?:string;
    body?:string;
   receivedDate?:string;
  receivedTime?:string;
    commonStatus?:string;
    replyMessageStatus?:string;
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
        zmeetingId?:String; systemProfileDto?: {
            systemProfilesId?: String;
            systemProfilesName?: String;
            systemProfilesDiscription?: String;
            commonStatus?: String;
            requestStatus?: String;
            systemProfilesPrice?: String;
           
    }
}
}