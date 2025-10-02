package com.kone.kitms.web.rest;

import com.kone.kitms.aop.logging.ExTokenCheck;
import com.kone.kitms.domain.KitmsMenu;
import com.kone.kitms.service.KitmsMenuService;
import com.kone.kitms.service.dto.CustomReturnDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 메뉴 관리 REST API
 */
@RestController
@RequestMapping("/api/kitms-menus")
public class KitmsMenuResource {

    @Autowired
    private KitmsMenuService kitmsMenuService;

    /**
     * 전체 메뉴 트리 구조 조회 (공개 API)
     */
    @ExTokenCheck
    @GetMapping("/tree")
    public CustomReturnDTO getMenuTree() {
        try {
            List<KitmsMenu> menuTree = kitmsMenuService.getAllMenuTree();
            List<Map<String, Object>> menuTreeJson = kitmsMenuService.getMenuTreeAsJson();
            
            Map<String, Object> data = new HashMap<>();
            data.put("menuTree", menuTree);
            data.put("menuTreeJson", menuTreeJson);
            
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.OK);
            response.setData(data);
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 1depth 메뉴만 조회 (공개 API)
     */
    @ExTokenCheck
    @GetMapping("/root")
    public CustomReturnDTO getRootMenus() {
        try {
            List<KitmsMenu> rootMenus = kitmsMenuService.getRootMenus();
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.OK);
            response.setData(Map.of("list", rootMenus));
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("루트 메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 특정 부모 메뉴의 자식 메뉴 조회 (공개 API)
     */
    @ExTokenCheck
    @GetMapping("/children/{parentId}")
    public CustomReturnDTO getChildMenus(@PathVariable Long parentId) {
        try {
            List<KitmsMenu> childMenus = kitmsMenuService.getChildMenus(parentId);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.OK);
            response.setData(Map.of("list", childMenus));
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("자식 메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 전체 메뉴 목록 조회 (관리자용)
     */
    @ExTokenCheck
    @GetMapping("/all")
    public CustomReturnDTO getAllMenus() {
        try {
            System.out.println("=== 메뉴 API 호출됨 ===");
            List<KitmsMenu> allMenus = kitmsMenuService.getAllMenus();
            System.out.println("조회된 메뉴 수: " + (allMenus != null ? allMenus.size() : 0));
            if (allMenus != null && !allMenus.isEmpty()) {
                System.out.println("첫 번째 메뉴: " + allMenus.get(0));
            }
            
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.OK);
            response.setData(Map.of("list", allMenus));
            return response;
        } catch (Exception e) {
            System.err.println("메뉴 조회 오류: " + e.getMessage());
            e.printStackTrace();
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("전체 메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 메뉴 ID로 메뉴 조회 (관리자용)
     */
    @ExTokenCheck
    @GetMapping("/{menuId}")
    public CustomReturnDTO getMenuById(@PathVariable Long menuId) {
        try {
            KitmsMenu menu = kitmsMenuService.getMenuById(menuId);
            if (menu == null) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
                response.setMessage("메뉴를 찾을 수 없습니다.");
                return response;
            }
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.OK);
            response.setData(Map.of("menu", menu));
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 메뉴 생성 (관리자용)
     */
    @ExTokenCheck
    @PostMapping
    public CustomReturnDTO createMenu(@RequestBody KitmsMenu menu) {
        try {
            // 메뉴 레벨 자동 계산
            if (menu.getParentId() == null) {
                menu.setMenuLevel(1);
            } else {
                KitmsMenu parentMenu = kitmsMenuService.getMenuById(menu.getParentId());
                if (parentMenu != null) {
                    menu.setMenuLevel(parentMenu.getMenuLevel() + 1);
                } else {
                    menu.setMenuLevel(1);
                }
            }
            
            int result = kitmsMenuService.createMenu(menu);
            CustomReturnDTO response = new CustomReturnDTO();
            if (result > 0) {
                response.setStatus(org.springframework.http.HttpStatus.OK);
                response.setMessage("메뉴가 성공적으로 생성되었습니다.");
            } else {
                response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
                response.setMessage("메뉴 생성에 실패했습니다.");
            }
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 생성 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 메뉴 수정 (관리자용)
     */
    @ExTokenCheck
    @PutMapping("/{menuId}")
    public CustomReturnDTO updateMenu(@PathVariable Long menuId, @RequestBody KitmsMenu menu) {
        try {
            menu.setMenuId(menuId);
            
            // 메뉴 레벨 자동 계산
            if (menu.getParentId() == null) {
                menu.setMenuLevel(1);
            } else {
                KitmsMenu parentMenu = kitmsMenuService.getMenuById(menu.getParentId());
                if (parentMenu != null) {
                    menu.setMenuLevel(parentMenu.getMenuLevel() + 1);
                } else {
                    menu.setMenuLevel(1);
                }
            }
            
            int result = kitmsMenuService.updateMenu(menu);
            CustomReturnDTO response = new CustomReturnDTO();
            if (result > 0) {
                response.setStatus(org.springframework.http.HttpStatus.OK);
                response.setMessage("메뉴가 성공적으로 수정되었습니다.");
            } else {
                response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
                response.setMessage("메뉴 수정에 실패했습니다.");
            }
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 수정 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 메뉴 삭제 (관리자용)
     */
    @ExTokenCheck
    @DeleteMapping("/{menuId}")
    public CustomReturnDTO deleteMenu(@PathVariable Long menuId) {
        try {
            int result = kitmsMenuService.deleteMenu(menuId);
            CustomReturnDTO response = new CustomReturnDTO();
            if (result > 0) {
                response.setStatus(org.springframework.http.HttpStatus.OK);
                response.setMessage("메뉴가 성공적으로 삭제되었습니다.");
            } else {
                response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
                response.setMessage("메뉴 삭제에 실패했습니다.");
            }
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 메뉴 활성화/비활성화 (관리자용)
     */
    @PutMapping("/{menuId}/status")
    public CustomReturnDTO toggleMenuStatus(@PathVariable Long menuId, @RequestParam String isActive) {
        try {
            int result = kitmsMenuService.toggleMenuStatus(menuId, isActive);
            CustomReturnDTO response = new CustomReturnDTO();
            if (result > 0) {
                String message = "Y".equals(isActive) ? "메뉴가 활성화되었습니다." : "메뉴가 비활성화되었습니다.";
                response.setStatus(org.springframework.http.HttpStatus.OK);
                response.setMessage(message);
            } else {
                response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
                response.setMessage("메뉴 상태 변경에 실패했습니다.");
            }
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 상태 변경 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 메뉴 정렬 순서 변경 (관리자용)
     */
    @PutMapping("/{menuId}/sort")
    public CustomReturnDTO updateMenuSortOrder(@PathVariable Long menuId, @RequestParam Integer sortOrder) {
        try {
            int result = kitmsMenuService.updateMenuSortOrder(menuId, sortOrder);
            CustomReturnDTO response = new CustomReturnDTO();
            if (result > 0) {
                response.setStatus(org.springframework.http.HttpStatus.OK);
                response.setMessage("메뉴 정렬 순서가 변경되었습니다.");
            } else {
                response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
                response.setMessage("메뉴 정렬 순서 변경에 실패했습니다.");
            }
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 정렬 순서 변경 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }

    /**
     * 특정 메뉴와 모든 하위 메뉴 조회 (관리자용)
     */
    @GetMapping("/{menuId}/all-children")
    public CustomReturnDTO getMenuWithAllChildren(@PathVariable Long menuId) {
        try {
            List<KitmsMenu> menus = kitmsMenuService.getMenuWithAllChildren(menuId);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.OK);
            response.setData(Map.of("list", menus));
            return response;
        } catch (Exception e) {
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            response.setMessage("메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
            return response;
        }
    }
}
