import { UserDto } from "./user.dto";

export interface CustomerDto extends UserDto {
  loyaltyPoints: number;
}
