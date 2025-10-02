/**
 * KITMS 공지사항 섹션 JavaScript
 * 
 * 메인 페이지의 공지사항 섹션을 동적으로 로드하고 표시하는 기능을 제공합니다.
 * API를 통해 공지사항 데이터를 가져와서 썸네일 이미지와 함께 표시합니다.
 * 
 * 주요 기능:
 * - API를 통한 공지사항 데이터 로드
 * - 공지사항 내용에서 이미지 추출 및 표시
 * - 이미지 URL 보안 처리
 * - 공지사항 클릭 이벤트 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 */

// 전역 변수 사용 (main.html에서 이미 선언됨)
// const API_BASE_URL = 'http://localhost:8080/api';
// let secureImageUrls = {};

/**
 * 공지사항 HTML 내용에서 이미지 URL을 추출하는 함수
 * 
 * @param {string} noticeContent - 공지사항 HTML 내용
 * @returns {string|null} 추출된 이미지 URL 또는 null
 */
function extractImageFromContent(noticeContent) {
    if (!noticeContent) return null;
    
    try {
        // HTML 내용에서 img 태그 찾기
        const imgRegex = /<img[^>]+src\s*=\s*["']([^"']+)["'][^>]*>/i;
        const match = noticeContent.match(imgRegex);
        
        if (match && match[1]) {
            let imageUrl = match[1];
            
            // base64 이미지인 경우 직접 반환
            if (imageUrl.startsWith('data:image/')) {
                return imageUrl;
            }
            
            // 상대 경로를 절대 경로로 변환
            if (imageUrl.startsWith('/upload/')) {
                imageUrl = `${API_BASE_URL}${imageUrl}`;
            } else if (imageUrl.startsWith('upload/')) {
                imageUrl = `${API_BASE_URL}/${imageUrl}`;
            }
            
            return imageUrl;
        }
    } catch (error) {
        console.warn('이미지 추출 중 오류:', error);
    }
    
    return null;
}

/**
 * 공지사항 섹션용 공지사항 데이터를 로드하는 함수
 * 
 * 상단 고정 공지사항만 가져와서 메인 페이지의 공지사항 섹션에 표시합니다.
 * 각 공지사항의 썸네일 이미지를 처리하고 클릭 이벤트를 설정합니다.
 * 
 * @returns {Promise<void>} 공지사항 로드 완료 시 Promise 반환
 */
async function loadNoticesForSection() {
    try {
        // ✅ 상단고정 공지사항만 가져오기
        const response = await fetch(`${API_BASE_URL}/kitms-notices/static`);
        if (response.ok) {
            const data = await response.json();
            const notices = data.data.list || [];
            
            // 각 공지사항의 썸네일 이미지 처리
            for (let notice of notices) {
                // 1. 먼저 공지사항 내용에서 이미지 추출 시도
                const contentImage = extractImageFromContent(notice.noticeContent);
                if (contentImage) {
                    notice.thumbnailPath = contentImage;
                    continue; // 내용에서 이미지를 찾았으면 첨부파일 확인 생략
                }
                
                // 2. 내용에 이미지가 없으면 첨부파일에서 썸네일 찾기
                try {
                    const imageResponse = await fetch(`${API_BASE_URL}/kitms-attaches/notice/${notice.noticeNo}`);
                    if (imageResponse.ok) {
                        const imageData = await imageResponse.json();
                        const images = imageData.data.list || [];
                        const thumbnail = images.find(img => img.isThumbnail === true || img.isThumbnail === '1');
                        if (thumbnail) {
                            notice.thumbnailPath = thumbnail.attachFilePath;
                        }
                    }
                } catch (error) {
                    console.warn(`공지사항 ${notice.noticeNo} 첨부파일 로드 실패:`, error);
                }
            }
            
            displayNoticesForSection(notices);
        } else {
            displayErrorForSection('공지사항을 불러올 수 없습니다.');
        }
    } catch (error) {
        console.error('공지사항 로드 실패:', error);
        displayErrorForSection('공지사항을 불러오는 중 오류가 발생했습니다.');
    }
}

// 공지사항 섹션용 공지사항 표시 함수
function displayNoticesForSection(notices) {
    const noticeList = document.querySelector('#noticeSection #noticeList');
    if (!noticeList) return;
    
    if (!notices || notices.length === 0) {
        noticeList.innerHTML = '<div class="error">등록된 공지사항이 없습니다.</div>';
        return;
    }
    
    const noticeHTML = notices.map(notice => {
        // HTML 태그 제거하여 텍스트만 추출
        const textContent = notice.noticeContent ? 
            notice.noticeContent.replace(/<[^>]*>/g, '').trim() : 
            notice.noticeTitle || '내용이 없습니다.';
        const truncatedContent = textContent.length > 100 ? textContent.substring(0, 100) + '...' : textContent;
        
        // 썸네일 이미지 URL 결정
        let thumbnailSrc = ''; // 기본 이미지 (JavaScript에서 동적 설정)
        let dataSecureImage = 'logos/kone_logo_color.svg';
        
        if (notice.thumbnailPath) {
            // base64 이미지인 경우
            if (notice.thumbnailPath.startsWith('data:image/')) {
                thumbnailSrc = notice.thumbnailPath;
                dataSecureImage = 'base64-image'; // base64는 보안 URL 처리 불필요
            }
            // API URL로 시작하는 경우 (내용에서 추출된 이미지)
            else if (notice.thumbnailPath.startsWith(API_BASE_URL)) {
                thumbnailSrc = notice.thumbnailPath;
                // API URL에서 파일명만 추출
                const fileName = notice.thumbnailPath.split('/').pop();
                dataSecureImage = fileName || 'kone_logo_color.svg';
            } else {
                // 기존 첨부파일 경로인 경우
                thumbnailSrc = notice.thumbnailPath;
                dataSecureImage = notice.thumbnailPath.replace('/images/', '');
            }
        }
        
        return `
            <div class="row">
                <div class="date">
                    <p>${notice.createDt}</p>
                </div>
                <div class="board">
                    <a href="#" onclick="viewNotice(${notice.noticeNo})">
                        <div class="title">${notice.noticeTitle}</div>
                        <div class="content">${truncatedContent}</div>
                    </a>
                </div>
                <div class="thumb">
                    <a href="#" onclick="viewNotice(${notice.noticeNo})">
                        <img src="${thumbnailSrc}" alt="" data-secure-image="${dataSecureImage}" style="aspect-ratio: 670/420; object-fit: contain; object-position: center;">
                    </a>
                </div>
            </div>
        `;
    }).join('');
    
    noticeList.innerHTML = noticeHTML;
    
    // 공지사항 이미지들도 보안 URL로 업데이트 (기본 이미지와 base64 이미지가 아닌 경우만)
    const noticeImages = noticeList.querySelectorAll('img[data-secure-image]');
    for (const img of noticeImages) {
        const imageFile = img.getAttribute('data-secure-image');
        // 기본 이미지와 base64 이미지가 아닌 경우에만 보안 URL 적용
        if (imageFile !== 'area5-thumb1.png' && imageFile !== 'base64-image') {
            getSecureImageUrl(imageFile).then(secureUrl => {
                img.src = secureUrl;
            }).catch(error => {
                console.warn('보안 URL 가져오기 실패:', error);
                // 실패 시 원본 URL 유지
            });
        }
    }
}

// 공지사항 섹션용 오류 표시 함수
function displayErrorForSection(message) {
    const noticeList = document.querySelector('#noticeSection #noticeList');
    if (noticeList) {
        noticeList.innerHTML = `<div class="error">${message}</div>`;
    }
}

// 공지사항 상세보기 함수
function viewNotice(noticeNo) {

    // 상세페이지로 이동
    window.location.href = `/sub0303.html?noticeNo=${noticeNo}`;
}

