import { User } from "./user.interface";

export interface Customer extends User {
    loyaltyPoints: number;
}
