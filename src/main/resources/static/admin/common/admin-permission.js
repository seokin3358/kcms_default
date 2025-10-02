/**
 * 어드민 페이지 권한 체크 공통 스크립트
 */
class AdminPermissionManager {
    constructor() {
        this.API_BASE_URL = window.location.origin + '/api';
        this.currentUser = null;
        this.userPermissions = new Map();
    }

    /**
     * 페이지 로드 시 권한 체크 및 초기화
     */
    async init() {
        try {
            // 현재 사용자 정보 가져오기
            await this.loadCurrentUser();
            
            // 현재 페이지의 권한 체크
            const hasAccess = await this.checkPagePermission();
            
            if (!hasAccess) {
                this.showAccessDenied();
                return false;
            }
            
            // 사이드바 메뉴 로드 (권한 기반)
            await this.loadAccessibleMenus();
            
            return true;
        } catch (error) {
            console.error('권한 체크 초기화 오류:', error);
            this.showError('권한 체크 중 오류가 발생했습니다.');
            return false;
        }
    }

    /**
     * 현재 사용자 정보 로드
     */
    async loadCurrentUser() {
        try {
            const response = await fetch(`${this.API_BASE_URL}/account`, {
                credentials: 'include'
            });
            
            if (response.ok) {
                this.currentUser = await response.json();
            } else {
                throw new Error('사용자 정보를 가져올 수 없습니다.');
            }
        } catch (error) {
            console.error('사용자 정보 로드 오류:', error);
            throw error;
        }
    }

    /**
     * 현재 페이지 접근 권한 체크
     */
    async checkPagePermission() {
        if (!this.currentUser) {
            return false;
        }

        const currentPath = window.location.pathname;
        const pageMenuMapping = this.getPageMenuMapping();
        const requiredMenuName = pageMenuMapping[currentPath];

        if (!requiredMenuName) {
            // 매핑되지 않은 페이지는 접근 허용 (기본 페이지들)
            return true;
        }

        // 메인 관리자(001)는 모든 페이지 접근 가능
        if (this.currentUser.authCode === '001') {
            return true;
        }

        // 서브 관리자(002)는 권한 테이블에서 확인
        try {
            const response = await fetch(`${this.API_BASE_URL}/admin-menu-permissions/check`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: this.currentUser.userId,
                    menuName: requiredMenuName
                }),
                credentials: 'include'
            });

            if (response.ok) {
                const result = await response.json();
                return result.hasAccess;
            } else {
                console.error('권한 체크 API 오류:', response.status);
            }
        } catch (error) {
            console.error('페이지 권한 체크 오류:', error);
        }

        return false;
    }

    /**
     * 페이지 경로와 메뉴명 매핑
     */
    getPageMenuMapping() {
        return {
            '/admin/dashboard.html': '대시보드',
            '/admin/admin-management.html': '관리자 관리',
            '/admin/menu-management.html': '메뉴 관리',
            '/admin/notice-management.html': '공지사항 관리',
            '/admin/notice-detail.html': '공지사항 관리',
            '/admin/newsroom-management.html': '보도자료 관리',
            '/admin/cms-test.html': '콘텐츠 관리'
        };
    }

    /**
     * 접근 권한이 없을 때 처리
     */
    showAccessDenied() {
        const currentPath = window.location.pathname;
        const pageMenuMapping = this.getPageMenuMapping();
        const requiredMenuName = pageMenuMapping[currentPath] || '알 수 없는 페이지';
        
        // 알림 메시지 표시
        alert(`'${requiredMenuName}' 페이지에 대한 접근 권한이 없습니다.\n대시보드로 이동합니다.`);
        
        // 대시보드로 리다이렉트
        window.location.href = '/admin/dashboard.html';
    }

    /**
     * 권한 기반 메뉴 로드
     */
    async loadAccessibleMenus() {
        try {
            const response = await fetch(`${this.API_BASE_URL}/admin-menus/accessible`, {
                credentials: 'include'
            });
            
            if (response.ok) {
                const menus = await response.json();
                this.renderAccessibleMenus(menus);
            } else {
                console.error('메뉴 로드 실패:', response.status);
            }
        } catch (error) {
            console.error('메뉴 로드 오류:', error);
        }
    }

    /**
     * 권한 기반 메뉴 렌더링
     */
    renderAccessibleMenus(menus) {
        const sidebarMenu = document.getElementById('sidebarMenu');
        if (!sidebarMenu) return;

        if (menus.length === 0) {
            sidebarMenu.innerHTML = '<li class="loading">등록된 메뉴가 없습니다.</li>';
            return;
        }

        sidebarMenu.innerHTML = menus.map(menu => `
            <li class="menu-item">
                <a class="menu-link" 
                   href="${menu.menuUrl || '#'}" 
                   data-menu-id="${menu.menuNo}">
                    <span class="menu-icon">${this.getMenuIcon(menu.menuIcon)}</span>
                    <span class="menu-text">${menu.menuName}</span>
                </a>
            </li>
        `).join('');

        // 메뉴 클릭 이벤트 추가
        this.addMenuClickEvents();
    }

    /**
     * 메뉴 아이콘 매핑
     */
    getMenuIcon(iconName) {
        const iconMap = {
            'dashboard': '📊',
            'content_copy': '📄',
            'people': '👥',
            'settings': '⚙️',
            'announcement': '📢',
            'newspaper': '📰',
            'menu': '📋',
            'admin_panel_settings': '👤',
            'tune': '🎛️',
            'history': '📜'
        };
        return iconMap[iconName] || '📁';
    }

    /**
     * 메뉴 클릭 이벤트 추가
     */
    addMenuClickEvents() {
        document.querySelectorAll('.menu-link').forEach(link => {
            link.addEventListener('click', (e) => {
                if (link.getAttribute('href') && link.getAttribute('href') !== '#') {
                    this.setActiveMenuItem(link);
                    window.location.href = link.getAttribute('href');
                }
            });
        });
    }

    /**
     * 활성 메뉴 설정
     */
    setActiveMenuItem(activeLink) {
        document.querySelectorAll('.menu-link.active').forEach(link => {
            link.classList.remove('active');
        });
        activeLink.classList.add('active');
    }

    /**
     * 오류 메시지 표시
     */
    showError(message) {
        console.error(message);
        alert(`오류가 발생했습니다: ${message}\n대시보드로 이동합니다.`);
        window.location.href = '/admin/dashboard.html';
    }
}

// 전역 인스턴스 생성
window.adminPermissionManager = new AdminPermissionManager();

// 페이지 로드 시 자동 초기화
document.addEventListener('DOMContentLoaded', async function() {
    const success = await window.adminPermissionManager.init();
    if (!success) {
        console.error('권한 체크 초기화 실패');
    }
});
