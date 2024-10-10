import { Category } from "../enums/category.enum";

export interface ProductDto {
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
}
