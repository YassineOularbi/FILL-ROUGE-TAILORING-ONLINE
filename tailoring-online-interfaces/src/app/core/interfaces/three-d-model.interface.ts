import { CustomizableMeasurement } from "./customizable-measurement.interface";
import { CustomizableOption } from "./customizable-option.interface";

export interface ThreeDModel {
    id?: number;
    productId: number;
    measurements?: CustomizableMeasurement[];
    options?: CustomizableOption[];
  }
  