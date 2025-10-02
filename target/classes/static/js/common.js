// ■■■■■■■■■■■■■dropdown■■■■■■■■■■■■■■■
// const dropdowns = document.querySelectorAll(".dropdown");
// dropdowns.forEach((dropdown) => {
//     const select = dropdown.querySelector(".select");
//     const caret = dropdown.querySelector(".mso"); // `.mso`로 변경
//     const menu = dropdown.querySelector(".menu");
//     const options = menu.querySelectorAll("li");
//     const selected = dropdown.querySelector(".selected");

//     // 드롭다운 열기/닫기
//     select.addEventListener("click", (event) => {
//         event.stopPropagation(); 
//         menu.classList.toggle("menu-open");
//         caret.classList.toggle("rotate"); // 애니메이션을 위한 클래스 추가/제거
//     });

//     // 옵션 선택
//     options.forEach((option) => {
//         option.addEventListener("click", (event) => {
//             event.stopPropagation(); 
//             selected.classList.remove("placeholder");
//             selected.innerText = option.innerText;
//             menu.classList.remove("menu-open");
//             caret.classList.remove("rotate"); // 화살표 복원
//             options.forEach((option) => option.classList.remove("active"));
//             option.classList.add("active");
//         });
//     });

//     // 드롭다운 외부 클릭 시 닫기
//     document.addEventListener("click", () => {
//         menu.classList.remove("menu-open");
//         caret.classList.remove("rotate"); // 화살표 복원
//     });
// });
function initDropdown(dropdown) {
    const select = dropdown.querySelector(".select");
    const caret = dropdown.querySelector(".mso");
    const menu = dropdown.querySelector(".menu");
    const options = menu.querySelectorAll("li");
    const selected = dropdown.querySelector(".selected");

    select.addEventListener("click", (event) => {
        event.stopPropagation();
        menu.classList.toggle("menu-open");
        caret.classList.toggle("rotate");
    });

    options.forEach((option) => {
        option.addEventListener("click", (event) => {
            event.stopPropagation();
            selected.classList.remove("placeholder");
            selected.innerText = option.innerText;
            menu.classList.remove("menu-open");
            caret.classList.remove("rotate");
            options.forEach((o) => o.classList.remove("active"));
            option.classList.add("active");
            
            // ▼ 슬라이드 닫기 트리거 추가
            const closeBtn = document.querySelector('.slide_wrap .close');
            if (closeBtn) closeBtn.click();
        });
    });

    document.addEventListener("click", () => {
        menu.classList.remove("menu-open");
        caret.classList.remove("rotate");
    });
}
document.querySelectorAll(".dropdown").forEach(initDropdown);





// ■■■■■■■■■■■■■Load the header START■■■■■■■■■■■■■■■
// fetch('header.html')
// .then(response => response.text())
// .then(data => {
//     document.getElementById('header').innerHTML = data;
// });

// // Load the footer
// fetch('footer.html')
// .then(response => response.text())
// .then(data => {
//     document.getElementById('footer').innerHTML = data;
// });




// ■■■■■■■■■■■■■헤더와 푸터를 비동기로 로드■■■■■■■■■■■■■
function loadHeaderAndFooter() {
    // 헤더 로드
    fetch("header.html")
      .then((response) => response.text())
      .then((data) => {
        document.getElementById("header").innerHTML = data;
  
        // 헤더 로드 완료 후 GNB 초기화
        const header = document.querySelector(".header");
        const mobileGnb = document.querySelector(".mobile-gnb");
  
        if (header && mobileGnb) {
          initGnb(header);
          initMobileGnb(mobileGnb);
          console.log("GNB 초기화 완료");
        } else {
          console.error("GNB 초기화 실패: 올바른 DOM 요소를 찾을 수 없습니다.");
        }
      })
      .catch((error) => console.error("헤더 로드 실패:", error));
  
    // 푸터 로드
    fetch("footer.html")
      .then((response) => response.text())
      .then((data) => {
        document.getElementById("footer").innerHTML = data;
      })
      .catch((error) => console.error("푸터 로드 실패:", error));
  }
  
  // ■■■■■■■■■■■■■PC GNB 초기화■■■■■■■■■■■■■
  function initGnb(header) {
    const gnb = header.querySelector(".header-gnb");
    if (!gnb) {
      console.error("GNB 초기화 실패: 'header-gnb' 요소를 찾을 수 없습니다.");
      return;
    }
  
    const gnbMenus = gnb.querySelectorAll(".gnb-depth-1 .depth-1");
  
    [...gnbMenus].forEach((menu) => {
      menu.addEventListener("mouseenter", (event) => {
        gnbOpen(event.target);
      });
      menu.addEventListener("focusin", (event) => {
        gnbOpen(event.target.closest(".depth-1"));
      });
  
      menu.addEventListener("mouseleave", (event) => {
        gnbClose(event.target);
      });
      menu.addEventListener("focusout", (event) => {
        gnbClose(event.target.closest(".depth-1"));
      });
    });
  
    function gnbOpen(target) {
      const targetItem = target.querySelector(".depth-item");
      const targetMenu = target.querySelector(".gnb-depth-2");
      if (!targetItem || !targetMenu) return;
  
      const targetHeight = targetMenu.getBoundingClientRect().height;
      header.classList.add("open");
      target.classList.add("current");
      targetItem.style.height = `${targetHeight}px`;
    }
  
    function gnbClose(target) {
      const targetItem = target.querySelector(".depth-item");
      if (!targetItem) return;
  
      header.classList.remove("open");
      target.classList.remove("current");
      targetItem.style.height = "0px";
    }
  }
  
  // ■■■■■■■■■■■■■Mobile GNB 초기화■■■■■■■■■■■■■
  function initMobileGnb(mobileGnb) {
    const html = document.querySelector("html");
    const sidebarButton = mobileGnb.querySelector(".sidebar-btn");
    const mobileMenuButtons = mobileGnb.querySelectorAll(".depth-1 a");
    const sidebarWrap = mobileGnb.querySelector(".sidebar-wrap");
  
    if (!sidebarButton || !mobileMenuButtons) {
      console.error("Mobile GNB 초기화 실패: 올바른 DOM 요소를 찾을 수 없습니다.");
      return;
    }

    if (sidebarWrap) {
      sidebarWrap.addEventListener("click", (event) => {
        console.log("sidebarWrap 클릭");
        if (event.target === sidebarWrap) {
          mobileGnbClose(mobileGnb);
        }
      });
    }
  
    sidebarButton.addEventListener("click", () => {
      console.log("sidebarButton 클릭");
      if (mobileGnb.classList.contains("open")) {
        mobileGnbClose(mobileGnb);
      } else {
        mobileGnbOpen(mobileGnb);
      }
      // sidebarButton.dataset.bound = "true"; 
    });
  
    mobileMenuButtons.forEach((button) => {
        button.addEventListener("click", (event) => {
          const btn = event.target.closest(".depth-1-link");
          if (!btn) return;
      
          // 서브메뉴가 있는 경우만 아코디언 동작
          const depthTarget = btn.nextElementSibling;
          if (depthTarget) {
            event.preventDefault(); // 기본 링크 동작 차단 (서브메뉴가 있을 때만)
            openAccordion(btn);
          }
        });
      });
      
  
    window.addEventListener("resize", () => {
      if (window.innerWidth > 1024) {
        mobileGnbClose(mobileGnb);
      }
    });
  
    function mobileGnbOpen(gnb) {
      gnb.classList.add("open");
      html.classList.add("not-scroll");
      document.querySelector(".business_10 .tab-container").style.display = "none";
    }
  
    function mobileGnbClose(gnb) {
      gnb.classList.remove("open");
      html.classList.remove("not-scroll");
      document.querySelector(".business_10 .tab-container").style.display = "block"; 
    }
  
    function openAccordion(button) {
      const target = button.parentElement;
      const depthTarget = button.nextElementSibling;
      if (!depthTarget) return;
  
      const otherLinks = siblings(target);
      const otherItems = otherLinks.map((link) => link.querySelector("ul"));
  
      if (target.classList.contains("open")) {
        target.classList.remove("open");
        depthTarget.style.maxHeight = "0px";
      } else {
        otherLinks.forEach((link) => link.classList.remove("open"));
        otherItems.forEach((item) => (item ? (item.style.maxHeight = "0px") : ""));
        target.classList.add("open");
        depthTarget.style.maxHeight = `${depthTarget.scrollHeight}px`;
      }
    }
  }
  
  function siblings(element) {
    return [...element.parentElement.children].filter((child) => child !== element);
  }
  
  // ■■■■■■■■■■■■■페이지 로드 시 실행■■■■■■■■■■■■■
  window.addEventListener("DOMContentLoaded", () => {

      //loadHeaderAndFooter();

  });

