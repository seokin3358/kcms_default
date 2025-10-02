/**
 * KITMS 헤더 메뉴 관리 JavaScript
 * 
 * 웹사이트 헤더의 네비게이션 메뉴를 동적으로 로드하고 렌더링하는 기능을 제공합니다.
 * API를 통해 메뉴 데이터를 가져와서 트리 구조로 표시하며, API 실패 시 기본 메뉴를 표시합니다.
 * 
 * 주요 기능:
 * - API를 통한 동적 메뉴 로드
 * - 메뉴 트리 구조 렌더링
 * - API 실패 시 기본 메뉴 표시
 * - 메뉴 호버 효과 및 드롭다운 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 */

/**
 * API를 통해 메뉴 데이터를 로드하는 함수
 * 
 * @returns {Promise<void>} 메뉴 로드 완료 시 Promise 반환
 */
async function loadMenus() {
    try {
        const response = await fetch(`${API_BASE_URL}/kitms-menus/tree`);
        if (response.ok) {
            const result = await response.json();
            if (result.status === 'OK' && result.data.menuTreeJson) {
                renderMenus(result.data.menuTreeJson);
            }0

        } else {
            console.error('메뉴 API 응답 오류:', response.status);
        }
    } catch (error) {
        console.error('메뉴 로드 오류:', error);
        // 오류 시 기본 메뉴 표시
        renderDefaultMenus();
    }
}

/**
 * API 실패 시 표시할 기본 메뉴를 렌더링하는 함수
 * 
 * API 호출이 실패했을 때 사용할 정적 메뉴 구조를 생성합니다.
 * 케이원소개, 사업영역, 홍보센터, 채용정보 등의 기본 메뉴를 포함합니다.
 * 
 * @returns {void}
 */
function renderDefaultMenus() {
    const defaultMenus = [
        {
            menuName: '케이원소개',
            menuUrl: './sub0101.html',
            children: [
                { menuName: '회사소개', menuUrl: './sub0101.html' },
                { menuName: '케이원연혁', menuUrl: './sub0102.html' },
                { menuName: 'CEO 인사말', menuUrl: './sub0103.html' },
                { menuName: '윤리경영', menuUrl: './sub0104.html' },
                { menuName: '조직구성', menuUrl: './sub0105.html' },
                { menuName: '인증현황', menuUrl: './sub0106.html' },
                { menuName: '오시는 길', menuUrl: './sub0107.html' }
            ]
        },
        {
            menuName: '사업영역',
            menuUrl: './sub0201.html',
            children: [
                { menuName: '사업하기', menuUrl: './sub0201.html' },
                { menuName: '문의하기', menuUrl: './sub0202.html' }
            ]
        },
        {
            menuName: '홍보센터',
            menuUrl: './sub0301.html',
            children: [
                { menuName: '보도자료', menuUrl: './sub0301.html' },
                { menuName: '공지사항', menuUrl: './sub0302.html' }
            ]
        },
        {
            menuName: '채용공고',
            menuUrl: './sub0401.html',
            children: [
                { menuName: '채용공고', menuUrl: './sub0401.html' },
                { menuName: '인재상', menuUrl: './sub0402.html' }
            ]
        }
    ];
    renderMenus(defaultMenus);
}

// 메뉴 렌더링 함수
function renderMenus(menuTree) {
    // 데스크톱 메뉴 렌더링
    renderDesktopMenus(menuTree);
    
    // 모바일 메뉴 렌더링
    renderMobileMenus(menuTree);
}

// 데스크톱 메뉴 렌더링
function renderDesktopMenus(menuTree) {
    const gnbDepth1 = document.querySelector('.header-gnb .gnb-depth-1');
    if (!gnbDepth1) {
        console.error('데스크톱 메뉴 컨테이너를 찾을 수 없습니다.');
        return;
    }
    
    // 기존 메뉴 제거
    gnbDepth1.innerHTML = '';
    
    menuTree.forEach(menu => {
        // is_active가 'N'이면 건너뛰기
        if (menu.isActive === 'N') {
            return;
        }
        
        const li = document.createElement('li');
        li.className = 'depth-1';
        
        // 1depth 링크
        const link = document.createElement('a');
        link.href = menu.menuUrl || '#';
        link.className = 'depth-1-link hover-underline';
        if (menu.isNewWindow === 'Y') {
            link.target = '_blank';
        }
        
        const span = document.createElement('span');
        span.textContent = menu.menuName;
        link.appendChild(span);
        
        // 2depth 메뉴가 있는 경우
        if (menu.children && menu.children.length > 0) {
            const depthItem = document.createElement('div');
            depthItem.className = 'depth-item';
            
            const ul = document.createElement('ul');
            ul.className = 'gnb-depth-2';
            
            menu.children.forEach(child => {
                // 자식 메뉴도 is_active가 'N'이면 건너뛰기
                if (child.isActive === 'N') {
                    return;
                }
                
                const childLi = document.createElement('li');
                childLi.className = 'depth-2';
                
                const childLink = document.createElement('a');
                childLink.href = child.menuUrl || '#';
                childLink.className = 'depth-2-link';
                if (child.isNewWindow === 'Y') {
                    childLink.target = '_blank';
                }
                
                const childSpan = document.createElement('span');
                childSpan.textContent = child.menuName;
                childLink.appendChild(childSpan);
                
                childLi.appendChild(childLink);
                ul.appendChild(childLi);
            });
            
            // 자식 메뉴가 있는 경우에만 depth-item 추가
            if (ul.children.length > 0) {
                depthItem.appendChild(ul);
                li.appendChild(link);
                li.appendChild(depthItem);
            } else {
                li.appendChild(link);
            }
        } else {
            li.appendChild(link);
        }
        
        gnbDepth1.appendChild(li);
    });
}

// 모바일 메뉴 렌더링
function renderMobileMenus(menuTree) {
    const mobileGnbDepth1 = document.querySelector('.sidebar .gnb-depth-1');
    if (!mobileGnbDepth1) {
        console.error('모바일 메뉴 컨테이너를 찾을 수 없습니다.');
        return;
    }
    
    // 기존 메뉴 제거
    mobileGnbDepth1.innerHTML = '';
    
    menuTree.forEach(menu => {
        // is_active가 'N'이면 건너뛰기
        if (menu.isActive === 'N') {
            return;
        }
        
        const li = document.createElement('li');
        li.className = 'depth-1';
        
        // 1depth 링크
        const link = document.createElement('a');
        link.href = menu.menuUrl || '#';
        link.className = 'depth-1-link';
        if (menu.isNewWindow === 'Y') {
            link.target = '_blank';
        }
        
        const span = document.createElement('span');
        span.textContent = menu.menuName;
        link.appendChild(span);
        
        // 2depth 메뉴가 있는 경우
        if (menu.children && menu.children.length > 0) {
            const ul = document.createElement('ul');
            ul.className = 'gnb-depth-2';
            
            menu.children.forEach(child => {
                // 자식 메뉴도 is_active가 'N'이면 건너뛰기
                if (child.isActive === 'N') {
                    return;
                }
                
                const childLi = document.createElement('li');
                childLi.className = 'depth-2';
                
                const childLink = document.createElement('a');
                childLink.href = child.menuUrl || '#';
                childLink.className = 'depth-2-link';
                if (child.isNewWindow === 'Y') {
                    childLink.target = '_blank';
                }
                
                const childSpan = document.createElement('span');
                childSpan.textContent = child.menuName;
                childLink.appendChild(childSpan);
                
                childLi.appendChild(childLink);
                ul.appendChild(childLi);
            });
            
            // 자식 메뉴가 있는 경우에만 ul 추가
            if (ul.children.length > 0) {
                li.appendChild(link);
                li.appendChild(ul);
            } else {
                li.appendChild(link);
            }
        } else {
            li.appendChild(link);
        }
        
        mobileGnbDepth1.appendChild(li);
    });
}

// 페이지 로드 시 메뉴 로드
document.addEventListener('DOMContentLoaded', function() {
    loadMenus();
});
