import { Category } from "../enums/category.enum";

export interface Product {
    id?: number;
    name: string;
    description: string;
    category: Category;
    picture: string;
    images?: string[];
    details?: { [key: string]: string };
    historicalStory: string;
    codeSKU?: string;
    rating?: number;
    sales?: number;
    authenticityVerified?: boolean;
    storeId?: number;
    threeDModelId?: number;
  }
  