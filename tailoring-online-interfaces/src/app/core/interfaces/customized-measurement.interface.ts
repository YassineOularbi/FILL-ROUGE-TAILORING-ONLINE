import { MeasurementUnit } from "../enums/measurement-unit.enum";

export interface CustomizedMeasurement {
  id?: number;
  measurementId: number;
  value: number;
  unit: MeasurementUnit;
  productId?: number;
}
