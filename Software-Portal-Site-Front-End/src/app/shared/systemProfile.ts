export class SystemProfile{
    systemProfilesId?:String;
    systemProfilesName?:String;
    systemProfilesDiscription?:String;
    commonStatus?:String;
    requestStatus?:String;
    systemProfilesPrice?:String;
    categoryDto?:{
        categoryName?: String;
        categoryId?:String;
    }
    subCategoryDto?:{
        subCategoryName?: String;
        subCategoryId?: String;

    }
    employeeDto?:{
    employeeId?:String;
    employeeNumber?:String;
    companyName?:String;
    employeeNIC?:String;
    commonStatus?:String
    companyRgNo?:String
    }
    userDto?:{
        commonStatus?:String;
        userId?:String;
        email?:String;
        password?:String;
        userName?:String;
        requestStatus?:String;

    }
  
}