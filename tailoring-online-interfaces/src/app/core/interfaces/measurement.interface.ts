import { CustomizableMeasurement } from "./customizable-measurement.interface";

export interface Measurement {
  id?: number;
  name: string;
  description: string;
  logo: string;
  measurements?: CustomizableMeasurement[];
}
