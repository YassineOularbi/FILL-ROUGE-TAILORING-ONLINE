package com.store_management_service.repository;

import com.store_management_service.model.CustomizableOption;
import com.store_management_service.model.MaterialOption;
import com.store_management_service.model.MaterialOptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialOptionRepository extends JpaRepository<MaterialOption, MaterialOptionKey> {
    List<MaterialOption> getAllByOption(CustomizableOption option);
}
