import { User } from "./user.interface";

export interface Tailor extends User {
    bio: string;
    specialty: string;
    rating: number;
  }
  