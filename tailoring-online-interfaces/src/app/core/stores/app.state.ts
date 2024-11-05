import { PaginationState } from "./pagination/pagination.state";
import { ProductState } from "./product/product.state";

export interface AppState {
  pagination: PaginationState;
  product: ProductState;
}
