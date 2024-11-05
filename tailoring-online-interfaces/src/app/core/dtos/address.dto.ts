import { User } from "../interfaces/user.interface";

export interface AddressDto {
  address: string;
  suite?: string;
  city: string;
  province: string;
  country: string;
  zipCode: string;
  isDefault?: boolean;
  user?: User;
}
