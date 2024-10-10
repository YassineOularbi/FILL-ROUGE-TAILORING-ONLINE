import { CustomizableMeasurementKey } from "./customizable-measurement-key.interface";
import { Measurement } from "./measurement.interface";

export interface CustomizableMeasurement {
    id: CustomizableMeasurementKey;
    modelId?: number;
    measurement: Measurement;
}
