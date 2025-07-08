import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/coustomer/home/home.component';
import { CoustomerComponent } from './components/sign-up/coustomer/coustomer.component';
import { EmployeeComponent } from './components/sign-up/employee/employee.component';
import { LoginComponent } from './components/login/login.component';
import { ContactUsComponent } from './components/coustomer/contact-us/contact-us.component';
import { AboutUsComponent } from './components/coustomer/about-us/about-us.component';
import { CategorysComponent } from './components/coustomer/categorys/categorys.component';
import { CategoryIncludeComponent } from './components/coustomer/category-include/category-include.component';
import { SystemPortfoliyoComponent } from './components/coustomer/system-portfoliyo/system-portfoliyo.component';
import { SuperAdminDashbordComponent } from './components/superAdmin/super-admin-dashbord/super-admin-dashbord.component';
import { SuperAdminComponent } from './components/sign-up/super-admin/super-admin.component';
import { SideNavComponent } from './components/superAdmin/side-nav/side-nav.component';
import { EmployeeDetailsComponent } from './components/superAdmin/employee-details/employee-details.component';
import { EmployeeProfilesComponent } from './components/superAdmin/employee-profiles/employee-profiles.component';
import { SuperAdminCustomerComponent } from './components/superAdmin/super-admin-customer/super-admin-customer.component';
import { PayemntRisitSuperAdminComponent } from './components/superAdmin/payemnt-risit-super-admin/payemnt-risit-super-admin.component';
import { WebSiteManegingComponent } from './components/superAdmin/web-site-maneging/web-site-maneging.component';
import { SuperAdminSystemIssuesComponent } from './components/superAdmin/super-admin-system-issues/super-admin-system-issues.component';
import { RecoverPasswordComponent } from './components/sign-up/recover-password/recover-password.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { NavbarComponent } from './components/coustomer/navbar/navbar.component';
import { AuthGuard } from './auth/auth.guard';


const routes: Routes = [
  {path:"", redirectTo: 'software', pathMatch: 'full' },
  {path:"not-found",component:NotFoundComponent},
  {path:"superadmin-sign-up",component:SuperAdminComponent},
  {path:"SuperAdmin-login",component:LoginComponent},
  {path: "recover-password", component: RecoverPasswordComponent },
  {path:"coustomer-sign-up/:systemProfilesId",component:CoustomerComponent},
  {path:"employee-sign-up",component:EmployeeComponent},

  {
    path: "software",
    component: NavbarComponent,
    children: [
      {path:"", redirectTo: 'home', pathMatch: 'full' },
      {path:"home",component:HomeComponent},
      {path:"contact-us",component:ContactUsComponent},
      {path:"about-us",component:AboutUsComponent},
      {path:"Categorys/:categoryId",component:CategorysComponent},
      {path:"catageroy-include",component:CategoryIncludeComponent},
      {path:"system-profile/:systemProfilesId",component:SystemPortfoliyoComponent},

    ]
  },

  {
    path: "admin",
    component: SideNavComponent, canActivate: [AuthGuard] , data: { role: 'ROLE_ADMIN' } ,
    children: [
      { path: "", redirectTo: 'super-admin-dash', pathMatch: 'full' },
      { path: "super-admin-dash", component: SuperAdminDashbordComponent },
      { path: "s-employeeDetails", component: EmployeeDetailsComponent },
      { path: "s-employeeProfiles", component: EmployeeProfilesComponent },
      { path: "s-customer", component: SuperAdminCustomerComponent },
      { path: "s-payment-resit", component: PayemntRisitSuperAdminComponent },
      { path: "s-page-maneging", component: WebSiteManegingComponent },
      { path: "s-system-issues", component: SuperAdminSystemIssuesComponent }
    ]
  },
  
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
