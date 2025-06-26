export class Zoom {
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
    systemProfileDto?: {
        systemProfilesId?: String;
        systemProfilesName?: String;
        systemProfilesDiscription?: String;
        commonStatus?: String;
        requestStatus?: String;
        systemProfilesPrice?: String;
        employeeDto?:{
            employeeId?:String;
        }
    }
    zoomTimeSlotsDto?:{
    zoomTimeSlotId?:String;
    slotOpenTime?:String;
    slotCloseTime?:String;
    commonStatus?:String;
    }
}