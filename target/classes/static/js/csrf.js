/**
 * KITMS CSRF 보호 자동 처리 스크립트
 *
 * 이 스크립트는 웹 폼의 CSRF 보호를 자동으로 처리합니다:
 * - CSRF 토큰 자동 발급
 * - 모든 폼 요청에 CSRF 토큰 자동 추가
 * - fetch 요청에 CSRF 토큰 자동 추가
 * - 토큰 만료 시 자동 갱신
 *
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
(function() {
    'use strict';
    
    let csrfToken = null;
    let csrfHeaderName = 'X-XSRF-TOKEN';
    let csrfParameterName = '_csrf';
    
    /**
     * CSRF 토큰 가져오기
     * 서버에서 CSRF 토큰을 가져와서 저장합니다.
     */
    function getCsrfToken() {
        return fetch('/api/csrf-token', {
            method: 'GET',
            credentials: 'same-origin'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('CSRF 토큰 요청 실패: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            csrfToken = data.token;
            csrfHeaderName = data.headerName || 'X-XSRF-TOKEN';
            csrfParameterName = data.parameterName || '_csrf';
            
            return csrfToken;
        })
        .catch(error => {
            console.error('CSRF 토큰 발급 실패:', error);
            return null;
        });
    }
    
    /**
     * CSRF 토큰 인터셉터 설정
     * 모든 fetch 요청에 자동으로 CSRF 토큰을 추가합니다.
     */
    function setupCsrfInterceptor() {
        const originalFetch = window.fetch;
        
        window.fetch = function(url, options = {}) {
            // API 요청은 제외 (JWT 토큰으로 인증)
            if (url.startsWith('/api/') && !url.includes('/csrf-token')) {
                return originalFetch(url, options);
            }
            
            // 웹 폼 요청에만 CSRF 토큰 추가
            if (csrfToken && (options.method === 'POST' || options.method === 'PUT' || options.method === 'DELETE' || options.method === 'PATCH')) {
                options.headers = options.headers || {};
                options.headers[csrfHeaderName] = csrfToken;
            }
            
            return originalFetch(url, options);
        };
    }
    
    /**
     * 폼에 CSRF 토큰 자동 추가
     * 모든 폼에 숨겨진 CSRF 토큰 필드를 자동으로 추가합니다.
     */
    function setupFormCsrfProtection() {
        const forms = document.querySelectorAll('form');
        
        forms.forEach(form => {
            // 이미 CSRF 토큰이 있는지 확인
            const existingToken = form.querySelector('input[name="' + csrfParameterName + '"]');
            if (existingToken) {
                return; // 이미 있으면 건너뛰기
            }
            
            // CSRF 토큰 필드 추가
            if (csrfToken) {
                const tokenInput = document.createElement('input');
                tokenInput.type = 'hidden';
                tokenInput.name = csrfParameterName;
                tokenInput.value = csrfToken;
                form.appendChild(tokenInput);
            }
        });
    }
    
    /**
     * CSRF 토큰 갱신
     * 토큰이 만료되었을 때 자동으로 갱신합니다.
     */
    function refreshCsrfToken() {
        return getCsrfToken().then(() => {
            setupFormCsrfProtection();
        });
    }
    
    /**
     * 에러 응답 처리
     * CSRF 토큰 관련 에러를 처리합니다.
     */
    function handleCsrfError(response) {
        if (response.status === 403) {
            // CSRF 토큰 에러일 가능성이 높음
            console.warn('CSRF 토큰 에러 감지, 토큰 갱신 시도');
            return refreshCsrfToken();
        }
        return Promise.resolve();
    }
    
    /**
     * 초기화 함수
     * 페이지 로드 시 CSRF 보호를 초기화합니다.
     */
    function initializeCsrfProtection() {
        getCsrfToken()
            .then(() => {
                setupCsrfInterceptor();
                setupFormCsrfProtection();
                
                // 폼 동적 추가 시에도 CSRF 토큰 추가
                const observer = new MutationObserver(function(mutations) {
                    mutations.forEach(function(mutation) {
                        if (mutation.type === 'childList') {
                            mutation.addedNodes.forEach(function(node) {
                                if (node.nodeType === 1) { // Element node
                                    if (node.tagName === 'FORM') {
                                        setupFormCsrfProtection();
                                    } else if (node.querySelectorAll) {
                                        const forms = node.querySelectorAll('form');
                                        if (forms.length > 0) {
                                            setupFormCsrfProtection();
                                        }
                                    }
                                }
                            });
                        }
                    });
                });
                
                observer.observe(document.body, {
                    childList: true,
                    subtree: true
                });
                
            })
            .catch(error => {
                console.error('CSRF 보호 초기화 실패:', error);
            });
    }
    
    // 전역 함수로 노출
    window.KitmsCsrf = {
        getToken: getCsrfToken,
        refreshToken: refreshCsrfToken,
        handleError: handleCsrfError
    };
    
    // 페이지 로드 시 초기화
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initializeCsrfProtection);
    } else {
        initializeCsrfProtection();
    }
    
})();
