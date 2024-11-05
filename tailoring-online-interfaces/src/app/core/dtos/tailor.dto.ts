import { UserDto } from "./user.dto";

export interface TailorDto extends UserDto {
  bio: string;
  specialty: string;
  rating: number;
}
