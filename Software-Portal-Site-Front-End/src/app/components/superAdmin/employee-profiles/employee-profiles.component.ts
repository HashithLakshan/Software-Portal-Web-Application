
import { Component, inject, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoryService } from 'src/app/services/category.service';
import { SubCategoryService } from 'src/app/services/sub-category.service';
import { SystemProfileChipsService } from 'src/app/services/system-profile-chips.service';
import { SystemProfileFeaturesService } from 'src/app/services/system-profile-features.service';
import { SystemProfilePhotosService } from 'src/app/services/system-profile-photos.service';
import { SystemProfileVideoService } from 'src/app/services/system-profile-video.service';
import { systemprofiles } from 'src/app/services/system-profiles.service';
import { Category } from 'src/app/shared/category';
import { SubCategory } from 'src/app/shared/subCategory';
import { SystemProfile } from 'src/app/shared/systemProfile';
import { ConfirmationService, MessageService } from 'primeng/api';
import Swal from 'sweetalert2';
import { SystemFeatures } from 'src/app/shared/systemFeatures';
import { SystemChips } from 'src/app/shared/systemChips';
import { Images } from 'src/app/shared/image';

import { NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { LoadingService } from 'src/app/services/loading.service';

@Component({
  selector: 'app-employee-profiles',
  templateUrl: './employee-profiles.component.html',
  styleUrls: ['./employee-profiles.component.css']
})
export class EmployeeProfilesComponent {
  collectionSize: number = 0;
  page: number = 1;
  pageSize: number = 5;

  collectionSize1: number = 0;
  page1: number = 1;
  pageSize1: number = 5;

  collectionSize2: number = 0;
  page2: number = 1;
  pageSize2: number = 5;

  systemProfile: SystemProfile = {
    systemProfilesId: '',
    systemProfilesName: '',
    systemProfilesDiscription: '',
    commonStatus: 'ACTIVE',
    requestStatus: 'APPROVED',
    categoryDto: {
      categoryName: '',
      categoryId: "",
    },
    subCategoryDto: {
      subCategoryName: '',
      subCategoryId: '',
    },
    employeeDto: {
      employeeId: '',
      employeeNumber: '',
      companyName: '',
      employeeNIC: '',
      commonStatus: '',
      companyRgNo: '',
    },
    systemProfilesPrice: '',
  }


  systemProfileUpdate: SystemProfile = {
    systemProfilesId: '',
    systemProfilesName: '',
    systemProfilesDiscription: '',
    commonStatus: '',
    requestStatus: '',
    categoryDto: {
      categoryName: '',
      categoryId: "",
    },
    subCategoryDto: {
      subCategoryName: '',
      subCategoryId: '',
    },
    employeeDto: {
      employeeId: '',
      employeeNumber: '',
      companyName: '',
      employeeNIC: '',
      commonStatus: '',
      companyRgNo: '',
    },
    systemProfilesPrice: '',
  }


  features: SystemFeatures = {
    systemFeatureId: '',
    systemFeatureDiscripion: '',
    commonStatus: 'ACTIVE',
    systemProfileDto: {
      systemProfilesId: ''
    }

  }


  featuresUpdate: SystemFeatures = {
    systemFeatureId: '',
    systemFeatureDiscripion: '',
    commonStatus: 'ACTIVE',
    systemProfileDto: {
      systemProfilesId: ''
    }

  }

  technolofies: SystemChips = {
    systemProfileChipId: '',
    chipName: '',
    systemProfileDto: {
      systemProfilesId: ''
    }

  }

  technolofiesUpdate: SystemChips = {
    systemProfileChipId: '',
    chipName: '',
    systemProfileDto: {
      systemProfilesId: ''
    }

  }

  Super: String = '';
  image1: any[] = [];

  sysytmProfileview: SystemProfile[] = [];
  technolofiesget: SystemChips[] = [];
  systemFeatures: SystemFeatures[] = [];
  private modalService = inject(NgbModal);
  category: Category[] = [];
  SubCategory: SubCategory[] = [];
  systemProfilePending: SystemProfile[] = [];
  systemProfileApprove: SystemProfile[] = [];
  sysytmProfileInActive: SystemProfile[] = [];
  sysytmProfileBack: SystemProfile[] = [];
  categoryValue: String = '';
  SubaCategoryValue: String = '';
  categoryValueUpdate: String = '';
  SubaCategoryValueUpdate: String = '';
  searchInput: String = '';
  dropDown: String = '';
  videoUrl: string | null = null;
  selectedFile: File | null = null;
  employeeId: String = '';
  proImg: File | null = null;
  proImgUpdate: File | null = null;

  previewUrl: string | ArrayBuffer | null = null;
  previewUrlUpdate: string | ArrayBuffer | null = null;

  images: string[] = [];



  constructor(private systemProfileService: systemprofiles, private messageService: MessageService, private categoryService: CategoryService,
    private subCategoryService: SubCategoryService, private systmChipsService: SystemProfileChipsService, config: NgbModalConfig,
    private systmFetauresService: SystemProfileFeaturesService, private sysytemImageService: SystemProfilePhotosService, private loadingService: LoadingService,
    private systemVideoService: SystemProfileVideoService) {

    this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
    this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
    this.getAllFilterSystemProfileInactive("INACTIVE");
    this.getAllCategories("ACTIVE");
    this.getAllSubCategory("ACTIVE");
    config.backdrop = 'static';  // Prevent closing when clicking outside
    config.keyboard = false;     // Prevent closing with ESC key

  }

  openLg(content: TemplateRef<any>) {
    this.modalService.open(content, { size: 'xl' });
  }

  openXl(content: TemplateRef<any>) {
    this.modalService.open(content, { size: 'xl' });
  }

  onPageChange(page: number) {
    if (this.Super != 'present') {
      this.page = page;
      this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
    } else {
      this.page = page;
      this.getAllFilterSystemProfileAllSuperAdminApprove("ACTIVE", "APPROVED");
    }
  }


  onPageChange2(page: number) {
    this.page1 = page;
    this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
  }
  onPageChange3(page: number) {
    if (this.Super != 'present') {
      this.page2 = page;
      this.getAllFilterSystemProfileInactive("INACTIVE");
    } else {
      this.page2 = page;
      this.getAllFilterSystemProfileAllSuperAdminInactive("INACTIVE");
    }
  }
  onVideoSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];

    if (file) {
      this.selectedFile = file;
      this.videoUrl = URL.createObjectURL(file);
    }
  }

  getAllFilterSystemProfileApprove(commonStatus: String, requestStatus: String) {
    this.systemProfileService.getSystemProfileFilterStatus(String(commonStatus), String(requestStatus), this.page - 1, this.pageSize).subscribe(
      (response) => {
        this.systemProfileApprove = response.payload[0];
        console.log(this.systemProfileApprove)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 10; // Number of items per page (should match backend size)

        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );
  }


  getAllFilterSystemProfilePending(commonStatus: String, requestStatus: String) {
    this.systemProfileService.getSystemProfileFilterStatus(String(commonStatus), String(requestStatus), this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.systemProfilePending = response.payload[0];
        console.log(this.systemProfilePending)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize1 = pagination.totalItems; // Total number of items
          this.pageSize1 = 10; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );

  }

  getAllFilterSystemProfileInactive(commonStatus: String) {
    this.systemProfileService.getSystemProfileFilterStatus("", String(commonStatus), this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        this.sysytmProfileInActive = response.payload[0];
        console.log(this.systemProfileApprove)
        console.log(this.systemProfileApprove)


        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize2 = pagination.totalItems; // Total number of items
          this.pageSize2 = 10; // Number of items per page (should match backend size)

          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );
  }


  getAllCategories(commonStatus: String) {
    this.categoryService.getAllCategories(commonStatus).subscribe(
      (response) => {
        this.category = response.payload[0];
        console.log(this.category)
      }
    )
  }

  getAllSubCategory(commonStatus: String) {
    this.subCategoryService.getAllSubCategories(commonStatus).subscribe(
      (response) => {
        this.SubCategory = response.payload[0];
      }
    )
  }




  saveProfile() {



    function generateRandomId(): string {
      return Math.floor(100000 + Math.random() * 900000).toString(); // 6-digit random ID
    }
    if (!this.systemProfile.categoryDto) {
      this.systemProfile.categoryDto = {}; // Ensure categoryDto is initialized
    }
    this.systemProfile.categoryDto.categoryId = this.categoryValue;

    if (!this.systemProfile.subCategoryDto) {
      this.systemProfile.subCategoryDto = {}; // Ensure subCategoryDto is initialized
    }
    this.systemProfile.subCategoryDto.subCategoryId = this.categoryValue;

    if (!this.systemProfile.employeeDto) {
      this.systemProfile.employeeDto = {}; // Ensure categoryDto is initialized
    }
    this.systemProfile.employeeDto.employeeId = this.employeeId;
    console.log(this.systemProfile)

    this.systemProfile.systemProfilesId = generateRandomId();
    const payload = {

      "systemProfilesId": this.systemProfile.systemProfilesId,
      "systemProfilesName": this.systemProfile.systemProfilesName,
      "systemProfilesPrice": this.systemProfile.systemProfilesPrice,
      "systemProfilesDiscription": this.systemProfile.systemProfilesDiscription,
      "employeeDto": {
        "employeeId": this.systemProfile.employeeDto.employeeId,
      },
      "categoryDto": {
        "categoryId": this.systemProfile.categoryDto.categoryId,

      },
      "subCategoryDto": {
        "subCategoryId": this.systemProfile.subCategoryDto.subCategoryId,

      },
      "commonStatus": "ACTIVE",
      "requestStatus": "PENDING"

    }
    if (this.systemProfile.employeeDto.employeeId != null || this.systemProfile.employeeDto.employeeId != '') {
      payload.employeeDto.employeeId = this.systemProfile.employeeDto.employeeId;
    }

    this.systemProfileService.saveProfile(payload).subscribe(
      (response) => {
        console.log(response);
        if (response.status === true) {
          if (!this.selectedFile) {
            alert('Please select a video first!');
            return;
          }


          if (this.selectedFile != null) {
            this.loadingService.show();

            this.systemVideoService.uploadVideo(this.selectedFile, String(payload.systemProfilesId)).subscribe(
              (response) => {
                if (response.status === true) {
                  this.loadingService.hide();

                  this.messageService.add({ severity: 'success', summary: 'success', detail: "First Step is Complete" });
                  this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");

                } else {
                  this.loadingService.hide();

                  this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

                }
              },
              (err) => {
                console.error('Upload failed', err);
                alert('Video upload failed!');
              }
            );
          } else {
            this.messageService.add({ severity: 'info', summary: 'info', detail: 'Please select a video first!' });

          }


        } else {
          if (response.errorMessages) {
            for (let x = 0; x < response.errorMessages.length; x++) {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[x] });
            }
          }
          if (response.commonMessage) {
            this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
          }


        }
      }

    )
  }



  systemPictureUpload() {
    const payload = {
      image: this.proImg,
      systemProfileId: this.systemProfile.systemProfilesId
    }
    if (payload.systemProfileId == "") {
      payload.systemProfileId = this.systemProfileUpdate.systemProfilesId;
    }
    console.log(payload)
    if (payload.image != null) {
      this.loadingService.show();

      this.sysytemImageService.uploadImgs(payload).subscribe(
        (response) => {
          if (response.status === true) {
            this.loadingService.hide();
            this.removeFile();
            this.fetchImages(payload.systemProfileId);
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          } else {
            this.loadingService.hide();

            this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
          }
        }
      )
    } else {
      this.messageService.add({ severity: 'info', summary: 'info', detail: "Please provide an image file" });

    }
  }

  systemPictureUploadnew(x: any) {
    const payload = {
      image: this.proImg,
      systemProfileId: this.systemProfile.systemProfilesId
    }
    if (payload.systemProfileId == "") {
      payload.systemProfileId = this.systemProfileUpdate.systemProfilesId;
    }
    console.log(payload)
    if (payload.image != null) {
      this.loadingService.show();

      this.sysytemImageService.uploadImgs(payload).subscribe(
        (response) => {
          if (response.status === true) {
            this.loadingService.hide();
            this.removeFile();
            this.fetchImages(payload.systemProfileId);
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          } else {
            this.loadingService.hide();

            this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
          }
        }
      )
    } else {
      this.messageService.add({ severity: 'info', summary: 'info', detail: "Please provide an image file" });

    }
  }

  onFileSelected(event: any): void {
    this.proImg = event.target.files[0];

    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      const reader = new FileReader();

      // Read the file as a data URL
      reader.onload = () => {
        this.previewUrl = reader.result;
      };

      reader.readAsDataURL(file); // Trigger the file read
    }
  }
  fetchImages(systemProfileId: any): void {
    console.log(systemProfileId)
    this.sysytemImageService.getImages(systemProfileId).subscribe(
      (response) => {
        this.image1 = response;  // Store response in image1
      },
      (error) => {
        console.error('Error fetching images:', error);
      }
    );
  }


  removeFile(): void {
    this.proImg = null;
    this.previewUrl = null;
    const fileInput = document.getElementById('file') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }


  saveFeatures() {
    if (this.featuresUpdate.systemFeatureDiscripion != '') {
      this.featuresUpdate.systemProfileDto = this.featuresUpdate.systemProfileDto || {};
      this.featuresUpdate.systemProfileDto.systemProfilesId = this.systemProfileUpdate.systemProfilesId;
      this.systmFetauresService.saveFeatures(this.featuresUpdate).subscribe(
        (response) => {
          if (response.status === true) {
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getBySystemFeatures(this.systemProfileUpdate.systemProfilesId);

          } else {
            if (response.errorMessages) {
              for (let x = 0; x < response.errorMessages.length; x++) {
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[x] });
              }
            }
            if (response.commonMessage) {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
            }

          }
        }
      )
    } else {

      this.features.systemProfileDto = this.features.systemProfileDto || {};
      this.features.systemProfileDto.systemProfilesId = this.systemProfile.systemProfilesId;
      let systemProfilesId = this.features.systemProfileDto.systemProfilesId;
      console.log(this.features)
      this.systmFetauresService.saveFeatures(this.features).subscribe(
        (response) => {
          if (response.status === true) {
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getBySystemFeatures(systemProfilesId);

          } else {
            if (response.errorMessages) {
              for (let x = 0; x < response.errorMessages.length; x++) {
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[x] });
              }
            }
            if (response.commonMessage) {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
            }

          }
        }
      )
    }
  }

  getBySystemFeatures(systemProfileId: any) {
    this.systmFetauresService.getByIdSystemProfileId(systemProfileId, "ACTIVE").subscribe(
      (response) => {
        this.systemFeatures = response.payload[0];
      }
    )
  }

  saveChips() {

    if (this.technolofiesUpdate.chipName != '') {
      this.technolofiesUpdate.systemProfileDto = this.technolofiesUpdate.systemProfileDto || {};
      this.technolofiesUpdate.systemProfileDto.systemProfilesId = this.systemProfileUpdate.systemProfilesId;
      console.log(this.technolofiesUpdate)
      console.log(this.systemProfileUpdate.systemProfilesId)

      this.systmChipsService.saveChips(this.technolofiesUpdate).subscribe(
        (response) => {
          if (response.status === true) {
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getBySystemChips(this.systemProfileUpdate.systemProfilesId);
          } else {
            if (response.errorMessages) {
              for (let x = 0; x < response.errorMessages.length; x++) {
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[x] });
              }
            }
            if (response.commonMessage) {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
            }

          }
        }
      )
    } else {
      this.technolofies.systemProfileDto = this.technolofies.systemProfileDto || {};
      this.technolofies.systemProfileDto.systemProfilesId = this.systemProfile.systemProfilesId;
      let systemProfilesId = this.technolofies.systemProfileDto.systemProfilesId;
      console.log("hello")
      console.log(this.technolofies)

      this.systmChipsService.saveChips(this.technolofies).subscribe(
        (response) => {
          if (response.status === true) {
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getBySystemChips(systemProfilesId);

          } else {
            if (response.errorMessages) {
              for (let x = 0; x < response.errorMessages.length; x++) {
                this.messageService.add({ severity: 'info', summary: 'info', detail: response.errorMessages[x] });
              }
            }
            if (response.commonMessage) {
              this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
            }

          }
        }
      )
    }
  }

  getBySystemChips(systemProfileId: any) {
    this.systmChipsService.getByIdSystemProfileId(systemProfileId, "ACTIVE").subscribe(
      (response) => {
        console.log(response.payload)
        this.technolofiesget = response.payload[0];
      }
    )
  }

  turnInctive(systemProfileId: any) {
    this.systemProfileService.updateStatusInactive(systemProfileId).subscribe(
      (response) => {
        if (response.status === true) {
          console.log(response);
          // Swal.fire ('',response.commonMessage,'success')
          this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
          this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
          this.getAllFilterSystemProfileInactive("INACTIVE");

          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        } else {
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
    )
  }


  turnActive(systemProfileId: any) {
    this.systemProfileService.updateStatusActive(systemProfileId).subscribe(
      (response) => {
        if (response.status === true) {
          console.log(response);
          // Swal.fire ('',response.commonMessage,'success')
          this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
          this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
          this.getAllFilterSystemProfileInactive("INACTIVE");

          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        } else {
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
    )
  }

  turnApprove(systemProfileId: any) {
    this.systemProfileService.updateStatusApprove(systemProfileId).subscribe(
      (response) => {
        if (response.status === true) {
          console.log(response);
          // Swal.fire ('',response.commonMessage,'success')
          this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
          this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
          this.getAllFilterSystemProfileInactive("INACTIVE");

          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        } else {
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
    )
  }

  confirmDelete(systemProfileId: any) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You want to permenet delete this record',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.systemProfileService.deletePermanet(systemProfileId).subscribe(
          (response) => {
            if (response.status === true) {
              console.log(response);
              // Swal.fire ('',response.commonMessage,'success')
              this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
              this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
              this.getAllFilterSystemProfileInactive("INACTIVE");

              this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


            } else {
              this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

              // Swal.fire('',response.commonMessage,'error')
            }
          }
        )

      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Swal.fire(
        //   'Cancelled',
        //   'Your employee record is safe',
        //   'info'
        // );
        this.messageService.add({ severity: 'info', summary: 'info', detail: "Your employee record is safe" });


      }
    });
  }

  DeleteFeature(systemFeatureId: any, systemProfileId: any) {
    this.systmFetauresService.deleteFeature(systemFeatureId).subscribe(
      (response) => {
        if (response.status === true) {
          console.log(systemProfileId);
          this.getBySystemFeatures(systemProfileId);
          this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
          this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");

          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        } else {
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
    )
  }

  DeleteChips(systemProfileChipId: any, systemProfileId: any) {
    this.systmChipsService.deleteChips(systemProfileChipId).subscribe(
      (response) => {
        if (response.status === true) {
          this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
          this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
          console.log(systemProfileId);

          this.getBySystemChips(systemProfileId);
          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });


        } else {
          this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });

          // Swal.fire('',response.commonMessage,'error')
        }
      }
    )
  }


  onViewBtn(systemData: SystemProfile, index: number) {
    this.systemProfileUpdate = systemData;
    console.log(systemData)
    this.categoryValueUpdate = String(systemData.categoryDto?.categoryId);
    this.SubaCategoryValueUpdate = String(systemData.subCategoryDto?.subCategoryName);
    console.log(this.SubaCategoryValue)
  }


  systemVideo(systemProfilesId: any) {
    this.loadingService.show();
    this.systemVideoService.getVideo(systemProfilesId).subscribe(
      (blob) => {
        const objectUrl = URL.createObjectURL(blob);
        this.loadingService.hide();
        this.videoUrl = objectUrl;
      },
      (err) => {
        this.loadingService.hide();
        this.messageService.add({ severity: 'error', summary: 'error', detail: "refreshy and try again" });

      }
    )
  }

  updateProfile() {
    const payload = {

      "systemProfilesId": this.systemProfileUpdate.systemProfilesId,
      "systemProfilesName": this.systemProfileUpdate.systemProfilesName,
      "systemProfilesPrice": this.systemProfileUpdate.systemProfilesPrice,
      "systemProfilesDiscription": this.systemProfileUpdate.systemProfilesDiscription,
      "employeeDto": {
        "employeeId": '',
      },
      "categoryDto": {
        "categoryId": this.categoryValueUpdate,

      },
      "subCategoryDto": {
        "subCategoryId": this.SubaCategoryValueUpdate,

      },
      "commonStatus": "ACTIVE",
      "requestStatus": "APPROVED"

    }
console.log(payload)

    this.systemProfileService.UpdateProfile(payload).subscribe(
      (response) => {
        if (response.status === true) {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: response.commonMessage });

        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

        }
      }
    )
  }

  videoUpload(systemProfilesId: any) {
    if (!this.selectedFile) {
      alert('Please select a video first!');
      return;
    }


    if (this.selectedFile != null) {
      this.loadingService.show();
      this.systemVideoService.uploadVideo(this.selectedFile, String(systemProfilesId)).subscribe(
        (response) => {
          if (response.status === true) {
            this.loadingService.hide();
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");


          } else {
            this.loadingService.hide();
            this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });

          }
        },
        (err) => {
          this.loadingService.hide();
          this.messageService.add({ severity: 'error', summary: 'error', detail: "refreshy and try again" });

        }
      );
    } else {
      this.loadingService.hide();
      this.messageService.add({ severity: 'info', summary: 'info', detail: 'Please select a video first!' });

    }
  }

  clearVideoAndImages() {
    this.removeFile();
    this.videoUrl = null;

  }

  clearDta() {
    this.removeFile();
    this.systemFeatures.splice(0, this.systemFeatures.length);
    this.technolofiesget.splice(0, this.technolofiesget.length);

    this.videoUrl = null;
    this.systemProfile.systemProfilesId = '';
    this.systemProfile.systemProfilesDiscription = '';
    this.systemProfile.systemProfilesName = '';
    this.systemProfile.systemProfilesPrice = '';
    this.systemProfile.categoryDto = this.systemProfile.categoryDto || {};
    this.systemProfile.categoryDto.categoryId = '';
    this.systemProfile.employeeDto = this.systemProfile.employeeDto || {};
    this.systemProfile.employeeDto.employeeId = '';

    this.image1.splice(0, this.image1.length);


  }
  checkIfNumber(input: string): boolean {
    return /^\d+$/.test(input); // Returns true if input contains only digits (0-9)
  }

  search(commonStatus: any, requestStatus: any) {

    let x = this.checkIfNumber(String(this.searchInput));

    if (this.searchInput === null || this.searchInput === '') {
      this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Search fieeld are empty' });

    } else {
      // if (x === true) {

      switch (this.dropDown) {
        case "System ID":
          console.log(commonStatus)
          console.log(requestStatus)
          console.log(this.searchInput)

          if (x === true) {

            this.systemProfileService.geSystemFilterId(commonStatus, requestStatus, this.searchInput).subscribe(
              (response) => {
                if (response.status == true) {
                  if (commonStatus === "INACTIVE") {
                    this.sysytmProfileInActive = response.payload;
                  } else {
                    if (commonStatus === "ACTIVE" && requestStatus === "APPROVED") {
                      this.systemProfileApprove = response.payload;
                    }
                    if (commonStatus === "ACTIVE" && requestStatus === "PENDING") {
                      this.systemProfilePending = response.payload;
                    }
                  }
                } else {
                  this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
                  // Swal.fire('',response.commonMessage,'info');
                }
              }
            );
          } else {
            this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });

          }
          break;
        case "Employee ID":
          console.log(requestStatus)
          console.log(commonStatus)
          if (x === true) {

            this.systemProfileService.getEmployeesFilterId(commonStatus, requestStatus, this.searchInput).subscribe(
              (response) => {
                if (response.status == true) {
                  if (commonStatus === "INACTIVE") {
                    this.sysytmProfileInActive = response.payload[0];
                  } else {
                    if (commonStatus === "ACTIVE" && requestStatus === "APPROVED") {
                      this.systemProfileApprove = response.payload[0];
                      console.log(response.payload)
                    }
                    if (commonStatus === "ACTIVE" && requestStatus === "PENDING") {
                      this.systemProfilePending = response.payload[0];
                    }
                  }
                } else {
                  this.messageService.add({ severity: 'error', summary: 'error', detail: response.commonMessage });
                  // Swal.fire('',response.commonMessage,'info');
                }
              }
            );
          } else {
            this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });

          }
          break;
        default:
          this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Select the search type' });
        // Swal.fire ('','Select the search type','info')
      }
      // } else {
      //   this.messageService.add({ severity: 'info', summary: 'Info', detail: 'Not enter Words enter numbers only' });

      // }
    }
  }

  onTabClick(event: any) {
    this.clearData();
    this.getAllFilterSystemProfileApprove("APPROVED", "ACTIVE");
    this.getAllFilterSystemProfilePending("PENDING", "ACTIVE");
    this.getAllFilterSystemProfileInactive("INACTIVE");
  }

  clearData() {
    console.log(1)
    this.image1.splice(0, this.image1.length);
    this.systemFeatures.splice(0, this.systemFeatures.length);
    this.technolofiesget.splice(0, this.technolofiesget.length);


    this.systemProfile.systemProfilesId = '';
    this.systemProfile.systemProfilesName = '';
    this.systemProfile.systemProfilesPrice = '';
    this.systemProfile.systemProfilesDiscription = '';
    if (!this.systemProfile.categoryDto) {
      this.systemProfile.categoryDto = {}; // Ensure categoryDto is initialized
    }
    this.systemProfile.categoryDto.categoryId = this.categoryValue;

    if (!this.systemProfile.subCategoryDto) {
      this.systemProfile.subCategoryDto = {}; // Ensure subCategoryDto is initialized
    }
    this.systemProfile.subCategoryDto.subCategoryId = this.categoryValue;

    if (!this.systemProfile.employeeDto) {
      this.systemProfile.employeeDto = {}; // Ensure categoryDto is initialized
    }
    this.systemProfile.categoryDto.categoryId = '';
    this.systemProfile.subCategoryDto.subCategoryId = '';
    this.systemProfile.employeeDto.employeeId = '';
    this.dropDown = '';
    this.searchInput = '';
    this.technolofies.chipName = '';
    this.features.systemFeatureDiscripion = ''
    this.page = 1;
    this.page1 = 1;
    this.page2 = 1;
    this.Super = '';

  }

  getAllFilterSystemProfileAllSuperAdminApprove(commonStatus: String, requestStatus: String) {
    this.Super = 'present';

    this.systemProfileService.getSystemProfileAllSuperAdmin(String(requestStatus), String(commonStatus), this.page - 1, this.pageSize).subscribe(
      (response) => {
        console.log(response.payload);
        if(response.payload !=  ''){
        this.systemProfileApprove = response.payload[0];
        console.log(this.systemProfilePending)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize = pagination.totalItems; // Total number of items
          this.pageSize = 5; // Number of items per page (should match backend size)
          this.messageService.add({ severity: 'success', summary: 'success', detail: 'Successfully feached profiles' });

          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      }else{
        this.messageService.add({ severity: 'info', summary: 'Info', detail: 'No Profiles' });

      }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );

  }
  etAllFilterSystemProfileAllSuperAdminPending(commonStatus: String, requestStatus: String) {
    this.Super = 'present';

    this.systemProfileService.getSystemProfileAllSuperAdmin(String(requestStatus), String(commonStatus), this.page1 - 1, this.pageSize1).subscribe(
      (response) => {
        console.log(response.payload.length);
        if(response.payload !=  ''){
        this.systemProfilePending = response.payload[0];
        console.log(this.systemProfilePending)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize1 = pagination.totalItems; // Total number of items
          this.pageSize1 = 5; // Number of items per page (should match backend size)
          this.messageService.add({ severity: 'success', summary: 'success', detail: 'Successfully feached profiles' });

          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      }else{
        this.messageService.add({ severity: 'info', summary: 'Info', detail: 'No Profiles' });

      }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );

  }

  getAllFilterSystemProfileAllSuperAdminInactive(commonStatus: String) {
    this.Super = 'present';
    this.systemProfileService.getSystemProfileAllSuperAdmin('', String(commonStatus), this.page2 - 1, this.pageSize2).subscribe(
      (response) => {
        
        if(response.payload !=  ''){

        this.sysytmProfileInActive = response.payload[0];
        console.log(this.systemProfilePending)

        if (response.pages && response.pages.length > 0) {
          const pagination = response.pages[0];

          this.collectionSize2 = pagination.totalItems; // Total number of items
          this.pageSize2 = 5; // Number of items per page (should match backend size)
          this.messageService.add({ severity: 'success', summary: 'success', detail: 'Successfully feached profiles' });

          console.log("Total Items:", this.collectionSize, "Page Size:", this.pageSize);
        } else {
          console.warn("Pagination details not found in response");
        }
      }else{
        this.messageService.add({ severity: 'info', summary: 'Info', detail: 'No Profiles' });

      }
      },
      (error) => {
        console.error("Error fetching employees:", error);
      }
    );

  }

  systemPictureUpdatre(id: any, systemProfileId: any) {
    const payload = {
      image: this.proImgUpdate,
      id: id,

    }

    console.log(payload)
    if (payload.image != null) {
      this.sysytemImageService.uploadImgsUpdate(payload).subscribe(
        (response) => {
          if (response.status === true) {
            this.fetchImages(systemProfileId);
            this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
            this.previewUrlUpdate = null;
            this.proImgUpdate = null;
          } else {
            this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
          }
        }
      )
    } else {
      this.messageService.add({ severity: 'info', summary: 'info', detail: "Please provide an image file" });

    }
  }
  openSecondModal(content2: any) {
    this.modalService.open(content2, { size: 'md' }).result.then(() => { }, () => { });
  }

  open(content: any) {
    this.modalService.open(content);
  }

  deletePhto(id: number | null, systemProfilesId: string) {



    this.sysytemImageService.deleteImage(String(id)).subscribe(
      (response) => {
        if (response.status == true) {
          this.messageService.add({ severity: 'success', summary: 'success', detail: response.commonMessage });
          this.fetchImages(systemProfilesId);
        } else {
          this.messageService.add({ severity: 'info', summary: 'info', detail: response.commonMessage });
        }
      }
    )
  }

  onFileSelectedUpdated(event: any): void {
    this.proImgUpdate = event.target.files[0];

    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      const reader = new FileReader();

      // Read the file as a data URL
      reader.onload = () => {
        this.previewUrlUpdate = reader.result;
      };

      reader.readAsDataURL(file); // Trigger the file read
    }
  }


  removeFileUpdate(): void {
    this.proImgUpdate = null;
    this.previewUrlUpdate = null;
    const fileInput = document.getElementById('file') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }
}
