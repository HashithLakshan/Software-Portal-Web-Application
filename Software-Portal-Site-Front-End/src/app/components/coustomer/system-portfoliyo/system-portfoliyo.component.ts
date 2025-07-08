import { Component, AfterViewInit, OnInit, TemplateRef } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { SystemProfileChipsService } from 'src/app/services/system-profile-chips.service';
import { SystemProfileFeaturesService } from 'src/app/services/system-profile-features.service';
import { SystemProfileVideoService } from 'src/app/services/system-profile-video.service';
import { systemprofiles } from 'src/app/services/system-profiles.service';
import { SystemChips } from 'src/app/shared/systemChips';
import { SystemFeatures } from 'src/app/shared/systemFeatures';
import { SystemProfile } from 'src/app/shared/systemProfile';
import { NgbCarouselConfig, NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { inject, } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { SystemProfilePhotosService } from 'src/app/services/system-profile-photos.service';
import { customerToSystemFeedback } from 'src/app/shared/customerToSystemFeedback';
import Swal from 'sweetalert2';
import { CustomerToSystemFeedbackService } from 'src/app/services/customer-to-system-feedback.service';
import { Payment } from 'src/app/shared/payment';
import { PaymentService } from 'src/app/services/payment.service';
import { PdfService } from 'src/app/services/pdf.service';
import { SystemMessageIssues } from 'src/app/shared/systemMessage';
import { SystemIssueMessagesService } from 'src/app/services/system-issue-messages.service';
import { LoadingService } from 'src/app/services/loading.service';

@Component({
  selector: 'app-system-portfoliyo',
  templateUrl: './system-portfoliyo.component.html',
  styleUrls: ['./system-portfoliyo.component.css']
})
export class SystemPortfoliyoComponent implements AfterViewInit {
  values: number = 0;
  cc: boolean = false;
  profileId: string | null = null;
  systemProfileId: string = '';
  perchaseId: string = '';
  selectedFile: File | null = null;

  systemProfile: SystemProfile = {
    systemProfilesId: '',
    systemProfilesName: '',
    systemProfilesDiscription: '',
    commonStatus: '',
    requestStatus: '',
    systemProfilesPrice: '',
    categoryDto: {
      categoryName: ''
    },
    subCategoryDto: {
      subCategoryName: '',
    },
    userDto: {
      commonStatus: '',
      userId: '',
      userName: '',
      email: ''
    }
  }
  customerToSystemFeedbacks: customerToSystemFeedback = {
    ctSfSubject: '',
    ctSfMeesage: '',
    messageStatus: '',
    systemProfileDto: {
      systemProfilesId: '',
    },
    userDto: {
      userId: '',
    }
  }

  issueMessages: SystemMessageIssues = {
    subject: '',
    body: '',
    zoomDto: {
      perchaseId: ''
    }
  }

  payment: Payment = {
    amount: 0,
    quantity: 1,
    name: '',
    currency: 'USD'
  }

  perchaseId1: any = '';
  systemFeatures1: SystemFeatures[] = [];
  systemChips: SystemChips[] = [];
  videoUrl: SafeUrl | null = null;
  showNavigationArrows = false;
  showNavigationIndicators = false;
  images: string[] = [];
  private modalService = inject(NgbModal);
  openFullscreen(content: TemplateRef<any>) {
    this.modalService.open(content, { fullscreen: true });
  }
  constructor(config: NgbModalConfig, private systemProfileService: systemprofiles, private route: ActivatedRoute, private systemProfileFeatureService: SystemProfileFeaturesService, private systemChipsService: SystemProfileChipsService, private profileVideoService: SystemProfileVideoService, private systemImageService: SystemProfilePhotosService, private customerToSystemFeedbackService: CustomerToSystemFeedbackService, private paymentService: PaymentService, private pdfService: PdfService, private issueMessgeService: SystemIssueMessagesService,private loadingService : LoadingService) {
    this.getBySystem();
    this.getBySystemFeatures();
    this.getBySystemChips();
    this.systemVideo();
    this.fetchImages();

  }
  openFirstModal(content1: any) {
    this.modalService.open(content1, { size: 'lg' }).result.then(() => { }, () => { });
  }

  openSecondModal(content2: any) {
    this.modalService.open(content2, { size: 'md' }).result.then(() => { }, () => { });
  }

  openThirdModal(content3: any) {
    this.modalService.open(content3, { size: 'sm' }).result.then(() => { }, () => { });
  }

  getBySystem() {
    let systemProfilesId = this.route.snapshot.paramMap.get('systemProfilesId');
    this.systemProfileService.getBySystemProfile(systemProfilesId).subscribe(
      (response) => {
        this.systemProfile = response.payload[0];
      }
    )
  }

  getBySystemFeatures() {
    let systemProfilesId = this.route.snapshot.paramMap.get('systemProfilesId');
    this.systemProfileFeatureService.getByIdSystemProfileId(systemProfilesId, "ACTIVE").subscribe(
      (response) => {
        this.systemFeatures1 = response.payload[0];
        console.log(this.systemFeatures1)
      }
    )
  }

  getBySystemChips() {
    let systemProfilesId = this.route.snapshot.paramMap.get('systemProfilesId');
    let commonStatus = "ACTIVE";
    this.systemChipsService.getByIdSystemProfileId(systemProfilesId, commonStatus).subscribe(
      (response) => {
        this.systemChips = response.payload[0];
        console.log(this.systemChips)
      }
    )
  }

  systemVideo() {
    let systemProfilesId = this.route.snapshot.paramMap.get('systemProfilesId');
    this.loadingService.show();
    this.profileVideoService.getVideo(systemProfilesId).subscribe(
      (blob) => {
        const objectUrl = URL.createObjectURL(blob);
        this.loadingService.hide();
        this.videoUrl = objectUrl;
      }
    )
  }

  fetchImages() {
    let systemProfilesId = this.route.snapshot.paramMap.get('systemProfilesId');
    this.loadingService.show();
    this.systemImageService.getImagesallaaaaa(systemProfilesId).subscribe(
      (data) => {
        this.loadingService.hide();
        this.images = data;

      },
      (error) => {
        this.loadingService.hide();
        console.error('Error fetching images:', error);
      }
    );
  }


  submitMessage() {
    if (!this.issueMessages.zoomDto) {
      this.issueMessages.zoomDto = {};
    }

    this.issueMessages.zoomDto.perchaseId = this.perchaseId1;
    console.log(this.issueMessages)
    this.issueMessgeService.saver(this.issueMessages).subscribe(
      (response) => {
        if (response.status == true) {
          Swal.fire('', response.commonMessage, "success");
        } else {
          if (response.errorMessages.length > 0) {
            for (let i = 0; i < response.errorMessages.length; i++) {
              Swal.fire('', response.errorMessages[i], "info");

            }

          } else {
            Swal.fire('', response.commonMessage, "info");

          }
        }
      }
    )

  }


  closeModal(modal: any, videoElement: HTMLVideoElement) {
    // Close the modal
    modal.close('Close click');
    // Pause and reset the video
    if (videoElement) {
      videoElement.pause();
      videoElement.currentTime = 0;
    }
  }
  ngAfterViewInit(): void {
    const slides: NodeListOf<HTMLElement> = document.querySelectorAll('.slide');
    let active: HTMLElement | null = document.querySelector('.slide.active');

    slides.forEach((slide: HTMLElement) => {
      slide.onclick = () => {
        if (active) {
          active.classList.remove('active');
        }
        active = slide;
        slide.classList.add('active');
      };
    });
  }

  getPayment() {
    this.loadingService.show();
    this.payment.amount = Number(this.systemProfile.systemProfilesPrice);
    console.log(this.payment.amount)
    if (this.payment.name !== null && this.payment.name !== '') {
      this.paymentService.saveUser(this.payment).subscribe(
        (response) => {
          if (response.status == "SUCCESS") {
            window.open(response.sessionUrl, '_blank');
            this.loadingService.hide();


          } else {
            this.loadingService.hide();

            Swal.fire('', 'payment Fail', 'error')
          }
        }

      );
    } else {
      Swal.fire('', 'Fill in the field', 'info')
    }
  }

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onUploadPdf() {
    let systemProfilesId = this.route.snapshot.paramMap.get('systemProfilesId');
    this.systemProfileId = systemProfilesId ? systemProfilesId : ''; // Handle null case

    // Ensure selectedFile is not null
    if (!this.selectedFile) {
      Swal.fire('Warning', 'Please select a file', 'warning');
      return; // Stop execution if no file is selected
    }

    if (!this.perchaseId) {
      Swal.fire('Warning', 'Please enter a Customer ID', 'warning');
      return;
    }
    console.log(this.systemProfileId)
    console.log(this.perchaseId)
this.loadingService.show();
    this.pdfService.uploadPdf(this.selectedFile, this.systemProfileId, this.perchaseId, 'PENDING').subscribe(
      (response) => {
        if (response.status === true) {
          Swal.fire('', response.commonMessage, "success");
          this.loadingService.hide();
          this.selectedFile = null;
          this.perchaseId = '';
          const fileInput = document.getElementById("fileInput") as HTMLInputElement;
          if (fileInput) {
            fileInput.value = '';
          }

        } else {
          this.loadingService.hide();

          Swal.fire('', response.commonMessage, "info");
        }
      },
      (error) => {
        Swal.fire('Error', 'File upload failed', 'error');
      }
    );
  }

}

