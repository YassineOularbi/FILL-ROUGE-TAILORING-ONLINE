import { MaterialType } from "../enums/material-type.enum";
import { MaterialOptionDto } from "./material-option.dto";

export interface CustomizableOptionDto {
  type: MaterialType;
  materialDtos: MaterialOptionDto[];
}
