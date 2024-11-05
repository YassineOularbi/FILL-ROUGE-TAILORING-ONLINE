import { MeasurementUnit } from "../enums/measurement-unit.enum";
import { Measurement } from "../interfaces/measurement.interface";

export interface CustomizedMeasurementDto {
  measurement: Measurement;
  value: number;
  unit: MeasurementUnit;
}
