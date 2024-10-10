import { MaterialOptionKey } from "./material-option-key.inetrface";
import { Material } from "./material.interface";

export interface MaterialOption {
    id: MaterialOptionKey;
    material: Material;
    optionId?: number;
  }
  