import { Product } from "../interfaces/product.interface";
import { CustomizedMeasurementDto } from "./customized-measurement.dto";
import { CustomizedOptionDto } from "./customized-option.dto";

export interface CustomizedProductDto {
  product: Product;
  measurementDtos: CustomizedMeasurementDto[];
  optionDtos: CustomizedOptionDto[];
}
