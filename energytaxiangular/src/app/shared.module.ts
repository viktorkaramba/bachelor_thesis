import {CommonModule} from "@angular/common";
import {NgbdSortableHeader} from "./service/sortable.directive";
import {NgModule} from "@angular/core";

@NgModule({
  imports:      [ CommonModule ],
  declarations: [ NgbdSortableHeader],
  exports:      [ NgbdSortableHeader]
})
export class SharedModule { }
