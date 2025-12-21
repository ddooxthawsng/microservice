package com.thangdm.menu_service.repository;

import com.thangdm.menu_service.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {
    List<Menu> findByParentId(String parentId);
    List<Menu> findByIsActive(Boolean isActive);
    List<Menu> findAllByOrderBySortOrderAsc();
    List<Menu> findByParentIdIsNullOrderBySortOrderAsc();
}
