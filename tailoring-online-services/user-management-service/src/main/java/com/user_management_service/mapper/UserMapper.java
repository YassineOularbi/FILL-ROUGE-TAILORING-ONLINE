package com.user_management_service.mapper;

import com.github.dozermapper.core.Mapper;
import com.user_management_service.config.DozerConfig;
import com.user_management_service.dto.AdminDto;
import com.user_management_service.dto.CustomerDto;
import com.user_management_service.dto.TailorDto;
import com.user_management_service.dto.UserDto;
import com.user_management_service.model.Admin;
import com.user_management_service.model.Customer;
import com.user_management_service.model.Tailor;
import com.user_management_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final Mapper dozerMapper;

    public UserDto toDto(User user) {
        if (user instanceof Tailor) {
            return dozerMapper.map(user, TailorDto.class);
        } else if (user instanceof Customer) {
            return dozerMapper.map(user, CustomerDto.class);
        } else if (user instanceof Admin) {
            return dozerMapper.map(user, AdminDto.class);
        }
        return dozerMapper.map(user, UserDto.class);
    }

    public User toEntity(UserDto userDto) {
        if (userDto instanceof TailorDto) {
            return dozerMapper.map(userDto, Tailor.class);
        } else if (userDto instanceof CustomerDto) {
            return dozerMapper.map(userDto, Customer.class);
        } else if (userDto instanceof AdminDto) {
            return dozerMapper.map(userDto, Admin.class);
        }
        return dozerMapper.map(userDto, User.class);
    }

    public User partialUpdate(UserDto userDto, User user) {
        User updatedUser = dozerMapper.map(user, User.class);
        DozerConfig.PartialUpdateUtil.partialUpdate(userDto, updatedUser);
        if (user instanceof Tailor && userDto instanceof TailorDto) {
            DozerConfig.PartialUpdateUtil.partialUpdate((TailorDto) userDto, (Tailor) updatedUser);
        } else if (user instanceof Customer && userDto instanceof CustomerDto) {
            DozerConfig.PartialUpdateUtil.partialUpdate((CustomerDto) userDto, (Customer) updatedUser);
        } else if (user instanceof Admin && userDto instanceof AdminDto) {
            DozerConfig.PartialUpdateUtil.partialUpdate((AdminDto) userDto, (Admin) updatedUser);
        }

        return updatedUser;
    }

}
