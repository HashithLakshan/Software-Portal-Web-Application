import { Component, OnInit, TemplateRef } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { SystemProfilePhotosService } from 'src/app/services/system-profile-photos.service';
import { SystemProfileVideoService } from 'src/app/services/system-profile-video.service';
import { systemprofiles } from 'src/app/services/system-profiles.service';
import { SystemProfile } from 'src/app/shared/systemProfile';
import { inject, } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Video } from 'src/app/shared/video';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoadingService } from 'src/app/services/loading.service';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  min: number = 0; // Initial minimum value
  max: number = 1000; // Initial maximum value
  step: number = 1; // Increment s   
  value: number = 0; // Initial slider value
  values: number = 0;

  collectionSize: number = 0;
  page: number = 0;
  pageSize: number = 10;

  first: number = 0;  // Starting index (default 0)
  rows: number = 10;  // Number of records per page
  totalRecords: number = 120;  // Total records in the dataset

  
  systemProfilepayload: any[] = [];  
  combinedProfiles: any[] = [];  
  videoUrls: { videoUrl: SafeUrl }[] = [];  
  base64Images: string[] = [];  
  errorMessage: string = '';  
  inPut: string = '';  

  private modalService = inject(NgbModal);
  openFullscreen(content: TemplateRef<any>) {
  this.modalService.open(content, { fullscreen: true });
  }
  systemProfile: SystemProfile = {
    systemProfilesId: '',
    systemProfilesName: '',
    systemProfilesDiscription: '',
    commonStatus: '',
    requestStatus: '',
    categoryDto: {
      categoryName: '',
      categoryId:''
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

  video: Video[] = [];
  images: string[] = [];
  systemProfilessId?: String = '';
  videoUrl: SafeUrl | null = null;
  ;



  constructor(private systemProfileservice: systemprofiles, private systemProfileImageService: SystemProfilePhotosService, private profileVideoService: SystemProfileVideoService, private sanitizer: DomSanitizer,private route: ActivatedRoute,private messageService: MessageService,private router: Router, private loadService : LoadingService) {
    this.getAllSystemProfiles('APPROVED','ACTIVE');
    

  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.inPut = params['inPut'] || ''; // Ensure default value if undefined
      this.search();
    });

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



  getAllSystemProfiles(commonStatus: any, requestStatus: any) {
    this.loadService.show();
    this.systemProfileservice
      .getSystemProfileFilterStatus(String(commonStatus), String(requestStatus), this.page , this.pageSize)
      .subscribe(
        (response) => {

          if(response.payload !=  ''){
          

          console.log(response);
          this.systemProfilepayload = response.payload[0];
  
          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];
  
            this.collectionSize = pagination.totalItems;
            this.pageSize = 10;
          }
        }else{
          this.loadService.hide();

        }
          this.combinedProfiles = []; // Ensure the array is initialized
  
          this.systemProfilepayload.forEach((profile, i) => {
            const systemProfilesId = profile.systemProfilesId;
            console.log(`Fetching data for SystemProfile ID: ${systemProfilesId}`);
  
            // Initialize profile in combinedProfiles first
            this.combinedProfiles[i] = { systemProfile: profile, videoUrl: '', images: [] };
  
            // Fetching video data for each profile
            this.profileVideoService.getVideo(systemProfilesId).subscribe(
              (blob) => {
                const objectUrl = URL.createObjectURL(blob);
                this.combinedProfiles[i].videoUrl = objectUrl;
                console.log('Updated Combined Profiles with Video:', this.combinedProfiles);
              },
              (error) => {
                this.loadService.hide();
                console.error(`Error fetching video for ID ${systemProfilesId}: ${error.message}`);
              }
            );
  
            this.systemProfileImageService.getImagesallaaaaa(systemProfilesId).subscribe(
              (imageResponse) => {
                if (this.combinedProfiles[i]) {
                  this.loadService.hide();
                  this.combinedProfiles[i].images = imageResponse;
                  console.log('Updated Combined Profiles with Images:', this.combinedProfiles);
                } else {
                  this.loadService.hide();
                  console.warn(`Profile at index ${i} is undefined when setting images.`);
                }
              },
              (error) => {
                this.loadService.hide();
                console.error(`Error fetching images for ID ${systemProfilesId}: ${error.message}`);
              }
            );
          });
        },
        (error) => {
          this.loadService.hide();
          this.errorMessage = `Error fetching system profiles: ${error.message}`;
          console.error(this.errorMessage);
        }
      );
  }
  

  updateRange(): void {
    if (this.value < this.min) {
      this.value = this.min;
    } else if (this.value > this.max) {
      this.value = this.max;
    }
  }
  onPageChange(event: any) {
    console.log('Page event:', event);
    this.page = event.page; // PrimeNG pages start from 0
    this.pageSize = event.rows;
    this.getAllSystemProfiles('APPROVED','ACTIVE');

  }

 cc(){
  this.router.navigate(['/software'], { replaceUrl: true }); // Navigates to /home without parameters

 }



  search() {
   if(this.inPut == '' || this.inPut == null){
    
   }else{
    
    console.log(this.inPut)
    this.loadService.show();
    this.systemProfileservice
      .getSystemProfileSeachBarTop(this.inPut, this.page , this.pageSize)
      .subscribe(
        (response) => {
          if(response.status == true){
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          console.log(response);
          this.systemProfilepayload = response.payload[0];
  
          if (response.pages && response.pages.length > 0) {
            const pagination = response.pages[0];
  
            this.collectionSize = pagination.totalItems;
            this.pageSize = 10;
          }
          this.router.navigate(['/software'], { replaceUrl: true }); // Navigates to /home without parameters

          
        }else{
          this.loadService.hide();
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
          this.router.navigate(['/software'], { replaceUrl: true }); // Navigates to /home without parameters

        }
          this.combinedProfiles = []; // Ensure the array is initialized
  
          this.systemProfilepayload.forEach((profile, i) => {
            const systemProfilesId = profile.systemProfilesId;
            console.log(`Fetching data for SystemProfile ID: ${systemProfilesId}`);
  
            // Initialize profile in combinedProfiles first
            this.combinedProfiles[i] = { systemProfile: profile, videoUrl: '', images: [] };
  
            // Fetching video data for each profile
            this.profileVideoService.getVideo(systemProfilesId).subscribe(
              (blob) => {
                const objectUrl = URL.createObjectURL(blob);
                this.combinedProfiles[i].videoUrl = objectUrl;
                console.log('Updated Combined Profiles with Video:', this.combinedProfiles);
              },
              (error) => {
                this.loadService.hide();
                console.error(`Error fetching video for ID ${systemProfilesId}: ${error.message}`);
              }
            );
  
            // Fetching images for each profile
            this.systemProfileImageService.getImagesallaaaaa(systemProfilesId).subscribe(
              (imageResponse) => {
                if (this.combinedProfiles[i]) {
                  this.loadService.hide();
                  this.combinedProfiles[i].images = imageResponse; // Ensure profile exists before setting images
                  console.log('Updated Combined Profiles with Images:', this.combinedProfiles);
                } else {
                  this.loadService.hide();
                  console.warn(`Profile at index ${i} is undefined when setting images.`);
                }
              },
              (error) => {
                this.loadService.hide();
                console.error(`Error fetching images for ID ${systemProfilesId}: ${error.message}`);
              }
            );
          });
        },
        (error) => {
          this.loadService.hide();
          this.errorMessage = `Error fetching system profiles: ${error.message}`;
          console.error(this.errorMessage);
        }
      );
    }

  }
  }
