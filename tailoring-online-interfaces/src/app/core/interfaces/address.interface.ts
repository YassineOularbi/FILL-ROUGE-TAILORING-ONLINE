export interface Address {
    id?: number;
    address: string;
    suite?: string;
    city: string;
    province: string;
    country: string;
    zipCode: string;
    isDefault: boolean;
    userId: string;
}
