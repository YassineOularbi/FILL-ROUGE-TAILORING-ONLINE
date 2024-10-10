import { MaterialType } from "../enums/material-type.enum";

export interface CustomizedOption {
  id?: number;
  type: MaterialType;
  materialId: number;
  productId?: number;
}
