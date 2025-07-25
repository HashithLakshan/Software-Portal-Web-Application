export class SystemIssuesAnswerMessages{
   answerId?:String;
    answerSubject?:String
    answerBody?:String
    sendDate?:String
    sendTime?:String
    commonStatus?:String
    systemIssueMessagesDto?:{
        issueId?:string;
        subject?:string;
        body?:string;
       receivedDate?:string;
      receivedTime?:string;
        commonStatus?:string;
        replyMessageStatus?:string;
        zoomDto?:{
            perchaseId?:String;
            customerEmail?:String;
        }
    }
}