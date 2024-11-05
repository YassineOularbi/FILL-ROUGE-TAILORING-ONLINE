import { Material } from "./material.interface";
import { Product } from "./product.interface";

export interface Store {
    id?: number;
    name: string;
    description: string;
    type?: { [key: string]: string };
    images?: string[];
    logo: string;
    coverImage: string;
    rating: number;
    tailorId: string;
    materials?: Material[];
    products?: Product[];
  }
  