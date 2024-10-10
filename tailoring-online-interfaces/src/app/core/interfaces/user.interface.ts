import { Gender } from "../enums/gender.enum";
import { LanguagePreference } from "../enums/language-preference.enum";
import { NotificationPreference } from "../enums/notification-preference.enum";
import { Role } from "../enums/role.enum";
import { Status } from "../enums/status.enum";

export interface User {
    id: string;
    username: string;
    password: string;
    email: string;
    role: Role;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    profilePicture?: string;
    dateOfBirth: Date;
    lastLogin: Date;
    status: Status;
    languagePreference: LanguagePreference;
    gender: Gender;
    notificationPreference: NotificationPreference;
    emailVerified: boolean;
    phoneVerified: boolean;
    OAuth2: boolean;
    is2FAuth: boolean;
    hasFingerprint: boolean;
    hasFaceId: boolean;
    isVerified: boolean;
  }
  