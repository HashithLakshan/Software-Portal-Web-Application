import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatMenuModule} from '@angular/material/menu';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatBadgeModule} from '@angular/material/badge';
import { BadgeModule } from 'primeng/badge';
import {MatSliderModule} from '@angular/material/slider';
import {MatCardModule} from '@angular/material/card';
import { SliderModule } from 'primeng/slider';
import { RatingModule } from 'primeng/rating';
import { PaginatorModule } from 'primeng/paginator';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatExpansionModule} from '@angular/material/expansion';
import { ChipModule } from 'primeng/chip';
import { GalleriaModule } from 'primeng/galleria';
import { SidebarModule } from 'primeng/sidebar';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import {MatStepperModule} from '@angular/material/stepper';
import {MatTabsModule} from '@angular/material/tabs';
import { TagModule } from 'primeng/tag';
import {MatPaginatorModule} from '@angular/material/paginator';
import { ToastModule } from 'primeng/toast';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { DialogModule } from 'primeng/dialog'; 
import { ImageModule } from 'primeng/image';



import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ButtonModule } from 'primeng/button';
import { MegaMenuModule } from 'primeng/megamenu';
import { HomeComponent } from './components/coustomer/home/home.component';
import { CoustomerComponent } from './components/sign-up/coustomer/coustomer.component';
import { EmployeeComponent } from './components/sign-up/employee/employee.component';
import { LoginComponent } from './components/login/login.component';
import { NavbarComponent } from './components/coustomer/navbar/navbar.component';
import {  FormsModule } from '@angular/forms';
import { CategorysComponent } from './components/coustomer/categorys/categorys.component';
import { CategoryIncludeComponent } from './components/coustomer/category-include/category-include.component';
import { FooterComponent } from './components/coustomer/footer/footer.component';
import { AboutUsComponent } from './components/coustomer/about-us/about-us.component';
import { ContactUsComponent } from './components/coustomer/contact-us/contact-us.component';
import { SystemPortfoliyoComponent } from './components/coustomer/system-portfoliyo/system-portfoliyo.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SuperAdminDashbordComponent } from './components/superAdmin/super-admin-dashbord/super-admin-dashbord.component';
import { SuperAdminComponent } from './components/sign-up/super-admin/super-admin.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { SideNavComponent } from './components/superAdmin/side-nav/side-nav.component';
import { SuperAdminTopComponent } from './components/superAdmin/super-admin-top/super-admin-top.component';
import { EmployeeDetailsComponent } from './components/superAdmin/employee-details/employee-details.component';
import { ConfirmationService, MessageService } from 'primeng/api';
import { EmployeeProfilesComponent } from './components/superAdmin/employee-profiles/employee-profiles.component';
import { SuperAdminCustomerComponent } from './components/superAdmin/super-admin-customer/super-admin-customer.component';
import { PayemntRisitSuperAdminComponent } from './components/superAdmin/payemnt-risit-super-admin/payemnt-risit-super-admin.component';
import { WebSiteManegingComponent } from './components/superAdmin/web-site-maneging/web-site-maneging.component';
import { SuperAdminSystemIssuesComponent } from './components/superAdmin/super-admin-system-issues/super-admin-system-issues.component';
import { RecoverPasswordComponent } from './components/sign-up/recover-password/recover-password.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { AuthInterceptor } from './auth/auth.interceptor';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    CoustomerComponent,
    EmployeeComponent,
    LoginComponent,
    CategorysComponent,
    CategoryIncludeComponent,
    FooterComponent,
    AboutUsComponent,
    ContactUsComponent,
    SystemPortfoliyoComponent,
    SuperAdminDashbordComponent,
    SuperAdminComponent,
    SideNavComponent,
    SuperAdminTopComponent,
    EmployeeDetailsComponent,
    EmployeeProfilesComponent,
    SuperAdminCustomerComponent,
    PayemntRisitSuperAdminComponent,
    WebSiteManegingComponent,
    SuperAdminSystemIssuesComponent,
    RecoverPasswordComponent,
    NotFoundComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ButtonModule,
    MegaMenuModule,
    MatMenuModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatBadgeModule,
    BadgeModule,
    MatSliderModule,
    MatCardModule,
    SliderModule,
    FormsModule,
    RatingModule,
    PaginatorModule,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatExpansionModule,
    ChipModule,
    GalleriaModule,
    SidebarModule,
    NgbModule  ,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    InputTextModule,
    InputTextareaModule,
    MatStepperModule,
    MatTabsModule,
    TagModule,
    MatPaginatorModule,
    ToastModule,
    ConfirmPopupModule,
    DialogModule,
    ImageModule

   
    
  ],
  providers: [
    MessageService,
    ConfirmationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
