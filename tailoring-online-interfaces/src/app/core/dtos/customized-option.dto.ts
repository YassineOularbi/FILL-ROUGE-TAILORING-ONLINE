import { MaterialType } from "../enums/material-type.enum";
import { Material } from "../interfaces/material.interface";

export interface CustomizedOptionDto {
  type: MaterialType;
  material: Material;
}
