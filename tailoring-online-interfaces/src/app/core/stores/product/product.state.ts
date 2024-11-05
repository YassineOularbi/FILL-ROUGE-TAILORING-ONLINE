import { Product } from "../../interfaces/product.interface";

export interface ProductState {
    products: Product[];
    totalRecords: number;
    loading: boolean;
    error: string | null;
  }
  
  export const initialProductState: ProductState = {
    products: [],
    totalRecords: 0,
    loading: false,
    error: null
  };