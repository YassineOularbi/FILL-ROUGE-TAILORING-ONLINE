import { CustomizedMeasurement } from "./customized-measurement.interface";
import { CustomizedOption } from "./customized-option.interface";

export interface CustomizedProduct {
    id?: number;
    productId: number;
    measurements?: CustomizedMeasurement[];
    options?: CustomizedOption[];
  }
  