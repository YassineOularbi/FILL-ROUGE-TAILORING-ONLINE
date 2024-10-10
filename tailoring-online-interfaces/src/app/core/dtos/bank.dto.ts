import { User } from "../interfaces/user.interface";

export interface BankDto {
  cardNumber: string;
  expirationDate: Date;
  cvc: string;
  user: User;
}
