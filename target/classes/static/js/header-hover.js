// 헤더 메뉴 호버 이벤트 초기화
function initializeHeaderHover() {
    const header = document.querySelector('.header');
    const depth1Items = document.querySelectorAll('.header .header-gnb .gnb-depth-1 .depth-1');
    
    depth1Items.forEach(item => {
        const depthItem = item.querySelector('.depth-item');
        if (depthItem) {
            item.addEventListener('mouseenter', function() {
                // 헤더에 open 클래스 추가
                if (header) {
                    header.classList.add('open');
                }
                item.classList.add('current');
                
                // 하위메뉴 표시
                depthItem.style.visibility = 'visible';
                depthItem.style.opacity = '1';
                
                // 높이 설정 (common.js와 동일한 로직)
                const targetMenu = item.querySelector('.gnb-depth-2');
                if (targetMenu) {
                    const targetHeight = targetMenu.getBoundingClientRect().height;
                    depthItem.style.height = `${targetHeight}px`;
                }
            });
            
            item.addEventListener('mouseleave', function() {
                // 헤더에서 open 클래스 제거
                if (header) {
                    header.classList.remove('open');
                }
                item.classList.remove('current');
                
                // 하위메뉴 숨김
                depthItem.style.visibility = 'hidden';
                depthItem.style.opacity = '0';
                depthItem.style.height = '0px';
            });
        }
    });
}

// DOM 로드 완료 후 호버 이벤트 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 헤더가 로드될 때까지 잠시 대기
    setTimeout(initializeHeaderHover, 200);
});
