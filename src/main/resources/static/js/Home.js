function closePopup3(popupId) {
  console.log("Attempting to close popup with ID:", popupId); // 추가된 로그
  var popup = document.getElementById(popupId);
  if (popup) {
    console.log("Popup found:", popup); // 추가된 로그
    popup.style.display = "none";
  } else {
    console.log("Popup element with ID " + popupId + " not found.");
  }
}
//푸터 여는 js
document.addEventListener("DOMContentLoaded", function () {
  // 초기 팝업 숨기기
  const popups = [
    "bg_gray",
    "bg_gray2",
    "bg_gray3",
    "bg_gray4",
    "bg_gray5",
    "bg_gray6",
    "bg_gray7",
    "bg_gray8",
    "bg_gray9",
    "bg_gray10",
    "bg_gray11",
  ];
  popups.forEach(function (popupId) {
    const popup = document.getElementById(popupId);
    if (popup) popup.style.display = "none";
  });

  // 로그인 상태 확인
  checkLoginStatus();

  // 로그인/회원가입 팝업 전환 이벤트 리스너 등록
  const loginLabel = document.getElementById("loginLabel");
  const signupLink = document.querySelector("#join_membership a");
  const loginLink = document.querySelector("#secondback a");

  if (loginLabel) {
    loginLabel.addEventListener("click", function () {
      showLoginPopup();
      document.body.style.overflow = "hidden"; // 스크롤 비활성화
    });
  }

  if (signupLink) {
    signupLink.addEventListener("click", function (event) {
      event.preventDefault();
      showJoinPopup(); // 회원가입 팝업 표시
    });
  }

  if (loginLink) {
    loginLink.addEventListener("click", function (event) {
      event.preventDefault();
      showLoginPopup(); // 로그인 팝업 표시
    });
  }

  // 이메일 찾기 및 비밀번호 찾기 팝업 전환
  const findEmailLink = document.querySelector("#findemail");
  const findPwLink = document.querySelector("#findpw");
  const emailFindBackBtn = document.querySelector("#emailfindback");
  const emailFindCheckBtn = document.querySelector("#emailfindcheckbtn");

  if (findEmailLink) {
    findEmailLink.addEventListener("click", function (event) {
      event.preventDefault();
      openEmailFindPopup();
    });
  }

  if (emailFindBackBtn) {
    emailFindBackBtn.addEventListener("click", function (event) {
      event.preventDefault();
      returnToLoginFromEmailFind();
    });
  }

  if (emailFindCheckBtn) {
    emailFindCheckBtn.addEventListener("click", function (event) {
      event.preventDefault();
      // 이메일 확인 팝업으로 전환하는 로직 추가
      togglePopup("bg_gray7", "bg_gray6"); // bg_gray7: 이메일 확인 팝업, bg_gray6: 이메일 찾기 팝업
    });
  }

  // 로그아웃 버튼 이벤트 리스너 등록
  const logoutButton = document.querySelector("form[action='/logout'] button");
  if (logoutButton) {
    logoutButton.addEventListener("click", function (event) {
      event.preventDefault();
      logout();
    });
  }

  // 닫기 버튼 이벤트 리스너 등록 (X 버튼)
  const closeButton = document.querySelector("#close");
  if (closeButton) {
    closeButton.addEventListener("click", function () {
      resetJoinForm(); // 폼 초기화
      document.getElementById("bg_gray").style.display = "none";
      document.getElementById("bg_gray2").style.display = "none";
    });
  }

  // 팝업 전환 함수
  function togglePopup(popupIdToShow, popupIdToHide) {
    const popupToShow = document.getElementById(popupIdToShow);
    const popupToHide = document.getElementById(popupIdToHide);

    if (popupToHide) popupToHide.style.display = "none";
    if (popupToShow) popupToShow.style.display = "block";
  }

  function showJoinPopup() {
    togglePopup("bg_gray2", "bg_gray");
  }

  function showLoginPopup() {
    togglePopup("bg_gray", "bg_gray2");
  }

  function openEmailFindPopup() {
    togglePopup("bg_gray6", "bg_gray");
  }

  function returnToLoginFromEmailFind() {
    togglePopup("bg_gray", "bg_gray6");
  }

  // 로그아웃 처리 함수
  function logout() {
    fetch("/logout", {
      method: "POST",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    })
      .then((response) => {
        if (response.ok) {
          // 로그아웃 성공 시
          alert("로그아웃이 완료되었습니다!"); // 로그아웃 성공 메시지
          location.href = "/home"; // 홈 페이지로 리디렉션
        } else {
          // 로그아웃 실패 시
          alert("로그아웃에 실패했습니다. 다시 시도해 주세요.");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("로그아웃 중 오류가 발생했습니다. 다시 시도해 주세요.");
      });
  }

  // 로그인 상태 확인 함수
  function checkLoginStatus() {
    fetch("/api/user/profile")
      .then((response) => response.json())
      .then((data) => {
        if (data.isLoggedIn) {
          login(data.profileImageUrl);
        }
      })
      .catch((error) => console.error("Error:", error));
  }

  // 로그인 처리 함수
  function login(profileImageUrl) {
    const checkbox = document.getElementById("join");
    const loginLabel = document.getElementById("loginLabel");
    const uploadbtn = document.getElementById("uploadbtn");
    const profilebtn = document.querySelector(".imagebtn");

    checkbox.checked = true;
    loginLabel.style.width = "36px";
    loginLabel.style.height = "36px";
    loginLabel.style.padding = "0";
    loginLabel.style.fontSize = "0";
    loginLabel.style.backgroundImage = `url(${profileImageUrl})`;
    loginLabel.style.backgroundSize = "cover";
    loginLabel.style.backgroundPosition = "center";

    uploadbtn.style.display = "block";
    profilebtn.style.display = "block";
  }

  // 생년월일 선택 시 hidden input 요소에 값 설정
  function selectBirthday() {
    const year = document.getElementById("birth-year").value;
    const month = document.getElementById("birth-month").value;
    const day = document.getElementById("birth-day").value;
    const birth = `${year}-${month}-${day}`;
    document.getElementById("birth_input").value = birth;
  }
});

function goToRisePage(button) {
  const keyword = button.getAttribute("data-keyword");
  window.location.href = "/rise/" + keyword;
}

function togglePopup(popupId) {
  const popup = document.getElementById(popupId);
  popup.style.display = popup.style.display === "none" ? "block" : "none";
}

function showJoinPopup() {
  togglePopup("loginPopup"); // 기존 로그인 팝업 닫기
  togglePopup("joinPopup"); // 회원가입 팝업 열기
}

function showLoginPopup() {
  togglePopup("joinPopup"); // 기존 회원가입 팝업 닫기
  togglePopup("loginPopup"); // 로그인 팝업 열기
}
document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".profile-img-btn").forEach(function (button) {
    button.addEventListener("click", function () {
      const url = button.getAttribute("data-url");
      window.location.href = url;
    });
  });
});

document.addEventListener("DOMContentLoaded", function () {
  const checkbox = document.getElementById("join");
  const loginLabel = document.getElementById("loginLabel");
  const uploadbtn = document.getElementById("uploadbtn");
  const profilebtn = document.querySelector(".imagebtn");
  const logoutButton = document.querySelector("form[action='/logout'] button");
  const loginPopup = document.getElementById("bg_gray");

  // 디버깅 메시지 추가
  console.log("DOM fully loaded and parsed");

  // 로그인 버튼 클릭 시 팝업 표시
  loginLabel.addEventListener("click", function () {
    console.log("Login label clicked");
    loginPopup.style.display = "block";
    document.body.style.overflow = "hidden"; // 스크롤 비활성화
  });

  // 로그인 성공 시 호출할 함수
  function login(profileImageUrl) {
    console.log("Login function called with profileImageUrl:", profileImageUrl);
    checkbox.checked = true;
    loginLabel.style.width = "36px";
    loginLabel.style.height = "36px";
    loginLabel.style.padding = "0";
    loginLabel.style.fontSize = "0";
    loginLabel.style.backgroundImage = `url(${profileImageUrl})`;
    loginLabel.style.backgroundSize = "cover";
    loginLabel.style.backgroundPosition = "center";

    uploadbtn.style.display = "block";
    profilebtn.style.display = "block";
  }

  // function checkLoginStatus() {
  //   fetch("/api/user/profile") // 서버에서 로그인 상태와 프로필 이미지를 가져오는 API 엔드포인트
  //     .then((response) => response.json())
  //     .then((data) => {
  //       console.log("checkLoginStatus data:", data);
  //       if (data.isLoggedIn) {
  //         // 로그인 성공 시
  //         document.querySelector(".followingbtn").style.display =
  //           "inline-block"; // 팔로잉 버튼 표시
  //         document.getElementById("uploadbtn").style.display = "inline-block"; // 업로드 버튼 표시
  //         document.querySelector(".imagebtn").style.display = "inline-block"; // 마이페이지 버튼 표시
  //         document.getElementById("loginLabel").style.display = "none"; // 로그인 버튼 숨기기
  //         document.getElementById("dropdownMenu").style.display = "block"; // 드롭다운 메뉴 표시
  //       } else {
  //         // 로그인 실패 시
  //         document.getElementById("uploadbtn").style.display = "none"; // 업로드 버튼 숨기기
  //         document.querySelector(".imagebtn").style.display = "none"; // 마이페이지 버튼 숨기기
  //         document.getElementById("loginLabel").style.display = "inline-block"; // 로그인 버튼 표시
  //         document.getElementById("dropdownMenu").style.display = "none"; // 드롭다운 메뉴 숨기기
  //       }
  //     })
  //     .catch((error) => console.error("Error:", error));
  // }

  // // 페이지가 로드될 때 로그인 상태를 확인
  // window.onload = function () {
  //   checkLoginStatus();
  // };
  function togglePopup1(popupId) {
    const popup = document.getElementById(popupId);
    const dropdownMenu = document.getElementById("dropdownMenu");

    if (popup.style.display === "none" || popup.style.display === "") {
      popup.style.display = "block";
      dropdownMenu.style.display = "none"; // 드롭다운 메뉴 숨기기
    } else {
      popup.style.display = "none";
    }
  }

  function toggleDropdown() {
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display =
      dropdownMenu.style.display === "block" ? "none" : "block";
  }

  function checkLoginStatus() {
    fetch("/api/user/profile")
      .then((response) => response.json())
      .then((data) => {
        if (data.isLoggedIn) {
          document.querySelector(".followingbtn").style.display =
            "inline-block";
          document.getElementById("uploadbtn").style.display = "inline-block";
          document.querySelector(".imagebtn").style.display = "inline-block";

          // 로그인 성공 후에는 팝업을 표시하지 않고 드롭다운 메뉴만 표시
          document.getElementById("loginLabel").onclick = toggleDropdown;
        } else {
          // 로그인 전에는 로그인 버튼을 클릭하면 팝업을 띄움
          document.getElementById("loginLabel").onclick = function () {
            togglePopup1("loginPopup");
          };
        }
      })
      .catch((error) => console.error("Error:", error));
  }

  window.onload = function () {
    checkLoginStatus();
  };

  // 로그아웃 버튼 클릭 시 로그아웃 처리
  logoutButton.addEventListener("click", function (event) {
    event.preventDefault(); // 기본 제출 동작 방지
    fetch("/logout", {
      method: "POST",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    }).then((response) => {
      if (response.ok) {
        // 로그아웃 성공 시 동작
        location.href = "/home"; // 로그아웃 성공 후 이동할 페이지
      } else {
        alert("로그아웃 완료!");
        location.href = "/home";
      }
    });
  });
});

// 로그인 창 입력값 확인 후 로그인 버튼 활성화
function submitForm(event) {
  event.preventDefault(); // 폼 기본 제출 동작 방지
  // 입력값 가져오기
  var emailInput = document.getElementById("email_input");
  var pwInput = document.getElementById("pw_input");

  // 디버깅 메시지 추가
  console.log("submitForm called");

  // 입력값이 모두 채워져 있는지 확인
  if (emailInput.value.trim() !== "" && pwInput.value.trim() !== "") {
    // 로그인 처리 등의 작업 수행 후, 창 닫기
    document.getElementById("bg_gray").style.display = "none";
    document.body.style.overflow = ""; // 스크롤 활성화

    // 입력 필드 초기화
    emailInput.value = "";
    pwInput.value = "";
    document.getElementById("join").checked = false;

    // 로그인 성공 시 프로필 이미지로 대체
    checkLoginStatus();
  }
}

// 초기 팝업창들 숨기기
// document.addEventListener("DOMContentLoaded", function () {
//   document.getElementById("loginPopup").style.display = "none";
//   document.getElementById("joinPopup").style.display = "none";
//   document.getElementById("termsPopup").style.display = "none";
//   document.getElementById("privacyPopup").style.display = "none";
//   document.getElementById("joinfinishPopup").style.display = "none";
//   document.getElementById("emailFindPopup").style.display = "none";
//   document.getElementById("emailCheckPopup").style.display = "none";
//   document.getElementById("pwFindPopup").style.display = "none";
//   document.getElementById("pwfindsendnotice_popup").style.display = "none";
// });

//팝업창 닫으면 스크롤 활성화
function enableScroll() {
  document.body.style.overflow = "auto"; // 스크롤 활성화
}

//팝업창 열고 닫는 이벤트
document.addEventListener("DOMContentLoaded", function () {
  var Joinbtn = document.querySelector("#join"); //로그인버튼
  var signupLink = document.querySelector("#join_membership a");
  var loginLink = document.querySelector("#secondback a");
  var TermsOfUseOpenLink = document.querySelector("#container2 a");
  var TermsOfUseCloseLink = document.querySelector("#Usebackarrow");
  var TermsOfUseAgreeCloseLink = document.querySelector("#UseAgree_btn");
  var PrivacyOpenLink = document.querySelector("#container1 a");
  var PrivacyCloseLink = document.querySelector("#Privacybackarrow");
  var PrivacyAgreeCloseLink = document.querySelector("#PrivacyAgree_btn");
  var JoinFinishBtn = document.querySelector("#joinfinishbtn");
  var GoToLoginBtn = document.querySelector("#gotologinbtn"); //회원가입완료
  var FindEmailLink = document.querySelector("#findemail");
  var FindPwLink = document.querySelector("#findpw");
  // var EmailFindBackBtn = document.querySelector("#emailfindback");
  var EmailFindCheckBtn = document.querySelector("#emailfindcheckbtn");
  var FindPwBtn = document.querySelector("#findpwbtn");
  var GoToLoginBtn2 = document.querySelector("#gotologinbtn2"); //이메일확인
  var PwFindCheckBtn = document.querySelector("#pwfindcheckbtn");
  var PwFindSendNoticecBtn = document.querySelector("#pwfindsendnoticecbtn");

  var loginPopup = document.getElementById("bg_gray");
  var joinPopup = document.getElementById("bg_gray2");
  var TermsOfUsePopup = document.getElementById("bg_gray3");
  var PrivacyPopup = document.getElementById("bg_gray4");
  var joinfinishPopup = document.getElementById("bg_gray5");
  var emailFindPopup = document.getElementById("bg_gray6");
  var emailCheckPopup = document.getElementById("bg_gray7");
  var pwFindPopup = document.getElementById("bg_gray8");
  var pwFindSendNoticePopup = document.getElementById("bg_gray9");

  // 로그인 버튼 누르면 로그인 팝업창 열기
  Joinbtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "block"; // 로그인 팝업 보이기
    // joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
  });
  // 로그인 팝업 창에서 회원가입 페이지로 이동하는 링크 클릭 시 이벤트 처리
  signupLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
  });
  // 회원가입 창에서 로그인 페이지로 돌아가기 링크를 누르면 로그인 팝업 창 열기
  loginLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "block"; // 로그인 팝업 보이기
    joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
  });
  // 개인정보 취급방침창 열기
  TermsOfUseOpenLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
    TermsOfUsePopup.style.display = "block"; // 개인정보 취급방침 팝업 보이기
  });
  // 개인정보 취급방침창 닫기
  TermsOfUseCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
  });
  // 개인정보 취급방침 동의 버튼 눌러서 창 닫기
  TermsOfUseAgreeCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
  });
  // 이용약관 창 열기
  PrivacyOpenLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
    PrivacyPopup.style.display = "block"; // 이용약관 팝업 보이기
  });
  // 이용약관 창 닫기
  PrivacyCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 이용약관 동의버튼 눌러서 창 닫기
  PrivacyAgreeCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 회원가입 버튼 누르면 회원가입 완료 팝업창 열기
  JoinMembershipBtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
    joinfinishPopup.style.display = "block"; // 회원가입 확인 팝업 보이기
    resetJoinForm(); // 회원가입 입력값 초기화
  });
  // 회원가입 완료 팝업창에서 닫기 버튼 누르기
  JoinFinishBtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinfinishPopup.style.display = "none"; // 회원가입 확인 팝업 숨기기
  });
  // 회원가입 완료 팝업창에서 로그인 버튼 누르기
  GoToLoginBtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "block"; // 로그인 팝업 보이기
    joinfinishPopup.style.display = "none"; // 회원가입 확인 팝업 숨기기
  });
  // 로그인 팝업창에서 이메일 찾기 팝업창으로 넘어가기
  FindEmailLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    emailFindPopup.style.display = "block"; // 이메일 찾기 팝업 보이기
  });
  // 로그인 팝업창에서 비밀번호 찾기 팝업창으로 넘어가기
  FindPwLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    pwFindPopup.style.display = "block"; // 비밀번호 찾기 팝업 보이기
  });
  // // 이메일 찾기 팝업창에서 로그인 팝업창으로 넘어가기
  // EmailFindBackBtn.addEventListener("click", function (event) {
  //   event.preventDefault(); // 링크의 기본 동작 방지
  //   loginPopup.style.display = "block"; // 로그인 팝업 보이기
  //   emailFindPopup.style.display = "none"; // 이메일 찾기 팝업 숨기기
  // });
  // 이메일 찾기 팝업창에서 확인 버튼 눌러서 이메일 확인 팝업창으로 넘어가기
  EmailFindCheckBtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    emailCheckPopup.style.display = "block"; // 이메일 확인 팝업 보이기
    emailFindPopup.style.display = "none"; // 이메일 찾기 팝업 숨기기
  });
  // 이메일 확인 팝업창에서 비밀번호 찾기 팝업창으로 넘어가기
  FindPwBtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    emailCheckPopup.style.display = "none"; // 이메일 확인 팝업 숨기기
    pwFindPopup.style.display = "block"; // 비밀번호 찾기 팝업 보이기
  });
  // 이메일 확인 팝업창에서 로그인 팝업창으로 넘어가기
  GoToLoginBtn2.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "block"; // 로그인 팝업 보이기
    emailCheckPopup.style.display = "none"; // 이메일 확인 팝업 숨기기
  });
  // // 비밀번호 찾기 팝업창에서 뒤로가기 버튼 누르면 로그인 팝업창을 넘어가기
  // PwFindBackBtn.addEventListener("click", function (event) {
  //   event.preventDefault(); // 링크의 기본 동작 방지
  //   loginPopup.style.display = "block"; // 로그인 팝업 보이기
  //   pwFindPopup.style.display = "none"; // 비밀번호 찾기 팝업 숨기기
  // });
  //비밀번호 찾기 팝업창에서 확인 버튼 누르면 비밀번호 전송 완료 팝업창으로 넘어가기
  PwFindCheckBtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    pwFindPopup.style.display = "none"; // 비밀번호 찾기 팝업 숨기기
    pwFindSendNoticePopup.style.display = "block"; // 비밀번호 전송 완료 팝업 보이기
  });
  //비밀번호 전송 완료 팝업창에서 확인버튼 누르면 창 닫히기
  PwFindSendNoticecBtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    pwFindSendNoticePopup.style.display = "none"; // 비밀번호 전송 완료 팝업 숨기기
  });
});

// 회원가입 팝업창(join_popup)
// 이메일 파트
const input = document.querySelector("#email_input1");
const p = document.querySelector(".emailcheck-content");
let value;

const isEmail = (value) => {
  return value.indexOf("@") > 1 && value.split("@")[1].indexOf(".") > 1;
};

input.addEventListener("keyup", (event) => {
  value = event.currentTarget.value;

  if (isEmail(value)) {
    p.textContent = `사용 가능한 이메일입니다.`;
    p.style.display = "block"; // 또는 "inline"
  } else {
    p.textContent = ``;
    p.style.display = "none"; // 또는 "inline"
  }
});

// 비밀번호 파트
document.getElementById("join_pw_input").addEventListener("keyup", checkInputs);
function checkInputs() {
  var pw = document.getElementById("join_pw_input").value;
  var specialCharacters = /[!@#$%^&*(),.?":{}|<>]/; // 특수 문자 정규식
  var checkText = document.getElementById("text24"); // 특수문자 포함 문구의 span 요소
  var checkNumber = document.getElementById("text25");
  var imageContainer1 = document.getElementById("imageContainer1");
  var imageContainer2 = document.getElementById("imageContainer2");
  // 첫 번째 이미지 요소 가져오기
  var checkImage1 = document.getElementById("check_img1");

  // 첫 번째 이미지에 패딩 추가
  checkImage1.style.paddingLeft = "5px";
  checkImage1.style.paddingTop = "7px";
  checkImage1.style.paddingBottom = "7px";

  // 두 번째 이미지 요소 가져오기
  var checkImage2 = document.getElementById("check_img2");

  // 두 번째 이미지에 패딩 추가
  checkImage2.style.paddingLeft = "5px";
  checkImage2.style.paddingTop = "7px";
  checkImage2.style.paddingBottom = "7px";

  if (specialCharacters.test(pw)) {
    checkText.style.color = "#448fff"; // 특수 문자가 포함될 때 색상 변경
    imageContainer1.style.display = "block"; // 이미지 표시
  } else {
    checkText.style.color = "#c1c1c1"; // 특수 문자가 포함되지 않을 때 기본 색상으로 변경
    imageContainer1.style.display = "none"; // 이미지 숨기기
  }

  if (pw.length >= 8) {
    checkNumber.style.color = "#448fff"; // 특수 문자가 포함될 때 색상 변경
    imageContainer2.style.display = "block"; // 이미지 표시
  } else {
    checkNumber.style.color = "#c1c1c1"; // 특수 문자가 포함되지 않을 때 기본 색상으로 변경
    imageContainer2.style.display = "none"; // 이미지 숨기기
  }
}

// 닉네임

//생년월일
// '년' 셀렉트 박스 option 목록 동적 생성
const birthYearEl = document.querySelector("#birth-year");
// option 목록 생성 여부 확인
isYearOptionExisted = false;
birthYearEl.addEventListener("focus", function () {
  // year 목록 생성되지 않았을 때 (최초 클릭 시)
  if (!isYearOptionExisted) {
    isYearOptionExisted = true;
    for (var i = 1920; i <= 2024; i++) {
      // option element 생성
      const YearOption = document.createElement("option");
      YearOption.setAttribute("value", i);
      YearOption.innerText = i;
      // birthYearEl의 자식 요소로 추가
      this.appendChild(YearOption);
    }
  }
});

// '월' 셀렉트 박스 option 목록 동적 생성
const birthMonthEl = document.querySelector("#birth-month");
// option 목록 생성 여부 확인
isMonthOptionExisted = false;
birthMonthEl.addEventListener("focus", function () {
  // Month 목록 생성되지 않았을 때 (최초 클릭 시)
  if (!isMonthOptionExisted) {
    isMonthOptionExisted = true;
    for (var i = 1; i <= 12; i++) {
      // option element 생성
      const MonthOption = document.createElement("option");
      MonthOption.setAttribute("value", i);
      MonthOption.innerText = i;
      // birthMonthEl의 자식 요소로 추가
      this.appendChild(MonthOption);
    }
  }
});

// '일' 셀렉트 박스 option 목록 동적 생성
const birthDayEl = document.querySelector("#birth-day");
// option 목록 생성 여부 확인
isDayOptionExisted = false;
birthDayEl.addEventListener("focus", function () {
  // Day 목록 생성되지 않았을 때 (최초 클릭 시)
  if (!isDayOptionExisted) {
    isDayOptionExisted = true;
    for (var i = 1; i <= 31; i++) {
      // option element 생성
      const DayOption = document.createElement("option");
      DayOption.setAttribute("value", i);
      DayOption.innerText = i;
      // birthDayEl의 자식 요소로 추가
      this.appendChild(DayOption);
    }
  }
});

// 남녀 확인 버튼
function selectButton(selectedButton) {
  // 모든 버튼에서 'selected' 클래스 제거
  document.getElementById("button1").classList.remove("selected");
  document.getElementById("button2").classList.remove("selected");

  // 선택된 버튼에 'selected' 클래스 추가
  const selectedElement = document.getElementById(`button${selectedButton}`);
  selectedElement.classList.add("selected");

  // 선택한 성별 값을 hidden input에 설정
  document.getElementById("gender_input").value =
    selectedButton === 1 ? "male" : "female";
}

// 개인정보 취급방침, 이용약관 창을 닫으면 check박스 선택되기
document.addEventListener("DOMContentLoaded", function () {
  // 버튼 요소를 가져옴
  var checkButton1 = document.getElementById("PrivacyAgree_btn"); //이용약관
  var checkButton2 = document.getElementById("UseAgree_btn"); //개인정보

  // 체크박스 요소를 가져옴
  var checkBox1 = document.getElementById("checkbtn2"); //이용약관
  var checkBox2 = document.getElementById("checkbtn3"); //개인정보

  // 버튼을 클릭했을 때 체크박스가 체크되도록 이벤트 리스너 등록
  checkButton1.addEventListener("click", function () {
    checkBox1.checked = true;
  });
  checkButton2.addEventListener("click", function () {
    checkBox2.checked = true;
  });
});

// 회원가입 버튼 누를 시 입력값 초기화 함수
function resetJoinForm() {
  document.getElementById("email_input").value = "";
  document.getElementById("pw_input").value = "";
  // 이메일 입력칸 초기화
  document.getElementById("email_input1").value = "";
  // 비밀번호 입력칸 초기화
  document.getElementById("join_pw_input").value = "";
  // 닉네임 입력칸 초기화
  document.getElementById("name_input").value = "";
  // 생년월일 입력칸 초기화
  document.getElementById("birth-year").selectedIndex = 0;
  document.getElementById("birth-month").selectedIndex = 0;
  document.getElementById("birth-day").selectedIndex = 0;
  // 성별 선택 초기화
  document.getElementById("button1").classList.remove("selected");
  document.getElementById("button2").classList.remove("selected");
  document.getElementById("gender_input").value = "";
  // 전화번호 입력칸 초기화
  document.getElementById("phone_input1").value = "";
  document.getElementById("phone_input2").value = "";
  document.getElementById("phone_input3").value = "";
  // 인증번호 입력칸 초기화
  document.getElementById("CertificationNumber_inputp").value = "";
  // 이용약관 체크박스 초기화
  document.getElementById("checkbtn2").checked = false;
  document.getElementById("checkbtn3").checked = false;

  // 비밀번호 조건 스팬 초기화
  document.getElementById("text24").style.color = "#c1c1c1";
  document.getElementById("text25").style.color = "#c1c1c1";

  // 비밀번호 조건 이미지 초기화
  document.getElementById("imageContainer1").style.display = "none";
  document.getElementById("imageContainer2").style.display = "none";

  document.querySelector(".emailcheck-content").style.display = "none";
}

// 회원가입에서 로그인으로 넘어갈 때 입력값 초기화
document.addEventListener("DOMContentLoaded", function () {
  // "로그인 페이지로 돌아가기" 링크 요소를 가져옴
  var loginLink = document.querySelector("#back a");

  // "로그인 페이지로 돌아가기" 링크를 클릭할 때마다 resetJoinForm() 함수 호출
  loginLink.addEventListener("click", function (event) {
    event.preventDefault(); // 기본 동작 방지
    resetJoinForm(); // JavaScript 코드 초기화
  });
});

// 로그인에서 회원가입으로 넘어갈 때 입력값 초기화
document.addEventListener("DOMContentLoaded", function () {
  // "회원가입" 링크 요소를 가져옴
  var signupLink = document.querySelector("#join_membership a");

  // "회원가입" 링크를 클릭할 때마다 resetJoinForm() 함수 호출
  signupLink.addEventListener("click", function (event) {
    event.preventDefault(); // 기본 동작 방지
    resetJoinForm(); // JavaScript 코드 초기화
  });
});

// X버튼을 누르면 모든 입력값 초기화
document.addEventListener("DOMContentLoaded", function () {
  // "close" 버튼 요소를 가져옴
  var closeButton = document.querySelector("#close");

  // "close" 버튼을 클릭할 때마다 모든 내용 초기화
  closeButton.addEventListener("click", function () {
    resetJoinForm(); // JavaScript 코드 초기화
  });
});

// 슬라이드 광고 js코드
const slides = document.querySelectorAll(".slide");
let currentSlide = 0;

function showSlide(n) {
  if (slides.length === 0 || !slides[n]) {
    return; // 슬라이드가 없는 경우 함수를 종료
  }
  slides.forEach((slide) => {
    slide.style.display = "none";
  });
  slides[n].style.display = "block";
}

function nextSlide() {
  currentSlide++;
  if (currentSlide >= slides.length) {
    currentSlide = 0;
  }
  showSlide(currentSlide);
}

function prevSlide() {
  currentSlide--;
  if (currentSlide < 0) {
    currentSlide = slides.length - 1;
  }
  showSlide(currentSlide);
}

// HTML에 슬라이드가 없으면 showSlide 함수 호출을 하지 않음
if (slides.length > 0) {
  showSlide(currentSlide);
}

// 생년월일 선택 시 hidden input 요소에 값 설정
function selectBirthday() {
  const year = document.getElementById("birth-year").value;
  const month = document.getElementById("birth-month").value;
  const day = document.getElementById("birth-day").value;

  // 선택된 생년월일 값을 합쳐서 YYYY-MM-DD 형태로 설정
  const birth = `${year}-${month}-${day}`;

  // hidden input 요소의 값을 설정하여 서버에 전송
  document.getElementById("birth_input").value = birth;
}

function check_pw(password) {
  var pwRegex =
    /^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*\d)[a-zA-Z0-9!@#$%^&*(),.?":{}|<>]{8,}$/;
  return pwRegex.test(password);
}
function validateName(name) {
  var nameRegex = /^[a-zA-Z가-힣]{2,15}$/;
  return nameRegex.test(name);
}

//업로드 팝업창 js 코드 시작
document.addEventListener("DOMContentLoaded", function () {
  var StoryUploadPopup = document.getElementById("Upload_pop_up");
  StoryUploadPopup.style.display = "block"; //업로드 팝업 보이기

  // 업로드 팝업창 js 코드
  var UploadBtn = document.getElementById("uploadbtn");
  var StoryUploadPopup = document.getElementById("bg_gray1");
  UploadBtn.addEventListener("click", function () {
    StoryUploadPopup.style.display = "block";
    document.body.style.overflow = ""; // 스크롤 허용
    resetForm();
  });
  // 닫기 버튼 클릭 시
  const closeButton1 = document.getElementById("uploadclose");
  closeButton1.addEventListener("click", function () {
    StoryUploadPopup.style.display = "none";
    resetForm();
    window.location.reload(); // 페이지 새로고침
  });
  // 폼 제출 핸들러 설정 함수
  function setupSubmitHandler() {
    const form = document.getElementById("uploadForm");
    form.addEventListener("submit", function (event) {
      event.preventDefault();
      uploadPost(event);
    });
  }
  // 이미지 업로드 클릭 오류 해결
  const uploadPlaceholder = document.querySelector(".upload-placeholder");
  uploadPlaceholder.addEventListener("click", function () {
    document.getElementById("input-file").click();
  });
  // '년', '월', '일' 셀렉트 박스 option 목록 동적 생성
  setupUploadDateSelectors();
  setupSubmitHandler();
});
function getURL(e) {
  if (e.files && e.files[0]) {
    let reader = new FileReader();
    reader.onload = function (event) {
      let imgElement = document.getElementById("uploadedImage");
      imgElement.src = event.target.result;
      imgElement.style.display = "block"; // 이미지 보이기
      document.querySelector(".upload-placeholder").classList.add("active");
      document.querySelector(".upload-placeholder div").style.display = "none"; // 이미지 선택시 텍스트를 숨김
      document.querySelector(".upload-placeholder p").style.display = "none"; // 이미지 선택시 텍스트를 숨김
      document.querySelector(".upload-placeholder img").style.display = "none"; // 이미지 선택시 텍스트를 숨김
      document.querySelector(".upload-placeholder label").style.display =
        "none";
    };
    reader.readAsDataURL(e.files[0]);

    // Get presigned URL for S3
    const fileName = encodeURIComponent(e.files[0].name);
    fetch(`/presigned-url?filename=${fileName}`)
      .then((response) => response.text())
      .then((url) => {
        return fetch(url, {
          method: "PUT",
          body: e.files[0],
        });
      })
      .then((response) => {
        if (response.ok) {
          const imageUrl = response.url.split("?")[0];
          document.getElementById("imageUrl").value = imageUrl; // 추가
        } else {
          alert("이미지 업로드에 실패했습니다.");
        }
      });
  }
}

function addTag() {
  let tagInput = document.getElementById("tagInput");
  let tagList = document.getElementById("tagList");
  let tagValue = tagInput.value.trim();
  if (tagValue) {
    let newTag = document.createElement("span");
    newTag.classList.add("spantags");
    newTag.textContent = "#" + tagValue;
    tagList.appendChild(newTag);
    tagInput.value = "";
    let label = document.querySelector('label[for="tagInput"]');
    label.parentNode.insertBefore(newTag, label.nextSibling);
    tagInput.value = "";
    // 추가된 태그를 hidden input에 추가
    let tagsInput = document.getElementById("tags");
    tagsInput.value += (tagsInput.value ? "," : "") + tagValue;
  }
}

function updateTagsInput() {
  let tagList = document.getElementById("tagList");
  let tags = Array.from(tagList.children).map((tag) =>
    tag.textContent.replace("#", "")
  );
  document.getElementById("tags").value = tags.join(",");
}
function uploadPost(event) {
  event.preventDefault();

  let form = event.target;
  let title = form.title.value;
  let date_year = form.date_year.value;
  let date_month = form.date_month.value;
  let date_day = form.date_day.value;
  let location = form.location.value;
  let tags = form.tags.value;
  let description = form.description.value;
  let imageUrl = form.imageUrl.value;
  var formData = new FormData(document.querySelector("form"));

  // 데이터 콘솔에 출력
  console.log(formData);

  // 나머지 코드
  // ...

  // 서버로 전송
  // ...

  // 이벤트 기본 동작 방지 (페이지 새로고침 방지)
  event.preventDefault();

  if (
    !title ||
    !date_year ||
    !date_month ||
    !date_day ||
    !location ||
    !description ||
    !imageUrl
  ) {
    alert("모든 필드를 채워주세요!");
    return false;
  }

  form.submit();
}

function resetForm() {
  const form = document.getElementById("uploadForm");
  form.reset();
  document.getElementById("input-file").value = "";
  document.getElementById("uploadedImage").src = "";
  document.getElementById("uploadedImage").style.display = "none";
  document.getElementById("uploadimg").style.display = "block";
  document.querySelector(".upload-placeholder p").style.display = "block";
  document.querySelector(".upload-placeholder label").style.display = "block";
  document.querySelector(".UploadBlank").style.display = "block";
  const tagList = document.getElementById("tagList");
  tagList.innerHTML = ""; // 기존 태그 초기화
}

function selectDate() {
  const year = document.getElementById("upload-year").value;
  const month = document.getElementById("upload-month").value;
  const day = document.getElementById("upload-day").value;
  const date = `${year}-${month}-${day}`;
  document.getElementById("date_input").value = date;
}
function setupUploadDateSelectors() {
  // '년' 셀렉트 박스 option 목록 동적 생성
  const uploadYearEl = document.querySelector("#upload-year");
  // option 목록 생성 여부 확인
  isYearOptionExisted = false;
  uploadYearEl.addEventListener("focus", function () {
    // year 목록 생성되지 않았을 때 (최초 클릭 시)
    if (!isYearOptionExisted) {
      isYearOptionExisted = true;
      for (var i = 2024; i >= 2000; i--) {
        // option element 생성
        const YearOption = document.createElement("option");
        YearOption.setAttribute("value", i);
        YearOption.innerText = i;
        // uploadYearEl의 자식 요소로 추가
        this.appendChild(YearOption);
      }
    }

    // '월' 셀렉트 박스 option 목록 동적 생성
    const uploadMonthEl = document.querySelector("#upload-month");
    let isMonthOptionExisted = false;
    uploadMonthEl.addEventListener("focus", function () {
      if (!isMonthOptionExisted) {
        isMonthOptionExisted = true;
        for (let i = 1; i <= 12; i++) {
          const MonthOption = document.createElement("option");
          MonthOption.setAttribute("value", i);
          MonthOption.innerText = i;
          this.appendChild(MonthOption);
        }
      }
    });

    // '일' 셀렉트 박스 option 목록 동적 생성
    const uploadDayEl = document.querySelector("#upload-day");
    let isDayOptionExisted = false;
    uploadDayEl.addEventListener("focus", function () {
      if (!isDayOptionExisted) {
        isDayOptionExisted = true;
        for (let i = 1; i <= 31; i++) {
          const DayOption = document.createElement("option");
          DayOption.setAttribute("value", i);
          DayOption.innerText = i;
          this.appendChild(DayOption);
        }
      }
    });
  });
}

function sendVerification() {
  var phone1 = document.getElementById("phone_input1").value.trim(); // 첫 번째 입력 필드 값 (예: '010')
  var phone2 = document.getElementById("phone_input2").value.trim(); // 두 번째 입력 필드 값 (예: '1234')
  var phone3 = document.getElementById("phone_input3").value.trim(); // 세 번째 입력 필드 값 (예: '5678')

  // 앞의 0을 제거하고 +82를 추가하여 Twilio가 요구하는 형식으로 조합
  var phoneNumber = `+82${phone1.substring(1)}${phone2}${phone3}`;

  // 서버에 전화번호를 전송하여 인증번호 요청
  fetch("/api/sendVerification", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ phoneNumber: phoneNumber }),
  })
    .then((response) => response.text())
    .then((data) => {
      alert(data); // 서버로부터 받은 응답 메시지를 출력
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("인증번호 전송 중 오류가 발생했습니다.");
    });
}

//home.html의 급상승 파트의 js 코드
document.addEventListener("DOMContentLoaded", function () {
  const wrapper = document.querySelector(".wrapper");
  const nextBtn = document.getElementById("nextBtn");
  const prevBtn = document.getElementById("prevBtn");

  let currentIndex = 0;
  const itemsToShow = 4;
  const totalItems = document.querySelectorAll("#TopSearchesList").length;
  const itemsPerClick = 4; // 한 번 클릭 시 이동할 요소의 수
  const itemWidth = Math.round(
    document.querySelector(".searchItem").offsetWidth
  ); // 각 searchItem의 가로 크기 (padding 포함)

  nextBtn.addEventListener("click", () => {
    if (currentIndex < totalItems - itemsToShow) {
      currentIndex += itemsPerClick;
      updateCarousel();
    }
  });

  prevBtn.addEventListener("click", () => {
    if (currentIndex > 0) {
      currentIndex -= itemsPerClick;
      updateCarousel();
    }
  });

  function updateCarousel() {
    const offset = currentIndex * itemWidth;
    wrapper.style.transform = `translateX(-${offset}px)`; // 전체 그룹을 이동
    checkButtons();
  }

  function checkButtons() {
    prevBtn.style.display = currentIndex === 0 ? "none" : "block";
    nextBtn.style.display =
      currentIndex >= totalItems - itemsToShow ? "none" : "block";
  }

  // 초기 버튼 상태 설정
  checkButtons();
});

// // 이메일 찾기 팝업창 js코드
// '010xxxxxxxx' 형식으로 입력된 번호를 '+8210xxxxxxxx' 형식으로 변환
function formatPhoneNumber(phoneNumber) {
  if (phoneNumber.startsWith("010")) {
    return "+82" + phoneNumber.slice(1);
  }
  return phoneNumber;
}

// 팝업 닫기 및 input 필드 리셋
function closePopup() {
  resetInputs(); // 입력 필드 리셋
  document.getElementById("bg_gray6").style.display = "none";
  document.body.style.overflow = "auto"; // 스크롤 활성화
}

// input 필드 리셋
function resetInputs() {
  document.getElementById("phone_input11").value = "";
  document.getElementById("phone_input21").value = "";
  document.getElementById("phone_input31").value = "";
  document.getElementById("CertificationNumber_inputp").value = "";
  document.getElementById("sendStatus").textContent = "";
  document.getElementById("verifyStatus").textContent = "";
}

async function sendVerificationCode() {
  let phone1 = document.getElementById("phone_input11").value;
  let phone2 = document.getElementById("phone_input21").value;
  let phone3 = document.getElementById("phone_input31").value;
  let phoneNumber = formatPhoneNumber(phone1 + phone2 + phone3);

  const response = await fetch("/api/sendVerificationCode", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ phoneNumber }),
  });

  const result = await response.text();
  document.getElementById("sendStatus").textContent = result;
}
async function verifyCode() {
  let phone1 = document.getElementById("phone_input11").value;
  let phone2 = document.getElementById("phone_input21").value;
  let phone3 = document.getElementById("phone_input31").value;
  let phoneNumber = formatPhoneNumber(phone1 + phone2 + phone3);

  const verificationCode = document.getElementById(
    "CertificationNumber_inputp"
  ).value;

  const response = await fetch("/api/verifyCode", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ phoneNumber, verificationCode }), // 서버로 인증번호 전송
  });

  const result = await response.text(); // 서버 응답 받기

  // 서버 응답에 이메일이 포함된 경우 인증 성공으로 처리
  if (result.includes("@")) {
    // 팝업 전환: bg_gray6 팝업 닫고 bg_gray7 팝업 열기
    document.getElementById("bg_gray6").style.display = "none"; // 기존 팝업 닫기
    document.getElementById("bg_gray7").style.display = "block"; // 새 팝업 열기
    console.log("팝업 전환 완료");

    // 팝업 전환 후 이메일 설정
    setTimeout(function () {
      const emailStatusElement = document.getElementById("verifyStatus");
      if (emailStatusElement) {
        emailStatusElement.textContent = result; // 이메일을 UI에 표시
      }
    }, 100); // 팝업이 제대로 열릴 수 있는 짧은 시간 대기 후 설정
  } else {
    console.log("인증 실패 - 인증 결과:", result);
    document.getElementById("verifyStatus").textContent =
      "인증 실패: " + result; // 실패 메시지 표시
  }

  resetInputs(); // 인증 후 입력 필드 리셋
}

//비밀번호 변경 팝업창 js 코드
document.getElementById("inputnewpwp").addEventListener("keyup", checkInputs1p);

function checkInputs1p() {
  var pw2 = document.getElementById("inputnewpwp").value;
  var specialCharacters1 = /[!@#$%^&*(),.?":{}|<>]/; // 특수 문자 정규식
  var checkText1 = document.getElementById("text241"); // 특수문자 포함 문구의 span 요소
  var checkNumber1 = document.getElementById("text251"); // 8자 이상 문구의 span 요소
  var imageContainer11 = document.getElementById("imageContainer11"); // 특수문자 체크 이미지 컨테이너
  var imageContainer21 = document.getElementById("imageContainer21"); // 8자 이상 체크 이미지 컨테이너
  var checkImage11 = document.getElementById("check_img11"); // 특수문자 체크 이미지 요소
  var checkImage21 = document.getElementById("check_img21"); // 8자 이상 체크 이미지 요소

  // 패딩 추가
  checkImage11.style.paddingLeft = "5px";
  checkImage11.style.paddingTop = "7px";
  checkImage11.style.paddingBottom = "7px";
  checkImage21.style.paddingLeft = "5px";
  checkImage21.style.paddingTop = "7px";
  checkImage21.style.paddingBottom = "7px";

  if (specialCharacters1.test(pw2)) {
    checkText1.style.color = "#448fff"; // 특수 문자가 포함될 때 색상 변경
    imageContainer11.style.display = "block"; // 이미지 표시
  } else {
    checkText1.style.color = "#c1c1c1"; // 특수 문자가 포함되지 않을 때 기본 색상으로 변경
    imageContainer11.style.display = "none"; // 이미지 숨기기
  }

  if (pw2.length >= 8) {
    checkNumber1.style.color = "#448fff"; // 8자 이상일 때 색상 변경
    imageContainer21.style.display = "block"; // 이미지 표시
  } else {
    checkNumber1.style.color = "#c1c1c1"; // 8자 미만일 때 기본 색상으로 변경
    imageContainer21.style.display = "none"; // 이미지 숨기기
  }
}

// 비밀번호 유효성 검사 함수
function check_pw1(password) {
  var pwRegex1 =
    /^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*\d)[a-zA-Z0-9!@#$%^&*(),.?":{}|<>]{8,}$/;
  return pwRegex1.test(password);
}

// 비밀번호 일치 여부 확인
document
  .getElementById("inputrecheckpw")
  .addEventListener("input", checkPasswordsMatchp);

// 비밀번호 일치 여부 확인 함수
function checkPasswordsMatchp() {
  const newPassword = document.getElementById("inputnewpwp").value;
  const recheckPassword = document.getElementById("inputrecheckpw").value;
  const validationMessage = document.getElementById("recheckpwparttext2");

  if (newPassword === recheckPassword) {
    validationMessage.style.color = "#448FFF";
    validationMessage.textContent = "비밀번호가 일치합니다.";
  } else {
    validationMessage.style.color = "#FF4F4F";
    validationMessage.textContent = "비밀번호가 일치하지 않습니다.";
  }
}

// 비밀번호 변경 함수 정의
function submitPasswordChange() {
  console.log("submitPasswordChange 함수 호출됨.");

  const currentPw = document.getElementById("inputcurrentpw").value;
  const newPw = document.getElementById("inputnewpwp").value;
  const recheckPw = document.getElementById("inputrecheckpw").value;
  const currentPwError = document.getElementById("currentPwError"); // 오류 메시지 요소 가져오기
  const specialCharacters = /[!@#$%^&*(),.?":{}|<>]/;

  // 비밀번호 유효성 검사
  if (!specialCharacters.test(newPw) || newPw.length < 8) {
    alert("새 비밀번호는 8자 이상이어야 하며, 특수문자를 포함해야 합니다.");
    return;
  }

  // 비밀번호 확인 일치 여부
  if (newPw !== recheckPw) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }

  // AJAX 요청
  console.log("AJAX 요청 시작");

  fetch("/change-password", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      currentPassword: currentPw,
      newPassword: newPw,
    }),
  })
    .then((response) => {
      if (response.ok) {
        console.log("비밀번호 변경 성공");
        alert("비밀번호가 성공적으로 변경되었습니다.");
        resetPasswordFieldsp();
        closePasswordPopup(); // 팝업 닫기
        currentPwError.style.display = "none"; // 성공 시 오류 메시지 숨기기
      } else {
        return response.json().then((data) => {
          console.log("비밀번호 변경 실패", data);
          currentPwError.style.display = "block"; // 오류 메시지 표시
          currentPwError.textContent =
            data.message || "비밀번호 변경에 실패했습니다.";
        });
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      currentPwError.style.display = "block"; // 비밀번호 일치하지 않을 때 메시지 표시
      currentPwError.textContent = "현재 비밀번호가 일치하지 않습니다.";
    });
}

// 비밀번호 입력 필드 및 유효성 검사 초기화
function resetPasswordFieldsp() {
  document.getElementById("inputcurrentpw").value = "";
  document.getElementById("inputnewpwp").value = "";
  document.getElementById("inputrecheckpw").value = "";

  document.getElementById("text241").style.color = "#c1c1c1";
  document.getElementById("imageContainer11").style.display = "none";
  document.getElementById("text251").style.color = "#c1c1c1";
  document.getElementById("imageContainer21").style.display = "none";

  const validationMessage = document.getElementById("recheckpwparttext2");
  if (validationMessage) {
    validationMessage.textContent = "";
  }
}

// 팝업 닫기와 스크롤 활성화
function closePasswordPopup() {
  document.getElementById("bg_gray11").style.display = "none";
  enableScroll();
}

// function checkInputs1p() {
//   const pw2 = document.getElementById("inputnewpwp").value;
//   const specialCharacters = /[!@#$%^&*(),.?":{}|<>]/; // 특수 문자 정규식
//   const checkText1 = document.getElementById("text241"); // 특수문자 포함 문구
//   const checkNumber1 = document.getElementById("text251"); // 8자 이상 문구

//   // 특수 문자 포함 여부 검사
//   if (specialCharacters.test(pw2)) {
//     checkText1.style.color = "#448fff"; // 특수 문자가 포함되면 파란색으로 변경
//   } else {
//     checkText1.style.color = "#c1c1c1"; // 특수 문자가 없으면 회색
//   }

//   // 8자 이상 여부 검사
//   if (pw2.length >= 8) {
//     checkNumber1.style.color = "#448fff"; // 8자 이상이면 파란색으로 변경
//   } else {
//     checkNumber1.style.color = "#c1c1c1"; // 8자 미만이면 회색
//   }
// }

// // 비밀번호 일치 여부 확인 함수
// function checkPasswordsMatch() {
//   const newPassword = document.getElementById("inputnewpw").value;
//   const recheckPassword = document.getElementById("inputrecheckpw").value;
//   const validationMessage = document.getElementById("recheckpwparttext2");

//   if (newPassword === recheckPassword) {
//     validationMessage.style.color = "#448FFF"; // 비밀번호가 일치하면 파란색
//     validationMessage.textContent = "비밀번호가 일치합니다.";
//   } else {
//     validationMessage.style.color = "#FF4F4F"; // 비밀번호가 일치하지 않으면 빨간색
//     validationMessage.textContent = "비밀번호가 일치하지 않습니다.";
//   }
// }
// function showPopupf(popupId) {
//   var popup = document.getElementById(popupId);
//   if (popup) {
//     popup.style.display = "block";
//   } else {
//     console.log("Popup element with ID " + popupId + " not found.");
//   }
// }

// function checkPasswordsMatchp() {
//   var newPassword = document.getElementById("inputnewpwp").value;
//   var recheckPassword = document.getElementById("inputrecheckpw").value;
//   var messageElement = document.getElementById("recheckpwparttext2");

//   if (newPassword === recheckPassword) {
//     messageElement.style.color = "#448FFF"; // 일치할 때 색상
//     messageElement.textContent = "비밀번호가 일치합니다.";
//   } else {
//     messageElement.style.color = "#FF4F4F"; // 일치하지 않을 때 색상
//     messageElement.textContent = "비밀번호가 일치하지 않습니다.";
//   }
// }

// // 비밀번호 필드 및 유효성 검사 메시지 초기화 함수
// function resetPasswordFields() {
//   // 비밀번호 입력 필드 초기화
//   document.getElementById("inputcurrentpw").value = "";
//   document.getElementById("inputnewpw").value = "";
//   document.getElementById("inputrecheckpw").value = "";

//   // 유효성 검사 메시지 초기화
//   document.getElementById("recheckpwparttext2").textContent = "";

//   // 특수문자 및 8자 이상 체크 초기화
//   document.getElementById("text241").style.color = "#c1c1c1";
//   document.getElementById("imageContainer11").style.display = "none";
//   document.getElementById("text251").style.color = "#c1c1c1";
//   document.getElementById("imageContainer21").style.display = "none";
// }

// // 비밀번호 유효성 검사 함수
// function checkInputs1p() {
//   const pw2 = document.getElementById("inputnewpwp").value;
//   const specialCharacters = /[!@#$%^&*(),.?":{}|<>]/;
//   const checkText1 = document.getElementById("text241");
//   const checkNumber1 = document.getElementById("text251");

//   if (specialCharacters.test(pw2)) {
//     checkText1.style.color = "#448fff";
//   } else {
//     checkText1.style.color = "#c1c1c1";
//   }

//   if (pw2.length >= 8) {
//     checkNumber1.style.color = "#448fff";
//   } else {
//     checkNumber1.style.color = "#c1c1c1";
//   }
// }

//링크로 받은 비밀번호 변경 js 코드 시작
document
  .getElementById("inputnewpwp11")
  .addEventListener("keyup", checkInputs1p);

function checkInputs1p1() {
  var pw2 = document.getElementById("inputnewpwp11").value;
  var specialCharacters1 = /[!@#$%^&*(),.?":{}|<>]/; // 특수 문자 정규식
  var checkText1 = document.getElementById("text2411"); // 특수문자 포함 문구의 span 요소
  var checkNumber1 = document.getElementById("text2511"); // 8자 이상 문구의 span 요소
  var imageContainer11 = document.getElementById("imageContainer111"); // 특수문자 체크 이미지 컨테이너
  var imageContainer21 = document.getElementById("imageContainer211"); // 8자 이상 체크 이미지 컨테이너
  var checkImage11 = document.getElementById("check_img111"); // 특수문자 체크 이미지 요소
  var checkImage21 = document.getElementById("check_img211"); // 8자 이상 체크 이미지 요소

  // 패딩 추가
  checkImage11.style.paddingLeft = "5px";
  checkImage11.style.paddingTop = "7px";
  checkImage11.style.paddingBottom = "7px";
  checkImage21.style.paddingLeft = "5px";
  checkImage21.style.paddingTop = "7px";
  checkImage21.style.paddingBottom = "7px";

  if (specialCharacters1.test(pw2)) {
    checkText1.style.color = "#448fff"; // 특수 문자가 포함될 때 색상 변경
    imageContainer11.style.display = "block"; // 이미지 표시
  } else {
    checkText1.style.color = "#c1c1c1"; // 특수 문자가 포함되지 않을 때 기본 색상으로 변경
    imageContainer11.style.display = "none"; // 이미지 숨기기
  }

  if (pw2.length >= 8) {
    checkNumber1.style.color = "#448fff"; // 8자 이상일 때 색상 변경
    imageContainer21.style.display = "block"; // 이미지 표시
  } else {
    checkNumber1.style.color = "#c1c1c1"; // 8자 미만일 때 기본 색상으로 변경
    imageContainer21.style.display = "none"; // 이미지 숨기기
  }
}

// 비밀번호 유효성 검사 함수
function check_pw2(password) {
  var pwRegex1 =
    /^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*\d)[a-zA-Z0-9!@#$%^&*(),.?":{}|<>]{8,}$/;
  return pwRegex1.test(password);
}

// 비밀번호 일치 여부 확인
document
  .getElementById("inputrecheckpw1")
  .addEventListener("input", checkPasswordsMatchp);

// 비밀번호 일치 여부 확인 함수
function checkPasswordsMatchp1() {
  const newPassword = document.getElementById("inputnewpwp11").value;
  const recheckPassword = document.getElementById("inputrecheckpw1").value;
  const validationMessage = document.getElementById("recheckpwparttext21");

  if (newPassword === recheckPassword) {
    validationMessage.style.color = "#448FFF";
    validationMessage.textContent = "비밀번호가 일치합니다.";
  } else {
    validationMessage.style.color = "#FF4F4F";
    validationMessage.textContent = "비밀번호가 일치하지 않습니다.";
  }
}

// 비밀번호 변경 함수 정의
function submitPasswordChange1() {
  console.log("submitPasswordChange 함수 호출됨.");

  // const currentPw = document.getElementById("inputcurrentpw").value;
  const newPw = document.getElementById("inputnewpwp11").value;
  const recheckPw = document.getElementById("inputrecheckpw1").value;
  // const currentPwError = document.getElementById("currentPwError"); // 오류 메시지 요소 가져오기
  const specialCharacters = /[!@#$%^&*(),.?":{}|<>]/;

  // 비밀번호 유효성 검사
  if (!specialCharacters.test(newPw) || newPw.length < 8) {
    alert("새 비밀번호는 8자 이상이어야 하며, 특수문자를 포함해야 합니다.");
    return;
  }

  // 비밀번호 확인 일치 여부
  if (newPw !== recheckPw) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }

  // AJAX 요청
  console.log("AJAX 요청 시작");

  fetch("/change-password", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      currentPassword: currentPw,
      newPassword: newPw,
    }),
  })
    .then((response) => {
      if (response.ok) {
        console.log("비밀번호 변경 성공");
        alert("비밀번호가 성공적으로 변경되었습니다.");
        resetPasswordFieldsp();
        closePasswordPopup(); // 팝업 닫기
        currentPwError.style.display = "none"; // 성공 시 오류 메시지 숨기기
      } else {
        return response.json().then((data) => {
          console.log("비밀번호 변경 실패", data);
          currentPwError.style.display = "block"; // 오류 메시지 표시
          currentPwError.textContent =
            data.message || "비밀번호 변경에 실패했습니다.";
        });
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      currentPwError.style.display = "block"; // 비밀번호 일치하지 않을 때 메시지 표시
      currentPwError.textContent = "현재 비밀번호가 일치하지 않습니다.";
    });
}

// // 비밀번호 입력 필드 및 유효성 검사 초기화
// function resetPasswordFieldsp() {
//   document.getElementById("inputcurrentpw").value = "";
//   document.getElementById("inputnewpwp1").value = "";
//   document.getElementById("inputrecheckpw1").value = "";

//   document.getElementById("text241").style.color = "#c1c1c1";
//   document.getElementById("imageContainer11").style.display = "none";
//   document.getElementById("text251").style.color = "#c1c1c1";
//   document.getElementById("imageContainer21").style.display = "none";

//   const validationMessage = document.getElementById("recheckpwparttext2");
//   if (validationMessage) {
//     validationMessage.textContent = "";
//   }
// }

// // 팝업 닫기와 스크롤 활성화
// function closePasswordPopup() {
//   document.getElementById("bg_gray11").style.display = "none";
//   enableScroll();
// }
