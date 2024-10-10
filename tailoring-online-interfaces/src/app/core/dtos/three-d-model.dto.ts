import { CustomizableMeasurementDto } from "./customizable-measurement.dto";
import { CustomizableOptionDto } from "./customizable-option.dto";
import { ProductDto } from "./product.dto";

export interface ThreeDModelDto {
  productDto: ProductDto;
  measurementDtos: CustomizableMeasurementDto[];
  optionDtos: CustomizableOptionDto[];
}
