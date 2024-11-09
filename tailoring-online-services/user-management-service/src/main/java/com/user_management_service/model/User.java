package com.user_management_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user_management_service.enums.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private Role role;

    private String firstName;

    private String lastName;

    private String phoneNumber;





    private LanguagePreference languagePreference;

    private Gender gender;




        @Override
        }

        @Override
            }

            }

    }
}
