export class customerToSystemFeedback{
    ctSfId?:String;
    ctSfSubject?:String;
    ctSfMeesage?:String;
    messageStatus?:String;
    systemProfileDto?:{
        systemProfilesId?: String;
    }
    userDto?:{
        userId?: String;
    }
}