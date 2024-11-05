import { MaterialType } from "../enums/material-type.enum";
import { MaterialOption } from "./material-option.interface";

export interface CustomizableOption {
    id?: number;
    modelId?: number;
    type: MaterialType;
    materials?: MaterialOption[];
}
