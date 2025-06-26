import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { CategoryService } from 'src/app/services/category.service';
import { SubCategoryService } from 'src/app/services/sub-category.service';
import { Category } from 'src/app/shared/category';
import { SubCategory } from 'src/app/shared/subCategory';
import { CategorysComponent } from '../categorys/categorys.component';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  @ViewChild(CategorysComponent, {static: false}) categoryComponent!: CategorysComponent;
categoryPayload : Category [] = [];
subCategoryPayload : SubCategory [] = [];

inPut : any = '';

  constructor(private renderer: Renderer2, private el: ElementRef,private categoryService : CategoryService,private subCategoryService : SubCategoryService,private router: Router) {
  this.getAllCategories();
  this.getAllSubCategories();
  }

  toggleDropdown(dropdownId: string, show: boolean): void {
    const dropdown = this.el.nativeElement.querySelector(`#${dropdownId}`);
    if (dropdown) {
      this.renderer.setStyle(dropdown, 'display', show ? 'block' : 'none');
    }
  }

  setupDropdownListeners(): void {
    const mainDropdown = this.el.nativeElement.querySelector('#mainDropdown');
    const mainDropdownContent = this.el.nativeElement.querySelector('#mainDropdownContent');
    const subDropdown1 = this.el.nativeElement.querySelector('#subDropdown1');
    const subDropdown2 = this.el.nativeElement.querySelector('#subDropdown2');

    if (mainDropdown && mainDropdownContent) {
      this.renderer.listen(mainDropdown, 'mouseenter', () =>
        this.toggleDropdown('mainDropdownContent', true)
      );
      this.renderer.listen(mainDropdown, 'mouseleave', () =>
        this.toggleDropdown('mainDropdownContent', false)
      );
    }

    if (subDropdown1) {
      const subDropdown1Parent = subDropdown1.parentElement;
      if (subDropdown1Parent) {
        this.renderer.listen(subDropdown1Parent, 'mouseenter', () =>
          this.toggleDropdown('subDropdown1', true)
        );
        this.renderer.listen(subDropdown1Parent, 'mouseleave', () =>
          this.toggleDropdown('subDropdown1', false)
        );
      }
    }

    if (subDropdown2) {
      const subDropdown2Parent = subDropdown2.parentElement;
      if (subDropdown2Parent) {
        this.renderer.listen(subDropdown2Parent, 'mouseenter', () =>
          this.toggleDropdown('subDropdown2', true)
        );
        this.renderer.listen(subDropdown2Parent, 'mouseleave', () =>
          this.toggleDropdown('subDropdown2', false)
        );
      }
    }
  }

  getAllCategories(){
    let commonStatus = "ACTIVE";
    this.categoryService.getAllCategories(commonStatus).subscribe(
      (response)=>{
        this.categoryPayload = response.payload[0];
      }
    );
  }

  getAllSubCategories(){
    let commonStatus = "ACTIVE";
    this.subCategoryService.getAllSubCategories(commonStatus).subscribe(
    (response)=> {
        this.subCategoryPayload = response.payload[0];
        console.log(this.subCategoryPayload)

}
    )
  }

  ngOnInit(): void {
    this.setupDropdownListeners();
  }

  navigateToCategory(categoryId: any) {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['/software/Categorys', categoryId]);
    });
  }


  search() {
    if (this.inPut.trim()) {
      this.router.navigate(['/software'], { queryParams: { inPut: this.inPut } });
    }
  }

  }
  

