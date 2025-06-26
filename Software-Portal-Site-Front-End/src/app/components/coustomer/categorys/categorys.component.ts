import { Component, inject, OnInit, TemplateRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from 'src/app/services/category.service';
import { Category } from 'src/app/shared/category';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SystemProfile } from 'src/app/shared/systemProfile';
import { Video } from 'src/app/shared/video';
import { systemprofiles } from 'src/app/services/system-profiles.service';
import { SystemProfilePhotosService } from 'src/app/services/system-profile-photos.service';
import { SystemProfileVideoService } from 'src/app/services/system-profile-video.service';

@Component({
  selector: 'app-categorys',
  templateUrl: './categorys.component.html',
  styleUrls: ['./categorys.component.css']
})
export class CategorysComponent implements OnInit {

  min: number = 0; // Initial minimum value
  max: number = 1000; // Initial maximum value
  step: number = 1; // Increment s  

  value: number = 0; // Initial slider value
  values: number = 0;
  first: number = 0;
  rows: number = 10;


  collectionSize: number = 0;
  page: number = 0;
  pageSize: number = 10;

  commonStatus: string = '';
  requestStatus : string = '';

  category: Category = {
    categoryId: '',
    categoryName: '',
    categoryDiscription: '',
    commonStatus: ''
  }
  systemProfilepayload: any[] = [];
  combinedProfiles: any[] = [];
  videoUrls: { videoUrl: SafeUrl }[] = [];
  base64Images: string[] = [];
  errorMessage: string = '';
  categoryPayload: Category[] = [];
  inPut : any = '';


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
    systemProfilesPrice:'',
    categoryDto: {
      categoryName: '',
      categoryId: ''
    },
    subCategoryDto: {
      subCategoryName: '',
    },
   
  }
  selectCategory?: String = '';
  categoryId?: any = '';

  video: Video[] = [];
  images: string[] = [];
  systemProfilessId?: String = '';
  videoUrl: SafeUrl | null = null;


  isDrawerOpen = true;
  constructor(private route: ActivatedRoute, private categoryService: CategoryService, private systemProfileservice: systemprofiles, private systemProfileImageService: SystemProfilePhotosService, private profileVideoService: SystemProfileVideoService, private sanitizer: DomSanitizer,private router: Router) {
    this.getByCategoryId();
    // this.getAllSystemProfiles();
    this.getAllCategories();
    this.sendCategoryIdToBackend('APPROVED','ACTIVE');
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.inPut = params['inPut'] || '';
      this.searchBarTop();
    });
  }
 
  onCategoryClick(categoryId: any): void {
    this.categoryId = categoryId;  // Set new category
    this.value = 0;  // Reset slider value to 0
    // this.sendCategoryIdToBackend();  // Fetch data for the selected category
  }
  slider(requestStatus : any,commonStatus : any): void {
    if (this.value === 0 || this.value === null) {
      return; // Do nothing when value is 0 or null
    }
  
    // ✅ Clear previous profile data to prevent duplicates
    this.resetProfileData();
    console.log(`Slider Value Changed: ${this.value}`);
  
    this.systemProfileservice.getAllSystemProfiessliderWith(this.categoryId, this.value,requestStatus,commonStatus, this.page, this.pageSize).subscribe(
      (response) => {
        console.log(this.page);

        if (!response || !response.payload || response.payload.length === 0) {
          console.error("No system profiles found.");
          return;
        }
        this.systemProfilepayload = response.payload[0]; // Store system profiles

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 10; // Number of items per page (should match backend size)

        }
  
        this.systemProfilepayload.forEach((profile) => {
          const systemProfilesId = profile.systemProfilesId;
          console.log(`Fetching data for SystemProfile ID: ${systemProfilesId}`);
  
          // ✅ Check if profile already exists before adding (Prevents duplicates)
          const existingProfile = this.combinedProfiles.find(p => p.systemProfile.systemProfilesId === systemProfilesId);
          if (existingProfile) {
            console.log(`Profile ID ${systemProfilesId} already exists, skipping.`);
            return;
          }
  
          // ✅ Create profile entry with empty placeholders
          const profileEntry = { systemProfile: profile, videoUrl: null, images: [] };
          this.combinedProfiles.push(profileEntry);
  
          // ✅ Fetch video and images for profile
          this.fetchProfileVideo(systemProfilesId, profileEntry);
          this.fetchProfileImages(systemProfilesId, profileEntry);
        });
  
        console.log('Final Combined Profiles:', this.combinedProfiles);
      },
      (error) => {
        this.handleError(error, "Error fetching system profiles");
      }
    );
  }
  
  // ✅ Clear data before fetching new profiles
  private resetProfileData(): void {
    this.systemProfilepayload = [];
    this.combinedProfiles = [];
    this.videoUrls = [];
    this.base64Images = [];
  }
  
  // ✅ Fetch video for a profile while preventing duplicates
  private fetchProfileVideo(systemProfilesId: number, profileEntry: any): void {
    this.profileVideoService.getVideo(systemProfilesId).subscribe(
      (blob) => {
        const objectUrl = URL.createObjectURL(blob);
        profileEntry.videoUrl = objectUrl;
        console.log(`Updated Video for ID ${systemProfilesId}:`, objectUrl);
      },
      (error) => {
        console.error(`Error fetching video for ID ${systemProfilesId}: ${error.message}`);
      }
    );
  }
  
  // ✅ Fetch images for a profile and correctly assign them
  private fetchProfileImages(systemProfilesId: number, profileEntry: any): void {
    this.systemProfileImageService.getImagesallaaaaa(systemProfilesId).subscribe(
      (response) => {
        if (response && response.length > 0) {
          this.base64Images = response; // ✅ Assign images to the correct profile
          profileEntry.images = this.base64Images;

          console.log(`Updated Images for ID ${systemProfilesId}:`, profileEntry.images);
        } else {
          console.warn(`No images found for ID ${systemProfilesId}`);
        }
      },
      (error) => {
        console.error(`Error fetching images for ID ${systemProfilesId}: ${error.message}`);
      }
    );
  }
  
  // ✅ Centralized error handling
  private handleError(error: any, message: string): void {
    this.errorMessage = `${message}: ${error.message}`;
    console.error(this.errorMessage);
  }
  
  
  
  sendCategoryIdToBackend(requestStatus: any, commonStatus: any): void {
    if (!this.categoryId || this.categoryId === '') {
      this.categoryId = this.route.snapshot.paramMap.get('categoryId');
      this.getAllSystemProfiles(this.categoryId);
      console.log(this.categoryId);
      return;
    }
  
    console.log(this.categoryId);
  
    // Reset data before making a new request
    this.systemProfilepayload = [];
    this.combinedProfiles = [];
    this.videoUrls = [];
  
    this.systemProfileservice.getAllSystemProfiesCategoryId(requestStatus, commonStatus, this.categoryId, this.page, this.pageSize).subscribe(
      (response) => {
        if (!response?.payload?.length) {
          console.error("No system profiles found.");
          return;
        }
  
        this.systemProfilepayload = response.payload[0];
  
        if (response.pages?.length > 0) {
          const pagination = response.pages[0];
          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 10; // Items per page (must match backend settings)
        }
  
        // Iterate over each system profile and fetch corresponding video and images
        this.systemProfilepayload.forEach((profile, index) => {
          const systemProfilesId = profile.systemProfilesId;
          console.log(`Fetching data for SystemProfile ID: ${systemProfilesId}`);
  
          // Add an empty entry to combinedProfiles with placeholders
          this.combinedProfiles[index] = {
            systemProfile: profile,
            videoUrl: '',
            images: []
          };
  
          // Fetching video data for each profile
          this.profileVideoService.getVideo(systemProfilesId).subscribe(
            (blob) => {
              const objectUrl = URL.createObjectURL(blob);
              this.combinedProfiles[index].videoUrl = objectUrl; // Assign video URL correctly
              console.log(`Fetched video for ID ${systemProfilesId}:`, objectUrl);
            },
            (error) => {
              console.error(`Error fetching video for ID ${systemProfilesId}: ${error.message}`);
            }
          );
  
          // Fetching images for each profile
          this.systemProfileImageService.getImagesallaaaaa(systemProfilesId).subscribe(
            (response) => {
              if (response && response.length > 0) {
                this.combinedProfiles[index].images = response; // Assign images correctly
                console.log(`Fetched images for ID ${systemProfilesId}:`, this.combinedProfiles[index].images);
              } else {
                console.warn(`No images found for ID ${systemProfilesId}`);
              }
            },
            (error) => {
              console.error(`Error fetching images for ID ${systemProfilesId}: ${error.message}`);
            }
          );
        });
  
        console.log('Final Combined Profiles:', this.combinedProfiles);
      },
      (error) => {
        this.errorMessage = `Error fetching system profiles: ${error.message}`;
        console.error(this.errorMessage);
      }
    );
  }
  


  getByCategoryId() {
    let categoryId = this.route.snapshot.paramMap.get('categoryId');
    console.log(categoryId)
    this.categoryService.getByIdCategory(categoryId).subscribe(
      (response) => {
        this.category = response.payload[0];
      }
    )

  }

  toggleDrawer() {
    this.isDrawerOpen = !this.isDrawerOpen;
  }

  getAllSystemProfiles(categoryId: any) {
    let requestStatus = "APPROVED";
    let commonStatus = "ACTIVE";

    // Fetching system profiles
    this.systemProfileservice.getAllSystemProfiesCategoryId(requestStatus, commonStatus, categoryId,this.page, this.pageSize).subscribe(
      (response) => {
        console.log(response);
        this.systemProfilepayload = response.payload[0];

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems;
          this.pageSize = 10;
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
              console.error(`Error fetching video for ID ${systemProfilesId}: ${error.message}`);
            }
          );

          // Fetching images for each profile
          this.systemProfileImageService.getImagesallaaaaa(systemProfilesId).subscribe(
            (imageResponse) => {
              if (this.combinedProfiles[i]) {
                this.combinedProfiles[i].images = imageResponse; // Ensure profile exists before setting images
                console.log('Updated Combined Profiles with Images:', this.combinedProfiles);
              } else {
                console.warn(`Profile at index ${i} is undefined when setting images.`);
              }
            },
            (error) => {
              console.error(`Error fetching images for ID ${systemProfilesId}: ${error.message}`);
            }
          );
        });
      },
      (error) => {
        this.errorMessage = `Error fetching system profiles: ${error.message}`;
        console.error(this.errorMessage);
      }
    );
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

  getShortDescription(description: string): string {
    if (!description) return ''; // Handle empty descriptions
    let words = description.split(' ');
    return words.length > 70 ? words.slice(0, 70).join(' ') + '...' : description;
  }

  getAllCategories() {
    let commonStatus = "ACTIVE";
    this.categoryService.getAllCategories(commonStatus).subscribe(
      (response) => {
        this.categoryPayload = response.payload[0];
      }
    );
  }
  onPageChange(event: any) {
if(this.commonStatus != '' || this.requestStatus != '' ){
    this.page = event.page; // PrimeNG pages start from 0
    this.pageSize = event.rows;
    this.slider(this.requestStatus,this.commonStatus);
  }else{
    this.page = event.page; // PrimeNG pages start from 0
    this.pageSize = event.rows;
    this.slider(this.requestStatus,this.commonStatus);
  }
}

navigateToProfile(systemProfilesId: any) {
  this.router.navigate(['/software/system-profile', systemProfilesId]);
}

searchBarTop(){
  
  console.log(this.inPut)
  this.systemProfileservice.getSystemProfileSeachBarTop(this. inPut,this.page, this.pageSize).subscribe(
    (response) => {
      console.log(response);
      this.systemProfilepayload = response.payload[0];

      if (response.pages && response.pages.length > 0) {
        const pagination = response.pages[0];

        this.collectionSize = pagination.totalItems;
        this.pageSize = 10;
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
            console.error(`Error fetching video for ID ${systemProfilesId}: ${error.message}`);
          }
        );

        // Fetching images for each profile
        this.systemProfileImageService.getImagesallaaaaa(systemProfilesId).subscribe(
          (imageResponse) => {
            if (this.combinedProfiles[i]) {
              this.combinedProfiles[i].images = imageResponse; // Ensure profile exists before setting images
              console.log('Updated Combined Profiles with Images:', this.combinedProfiles);
            } else {
              console.warn(`Profile at index ${i} is undefined when setting images.`);
            }
          },
          (error) => {
            console.error(`Error fetching images for ID ${systemProfilesId}: ${error.message}`);
          }
        );
      });
    },
    (error) => {
      this.errorMessage = `Error fetching system profiles: ${error.message}`;
      console.error(this.errorMessage);
    }
  );
}




}
