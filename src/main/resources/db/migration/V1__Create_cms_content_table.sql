-- CMS 컨텐츠 관리를 위한 테이블 생성
CREATE TABLE cms_content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    page_code VARCHAR(50) NOT NULL UNIQUE COMMENT '페이지 코드 (예: sub0101)',
    page_title VARCHAR(200) NOT NULL COMMENT '페이지 제목',
    page_content LONGTEXT NOT NULL COMMENT '페이지 컨텐츠 (HTML)',
    meta_title VARCHAR(200) COMMENT '메타 타이틀',
    meta_description TEXT COMMENT '메타 설명',
    meta_keywords VARCHAR(500) COMMENT '메타 키워드',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '상태 (ACTIVE, INACTIVE)',
    created_by VARCHAR(100) COMMENT '생성자',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_by VARCHAR(100) COMMENT '수정자',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    INDEX idx_page_code (page_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CMS 컨텐츠 관리 테이블';

-- 테스트용 데이터 삽입 (sub0101.html의 컨텐츠)
INSERT INTO cms_content (page_code, page_title, page_content, meta_title, meta_description, meta_keywords, created_by) VALUES 
('sub0101', '회사소개', 
'<div class="pgintro">
    <h3>Innovating <br class="mo">Solutions, <br class="mo">Inspiring Value</h3>
    <p>케이원은 IT 시스템 통합(SI), <br class="mo">네트워크 인프라, 보안 솔루션, <br class="mo">빅데이터 연구 등 <br>
        다양한 ICT 분야에서 최적의 <br class="mo">서비스를 제공하는 <br class="mo">종합 IT 전문기업입니다. </p>
</div>
<div class="center_frame">          
    <div class="philasophy">
        <p class="l">경영철학</p>
        <p class="r">중심의 경영 철학을 바탕으로 임직원, 파트너, 고객이 함께 성장하는 기업 문화를 만들어갑니다. 또한 사회적 책임을 다하며, 기술과 사람 을 연결해 모두가 함께 발전하는 미래를 지향합니다.</p>
    </div>
    <div class="koreano1">
        <div class="tit">                    
            <h3 class="t">Korea&nbsp;</h3>
            <h3 class="b">No.1</h3>
        </div>
        <p class="subtit">신뢰를 바탕으로 혁신을 실현하며 사람과 사회와 함께 성장하는 기업으로</p>
        <div class="imgs_top">
            <div class="imgs imgs1">
                <p class="tit">Trust</p>
                <p class="txt">케이원은 고객과의 약속을 최우선으로 여기며, 안정적이고 투명한 서비스를 통해 지속 가능한 신뢰를 쌓아갑니다.</p>
            </div>
            <div class="imgs imgs2">
                <p class="tit">Innovation</p>
                <p class="txt">빠르게 변화하는 ICT 환경 속에서 끊임없는 연구와 기술 혁신으로 최적의 솔루션을 제공하고, 고객의 미래 경쟁력을 강화합니다.</p>
            </div>
            <div class="imgs imgs3">
                <p class="tit">People</p>
                <p class="txt">사람이 곧 기업의 힘이라는 신념 아래, 임직원들의 역량 개발과 협력을 존중하며 함께 성장하는 기업 문화를 만들어갑니다.</p>
            </div>
        </div>
        <div class="imgs_bot">
            <div class="imgs imgs4">
                <p class="tit">Responsibility</p>
                <p class="txt">케이원은 공공·민간 ICT 인프라 구축을 통해 안전하고 편리한 사회를 만드는 데 기여합니다.</p>
            </div>
            <div class="imgs imgs5">
                <p class="tit">Challenge</p>
                <p class="txt">새로운 가능성에 도전하고, 실패를 두려워하지 않으며 한계를 넘어서는 성장을 통해 글로벌 ICT 기업으로 도약합니다.</p>
            </div>
        </div>
    </div>
</div>
<div class="dashboard">
    <div class="l">
        <h3>주요현황</h3>
        <p>30년의 노하우를 담아,<br>
            "대한민국 최고"를 향한 도전을 <br>
            이어나가도록 하겠습니다.  </p>
        <span class="year">2025년 기준</span>
    </div>
    <div class="r">
        <ul>
            <li>
                <p class="tit">업력</p>
                <p class="num">30 <span>년</span></p>
            </li>
            <li>
                <p class="tit">신용등급</p>
                <p class="num">AAA-</p>
            </li>
            <li>
                <p class="tit">임직원수</p>
                <p class="num">1,154 <span>명</span></p>
            </li>
            <li>
                <p class="tit">연매출</p>
                <p class="num">1,312 <span>억</span></p>
            </li>
        </ul>
    </div>
</div>
<div class="profile center_frame">
    <div class="compo l">
        <img src="./images/sub0101_prophile1.svg" alt="">
        <h3>COMPANY PROFILE</h3>
        <p>회사소개서</p>
        <a href="">PDF DOWNLOAD<i class="mso">vertical_align_bottom</i></a>
    </div>
    <div class="compo r">  
        <img src="./images/sub0101_prophile2.svg" alt="">
        <h3>PRODUCT BROCHURE</h3>
        <p>제품소개서</p>                 
        <a href="">PDF DOWNLOAD<i class="mso">vertical_align_bottom</i></a>
    </div>
</div>
<div class="quick_move">
    <button class="l" type="button"><img src="./images/quick_arr_l.svg" alt=""> 케이원연혁</button>
    <button class="r" type="button">인사말 <img src="./images/quick_arr_r.svg" alt=""></button>
</div>', 
'회사소개 - 케이원', 
'케이원은 IT 시스템 통합(SI), 네트워크 인프라, 보안 솔루션, 빅데이터 연구 등 다양한 ICT 분야에서 최적의 서비스를 제공하는 종합 IT 전문기업입니다.', 
'케이원, 회사소개, IT시스템통합, 네트워크인프라, 보안솔루션, 빅데이터', 
'admin');
