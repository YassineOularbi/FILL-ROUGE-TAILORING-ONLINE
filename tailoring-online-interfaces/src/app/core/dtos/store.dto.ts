import { Tailor } from "../interfaces/tailor.interface";

export interface StoreDto {
  name: string;
  description: string;
  type?: { [key: string]: string };
  images?: string[];
  logo: string;
  coverImage: string;
  rating: number;
  tailor: Tailor;
}
