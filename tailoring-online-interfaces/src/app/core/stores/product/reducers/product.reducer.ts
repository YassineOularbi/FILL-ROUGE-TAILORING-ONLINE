import { createReducer, on } from '@ngrx/store';
import * as ProductActions from '../actions/product.actions';
import { initialProductState } from '../product.state';


export const productReducer = createReducer(
  initialProductState,

  on(ProductActions.loadProducts, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(ProductActions.searchProducts, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(ProductActions.loadProductsSuccess, (state, { products, totalRecords }) => ({
    ...state,
    products: products,
    totalRecords: totalRecords,
    loading: false
  })),

  on(ProductActions.loadProductsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error: error
  }))
);
