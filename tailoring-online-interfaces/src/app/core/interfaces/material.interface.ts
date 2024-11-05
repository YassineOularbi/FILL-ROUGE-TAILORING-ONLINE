import { MaterialType } from "../enums/material-type.enum";
import { MaterialOption } from "./material-option.interface";

export interface Material {
    id?: number;
    name: string;
    description: string;
    image: string;
    type: MaterialType;
    unitPrice: number;
    storeId?: number;
    materials?: MaterialOption[];
  }
  