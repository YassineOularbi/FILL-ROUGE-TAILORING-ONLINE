import { MaterialType } from "../enums/material-type.enum";

export interface MaterialDto {
  name: string;
  description: string;
  image: string;
  type: MaterialType;
  unitPrice: number;
}
