export interface Bank {
    id?: number;
    cardNumber: string;
    expirationDate: Date;
    cvc: string;
    userId: string;
}
