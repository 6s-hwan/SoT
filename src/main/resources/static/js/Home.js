// 현재 페이지 URL이 "/bookmark"이면 imagebtn에 invert 클래스 추가
if (window.location.pathname === '/bookmark') {
  document.querySelector('.imagebtn').classList.add('invert');
}

// 현재 페이지 URL이 "/follows"이면 followingbtn에 invert 클래스 추가
if (window.location.pathname === '/follows') {
  document.querySelector('.followingbtn').classList.add('invert');
}

// 엔터키를 눌렀을 때 로그인 버튼 클릭을 트리거하는 함수
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("loginForm");
  form.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
      event.preventDefault(); // 폼이 자동으로 제출되는 것을 막음
      document.getElementById("login_btn").click(); // 버튼 클릭 트리거
    }
  });
});
// 이메일 찾기 및 비밀번호 찾기 팝업 전환
document.addEventListener("DOMContentLoaded", function () {
  const findEmailLink = document.querySelector("#findemail");
  const findPwLink = document.querySelector("#findpw");

  if (findEmailLink) {
    findEmailLink.addEventListener("click", function (event) {
      event.preventDefault();
      openEmailFindPopup(); // 이메일 찾기 팝업 열기
    });
  }

  if (findPwLink) {
    findPwLink.addEventListener("click", function (event) {
      event.preventDefault();
      openPwFindPopup(); // 비밀번호 찾기 팝업 열기
    });
  }
});

function openEmailFindPopup() {
  document.getElementById("bg_gray6").style.display = "block"; // 이메일 찾기 팝업 열기
  document.getElementById("bg_gray").style.display = "none"; // 로그인 팝업 닫기
}

function openPwFindPopup() {
  document.getElementById("bg_gray8").style.display = "block"; // 비밀번호 찾기 팝업 열기
  document.getElementById("bg_gray").style.display = "none"; // 로그인 팝업 닫기
}

let debounceTimeout;

// 이메일 유효성 및 중복 검사 함수
function checkEmail() {
  var email = document.getElementById("email_input1").value;
  var emailCheckMessage = document.getElementById("emailCheckMessage");

  clearTimeout(debounceTimeout); // 기존 타임아웃을 초기화

  // 입력이 멈춘 후 500ms 후에 유효성 검사 진행
  debounceTimeout = setTimeout(() => {
    const emailPattern = /.+@.+/; // 간단한 이메일 정규식

    if (emailPattern.test(email)) {
      // 이메일 유효성 통과 후, 중복 체크를 위한 서버 요청 추가
      fetch(`/check-email?email=${email}`)
        .then((response) => response.json())
        .then((data) => {
          if (data.isDuplicate) {
            emailCheckMessage.textContent = "중복된 이메일입니다.";
            emailCheckMessage.style.color = "#FF4F4F"; // 빨간색
            emailCheckMessage.style.display = "block";
          } else {
            emailCheckMessage.textContent = "사용 가능한 이메일입니다.";
            emailCheckMessage.style.color = "#448fff"; // 파란색
            emailCheckMessage.style.display = "block";
          }
        });
    } else {
      emailCheckMessage.textContent = "유효하지 않은 이메일 형식입니다.";
      emailCheckMessage.style.color = "#FF4F4F"; // 빨간색
      emailCheckMessage.style.display = "block";
    }
  }, 750); // 500ms (0.5초) 후에 유효성 검사 실행
}

// 이메일 입력 필드에 이벤트 리스너 추가
document.getElementById("email_input1").addEventListener("input", checkEmail);

// 이메일 파트
const input = document.querySelector("#email_input1");
const p = document.querySelector(".emailcheck-content");
let value;
const isEmail = (value) => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailPattern.test(value);
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
// 비밀번호 유효성 검사 함수
function checkInputs1p1() {
  const password = document.getElementById("newPassword").value;
  const specialCharacters = /[!@#$%^&*(),.?":{}|<>]/; // 특수문자 정규식
  const checkTextSpecial = document.getElementById("text2411"); // 특수문자 포함 텍스트
  const checkTextLength = document.getElementById("text2511"); // 8자 이상 텍스트
  const imageContainerSpecial = document.getElementById("imageContainer111"); // 특수문자 체크 이미지
  const imageContainerLength = document.getElementById("imageContainer211"); // 8자 이상 체크 이미지
  const checkImage111 = document.getElementById("check_img111"); // 특수문자 체크 이미지 요소
  const checkImage211 = document.getElementById("check_img211"); // 8자 이상 체크 이미지 요소

  // 패딩 추가
  checkImage111.style.paddingLeft = "5px";
  checkImage111.style.paddingTop = "7px";
  checkImage111.style.paddingBottom = "7px";
  checkImage211.style.paddingLeft = "5px";
  checkImage211.style.paddingTop = "7px";
  checkImage211.style.paddingBottom = "7px";
  // 특수문자 포함 여부 확인
  if (specialCharacters.test(password)) {
    checkTextSpecial.style.color = "#448fff"; // 조건 만족 시 파란색
    imageContainerSpecial.style.display = "block"; // 체크 이미지 표시
  } else {
    checkTextSpecial.style.color = "#c1c1c1"; // 기본 색상 회색
    imageContainerSpecial.style.display = "none"; // 체크 이미지 숨김
  }

  // 8자 이상 여부 확인
  if (password.length >= 8) {
    checkTextLength.style.color = "#448fff"; // 조건 만족 시 파란색
    imageContainerLength.style.display = "block"; // 체크 이미지 표시
  } else {
    checkTextLength.style.color = "#c1c1c1"; // 기본 색상 회색
    imageContainerLength.style.display = "none"; // 체크 이미지 숨김
  }
}

// 비밀번호 확인 일치 여부 확인 함수
function checkPasswordsMatchp1() {
  const newPassword = document.getElementById("newPassword").value;
  const confirmPassword = document.getElementById("confirmPassword").value;
  const validationMessage = document.getElementById("recheckpwparttext21");

  if (newPassword === confirmPassword) {
    validationMessage.style.color = "#448fff";
    validationMessage.textContent = "비밀번호가 일치합니다.";
  } else {
    validationMessage.style.color = "#FF4F4F";
    validationMessage.textContent = "비밀번호가 일치하지 않습니다.";
  }
}
// 팝업 닫기 함수
function closePopupff() {
  document.getElementById("bg_gray5").style.display = "none"; // 회원가입 완료 팝업 닫기
  enableScroll(); // 스크롤 활성화
}

// 로그인 팝업 열기 함수
function goTonewLoginff() {
  closePopupff(); // 회원가입 완료 팝업 닫기
  document.getElementById("bg_gray").style.display = "block"; // 로그인 팝업 열기
}
// 회원가입 완료 팝업 표시 함수
function showSignupCompletePopup() {
  // 로그인 팝업 닫기
  document.getElementById("bg_gray2").style.display = "none"; // 로그인 팝업 닫기

  // 회원가입 완료 팝업 열기
  document.getElementById("bg_gray5").style.display = "block"; // 회원가입 완료 팝업 열기
}
// 닉네임 유효성 및 중복 검사 함수
function checkUsername() {
  var username = document.getElementById("name_input").value;
  var nameCheckContent = document.querySelector(".namecheck-content");
  var namePattern = /^[a-zA-Z가-힣]{2,8}$/; // 2~8자 한글, 영문

  if (namePattern.test(username)) {
    // 서버에서 닉네임 중복 체크
    fetch(`/check-username?username=${username}`)
      .then((response) => response.json())
      .then((data) => {
        if (data.isDuplicate) {
          nameCheckContent.textContent = "중복된 닉네임입니다.";
          nameCheckContent.style.color = "#FF4F4F"; // 빨간색
          nameCheckContent.style.display = "block";
        } else {
          nameCheckContent.textContent = "사용 가능한 닉네임입니다.";
          nameCheckContent.style.color = "#448fff"; // 파란색
          nameCheckContent.style.display = "block";
        }
      });
  } else {
    nameCheckContent.textContent =
      "닉네임은 2~8자의 한글 또는 영문이어야 합니다.";
    nameCheckContent.style.color = "#FF4F4F"; // 빨간색
    nameCheckContent.style.display = "block";
  }
}

// 닉네임 입력 필드에 이벤트 리스너 추가
document.getElementById("name_input").addEventListener("input", checkUsername);
function checkPassword() {
  var password = document.getElementById("join_pw_input").value;
  var specialCharacters = /[!@#$%^&*(),.?":{}|<>]/;
  var checkText = document.getElementById("text24");
  var checkNumber = document.getElementById("text25");
  var imageContainer1 = document.getElementById("imageContainer1");
  var imageContainer2 = document.getElementById("imageContainer2");

  // 특수문자 포함 검사
  if (specialCharacters.test(password)) {
    checkText.style.color = "#448fff";
    imageContainer1.style.display = "block";
  } else {
    checkText.style.color = "#c1c1c1";
    imageContainer1.style.display = "none";
  }

  // 비밀번호 8자 이상 검사
  if (password.length >= 8) {
    checkNumber.style.color = "#448fff";
    imageContainer2.style.display = "block";
  } else {
    checkNumber.style.color = "#c1c1c1";
    imageContainer2.style.display = "none";
  }
}

// 이메일 형식 확인
function isEmailValid(email) {
  var emailPattern = /.+@.+/; // 간단한 이메일 정규식
  return emailPattern.test(email);
}
// 모든 입력 필드의 유효성 검사 후 비동기 회원가입 폼 제출
function submitSignUpForm() {
  var email = document.getElementById("email_input1").value;
  var password = document.getElementById("join_pw_input").value;
  var username = document.getElementById("name_input").value;
  var phone1 = document.getElementById("phone_input1").value;
  var phone2 = document.getElementById("phone_input2").value;
  var phone3 = document.getElementById("phone_input3").value;
  var verificationCode = document.getElementById(
    "CertificationNumber_input"
  ).value;
  var gender = document.getElementById("gender_input").value;
  var birthYear = document.getElementById("birth-year").value;
  var birthMonth = document.getElementById("birth-month").value;
  var birthDay = document.getElementById("birth-day").value;

  // 이용약관과 개인정보 처리방침 체크 여부 확인
  var termsOfUseChecked = document.getElementById("checkbtn2").checked; // 이용약관 체크박스
  var privacyPolicyChecked = document.getElementById("checkbtn3").checked; // 개인정보 처리방침 체크박스

  // 필수 입력 필드가 비어있는지 확인
  if (!email) {
    alert("이메일을 입력해 주세요.");
    return false;
  }
  if (!password) {
    alert("비밀번호를 입력해 주세요.");
    return false;
  }
  if (!username) {
    alert("닉네임을 입력해 주세요.");
    return false;
  }
  if (birthYear === "년" || birthMonth === "월" || birthDay === "일") {
    alert("생년월일을 올바르게 선택해 주세요.");
    return false;
  }
  if (!gender) {
    alert("성별을 선택해 주세요.");
    return false;
  }
  if (!phone1 || !phone2 || !phone3) {
    alert("전화번호를 입력해 주세요.");
    return false;
  }
  if (!verificationCode) {
    alert("인증번호를 입력해 주세요.");
    return false;
  }
  // 이용약관과 개인정보 처리방침 동의 여부 확인
  if (!termsOfUseChecked) {
    alert("이용약관에 동의해 주세요.");
    return false;
  }
  if (!privacyPolicyChecked) {
    alert("개인정보 처리방침에 동의해 주세요.");
    return false;
  }

  var specialCharacters = /[!@#$%^&*(),.?":{}|<>]/;

  // 비밀번호 유효성 검사
  if (!specialCharacters.test(password)) {
    alert("비밀번호는 특수문자를 포함해야 합니다.");
    return false;
  }
  if (password.length < 8) {
    alert("비밀번호는 8자 이상이어야 합니다.");
    return false;
  }

  var formData = new FormData(document.getElementById("joinForm"));
  fetch("/user", {
    method: "POST",
    body: formData,
  })
      .then(response => {
        if (!response.ok) {
          // 상태 코드가 400, 500 등의 오류일 경우 처리
          return response.json().then(err => {
            throw new Error(err.message);
          });
        }
        return response.json();
      })
    .then((data) => {
      // 이메일 중복
      if (
        data.status === "error" &&
        data.message === "이미 사용 중인 이메일입니다."
      ) {
        var emailCheckMessage = document.getElementById("emailCheckMessage");
        emailCheckMessage.textContent = "중복된 이메일입니다.";
        emailCheckMessage.style.color = "#FF4F4F"; // 빨간색으로 표시
        emailCheckMessage.style.display = "block";
      }

      // 닉네임 중복
      if (
        data.status === "error" &&
        data.message === "이미 사용 중인 닉네임입니다."
      ) {
        var nameCheckMessage = document.getElementById("nameCheckMessage");
        nameCheckMessage.textContent = "중복된 닉네임입니다.";
        nameCheckMessage.style.color = "#FF4F4F"; // 빨간색으로 표시
        nameCheckMessage.style.display = "block";
      }

      // 성공 메시지 처리
      if (data.status === "success") {
        showSignupCompletePopup();
      }
    })
    .catch((error) => {
      alert(error.message);  // 상태 코드 제외한 메시지만 표시
    });
}

document.addEventListener("DOMContentLoaded", function () {
  // 비밀번호 찾기 버튼에 대한 이벤트 리스너
  const findPwBtn = document.querySelector("#findpwbtn");
  const emailCheckPopup = document.getElementById("bg_gray7");
  const pwFindPopup = document.getElementById("bg_gray8");

  if (findPwBtn) {
    findPwBtn.addEventListener("click", function (event) {
      event.preventDefault(); // 기본 동작 방지

      // 이메일 확인 팝업 숨기고, 비밀번호 찾기 팝업 보이기
      emailCheckPopup.style.display = "none";
      pwFindPopup.style.display = "block"; // 비밀번호 찾기 팝업 보이기
    });
  }

  // 로그인 버튼에 대한 이벤트 리스너 (만약 필요한 경우)
  const goToLoginBtn2 = document.getElementById("gotologinbtn2");
  const loginPopup = document.getElementById("bg_gray");

  if (goToLoginBtn2) {
    goToLoginBtn2.addEventListener("click", function (event) {
      event.preventDefault();
      emailCheckPopup.style.display = "none";
      loginPopup.style.display = "block"; // 로그인 팝업 보이기
    });
  }
});
// 페이지가 완전히 로드된 후 팝업 숨김 처리
document.addEventListener("DOMContentLoaded", function () {
  document.getElementById("bg_gray").style.display = "none";
});

// AJAX를 통해 로그인 폼 제출
function submitLoginForm() {
  const username = document.querySelector("#email_input").value;
  const password = document.querySelector("#pw_input").value;

  // 오류 메시지 초기화
  const errorMessageElement = document.getElementById("error-message");
  errorMessageElement.textContent = ""; // 기존 메시지 제거
  errorMessageElement.classList.remove("show"); // 에러 메시지 숨기기

  fetch("/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams({ username: username, password: password }),
  })
    .then((response) => {
      if (!response.ok) {
        // 실패 시 JSON 응답을 받아서 에러 메시지 출력
        return response.json().then((err) => {
          throw new Error(err.message);
        });
      }
      return response.text(); // 성공 시
    })
    .then((data) => {
// 로그인 성공 시 페이지 새로고침
      window.location.reload();
    })
    .catch((error) => {
      // 로그인 실패 시 error-message 요소에 에러 메시지 표시
      errorMessageElement.textContent = error.message;
      errorMessageElement.classList.add("show"); // 에러 메시지 보이게 설정
      errorMessageElement.style.color = "#FF5656";
    });
}

function submitPwFindForm() {
  const email = document.querySelector("#email_input11").value;

  fetch("/forgot-password", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams({ email: email }),
  })
    .then((response) => {
      if (!response.ok) {
        return response.json().then((err) => {
          throw new Error(err.message);
        });
      }
      return response.json();
    })
    .then((data) => {
      // 성공 시 'bg_gray9' 팝업을 띄움

      document.getElementById("bg_gray8").style.display = "none";
      document.getElementById("bg_gray9").style.display = "block";
    })
    .catch((error) => {
      alert(error.message); // 실패 시 에러 메시지를 alert로 표시
    });
}
document.addEventListener("DOMContentLoaded", function () {
  var Joinbtn = document.querySelector("#join"); // 로그인 버튼
  var loginLabel = document.getElementById("loginLabel"); // 로그인 라벨
  var dropdownMenu = document.getElementById("dropdownMenu"); // 드롭다운 메뉴
  var followingBtn = document.querySelector(".followingbtn");
  var uploadBtn = document.getElementById("uploadbtn");
  var imageBtn = document.querySelector(".imagebtn");

  // 처음에 버튼들을 숨김 (로그인 전)
  followingBtn.style.display = "none";
  uploadBtn.style.display = "none";
  imageBtn.style.display = "none";

  // 로그인 상태 확인 함수
  function checkLoginStatus() {
    fetch("/api/user/profile")
      .then((response) => response.json())
      .then((data) => {
        if (data.isLoggedIn) {
          // 프로필 이미지 설정
          login(data.profileImageUrl);

          // 팔로우, 업로드, 이미지 버튼 애니메이션 적용
          setTimeout(function () {
            followingBtn.style.display = "inline-block";
            uploadBtn.style.display = "inline-block";
            imageBtn.style.display = "inline-block";

            followingBtn.classList.add("fade-in");
            uploadBtn.classList.add("fade-in");
            imageBtn.classList.add("fade-in");
          }, 700); // 0.3초 후에 버튼 표시 및 애니메이션 추가

          // 로그인 버튼(#join)을 제거
          if (Joinbtn) {
            Joinbtn.remove();
          }

          // 로그인 라벨 클릭 시 드롭다운 메뉴 표시
          loginLabel.onclick = function () {
            toggleDropdown();
          };
        } else {
          // 로그인되지 않은 경우 로그인 버튼을 클릭하면 팝업을 띄움
          loginLabel.onclick = function () {
            togglePopup1("loginPopup");
          };
        }
      })
      .catch((error) => console.error("Error:", error));
  }

  // 로그인 상태일 때 프로필 이미지를 설정하는 함수
  function login(profileImageUrl) {
    loginLabel.style.width = "46px";
    loginLabel.style.height = "46px";
    loginLabel.style.padding = "0";
    loginLabel.style.fontSize = "0";
    loginLabel.style.backgroundImage = `url(${profileImageUrl})`;
    loginLabel.style.backgroundSize = "cover";
    loginLabel.style.backgroundPosition = "center";
    loginLabel.style.border = "2px solid #fff"; // 흰색 테두리 추가
    loginLabel.style.borderRadius = "50%"; // 원형으로 만들기
    loginLabel.style.boxShadow = "0px 4px 8px rgba(0, 0, 0, 0.15)"; // 그림자 효과 추가
    loginLabel.style.cursor = "pointer";
  }

  // 드롭다운 메뉴 토글 함수
  function toggleDropdown() {
    dropdownMenu.style.display =
      dropdownMenu.style.display === "block" ? "none" : "block";
  }

  // 로그인 상태 확인
  checkLoginStatus();
});
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
  var FindPwBtn = document.querySelector("#findpwbtn");
  const emailFindBackBtn = document.querySelector("#emailfindback");
  const emailFindCheckBtn = document.querySelector("#emailfindcheckbtn");

  if (findEmailLink) {
    findEmailLink.addEventListener("click", function (event) {
      event.preventDefault();
      openEmailFindPopup();
    });
  }

  if (findPwLink) {
    findPwLink.addEventListener("click", function (event) {
      event.preventDefault();
      openPwFindPopup();
    });
  }

  if (findPwbtnLink) {
    findPwbtnLink.addEventListener("click", function (event) {
      event.preventDefault();
      openPwFindPopup();
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

  function openPwFindPopup() {
    togglePopup("bg_gray8", "bg_gray");
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

  // // 로그인 성공 시 호출할 함수
  // function login(profileImageUrl) {
  //   console.log("Login function called with profileImageUrl:", profileImageUrl);
  //   checkbox.checked = true;
  //   loginLabel.style.width = "46px";
  //   loginLabel.style.height = "46px";
  //   loginLabel.style.padding = "0";
  //   loginLabel.style.fontSize = "0";
  //   loginLabel.style.backgroundImage = `url(${profileImageUrl})`;
  //   loginLabel.style.backgroundSize = "cover";
  //   loginLabel.style.backgroundPosition = "center";

  //   uploadbtn.style.display = "block";
  //   profilebtn.style.display = "block";
  // }

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
  document.querySelector(".namecheck-content").style.display = "none";
  document.getElementById("error-message").style.color = "#fff";
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

let currentSlide = 0;
const slides = document.querySelectorAll('.slide');

function showSlide(n) {
  slides.forEach((slide, index) => {
    slide.classList.remove('active'); // 모든 슬라이드에서 active 제거
  });
  slides[n].classList.add('active'); // 현재 슬라이드에 active 추가
}

function nextSlide() {
  currentSlide = (currentSlide + 1) % slides.length;
  showSlide(currentSlide);
}

function prevSlide() {
  currentSlide = (currentSlide - 1 + slides.length) % slides.length;
  showSlide(currentSlide);
}

showSlide(currentSlide); // 첫 슬라이드 표시

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

  try {
    const response = await fetch("/api/sendVerificationCode", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ phoneNumber }),
    });

    if (!response.ok) {
      const errorData = await response.text();  // 서버에서 반환된 오류 메시지 추출
      throw new Error(errorData);  // 상태 코드 제거하고 메시지만 에러로 던짐
    }

    const result = await response.text();

    // 응답이 "인증번호가 전송되었습니다."이면 파란색으로 표시, 그 외는 빨간색으로 표시
    if (result === "인증번호가 전송되었습니다.") {
      document.getElementById("sendStatus").textContent = result;
      document.getElementById("sendStatus").style.color = "#448fff"; // 파란색 설정
    } else {
      document.getElementById("sendStatus").textContent = result;
      document.getElementById("sendStatus").style.color = "red"; // 그 외는 빨간색 설정
    }

  } catch (error) {
    console.error("Error:", error);
    document.getElementById("sendStatus").textContent = error.message; // 오류 메시지만 출력
    document.getElementById("sendStatus").style.color = "red"; // 오류 메시지는 빨간색으로 표시
  }
}
async function verifyCode() {
  let phone1 = document.getElementById("phone_input11").value;
  let phone2 = document.getElementById("phone_input21").value;
  let phone3 = document.getElementById("phone_input31").value;
  let phoneNumber = formatPhoneNumber(phone1 + phone2 + phone3);

  const verificationCode = document.getElementById("CertificationNumber_inputp").value;

  // 인증번호가 비어 있는지 확인하고, UI에 메시지 표시
  if (!verificationCode) {
    document.getElementById("sendStatus").textContent = "인증번호를 입력해 주세요.";
    document.getElementById("sendStatus").style.color = "red"; // 메시지 색상을 빨간색으로 설정
    return; // 인증번호가 없으면 함수 종료
  }

  try {
    const response = await fetch("/api/verifyCode", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ phoneNumber, verificationCode }),
    });

    if (!response.ok) {
      const errorData = await response.text();  // 서버에서 반환된 오류 메시지 추출
      document.getElementById("sendStatus").textContent = errorData;  // 오류 메시지를 UI에 표시
      document.getElementById("sendStatus").style.color = "red"; // 실패 메시지 색상 설정
      return; // 요청이 실패한 경우, 아래 코드를 실행하지 않고 종료
    }

    const result = await response.text();


    // 서버 응답에 이메일이 포함되거나 해당 번호로 가입된 이메일이 없을 때 처리
    if (result.includes("@") || result === "해당 번호로 가입된 이메일이 없습니다.") {
      // 팝업 전환: bg_gray6 팝업 닫고 bg_gray7 팝업 열기
      document.getElementById("bg_gray6").style.display = "none"; // 기존 팝업 닫기
      document.getElementById("bg_gray7").style.display = "block"; // 새 팝업 열기
      console.log("팝업 전환 완료");

      // 인증 성공 또는 해당 번호로 이메일이 없는 경우 메시지 설정
      setTimeout(function () {
        const emailStatusElement = document.getElementById("verifyStatus");
        if (emailStatusElement) {
          emailStatusElement.textContent = result; // 이메일 또는 메시지를 UI에 표시
        }
      }, 100); // 팝업이 제대로 열릴 수 있는 짧은 시간 대기 후 설정

      // 입력 필드 초기화
      document.getElementById("phone_input11").value = "";  // 전화번호 첫 번째 부분 초기화
      document.getElementById("phone_input21").value = "";  // 전화번호 두 번째 부분 초기화
      document.getElementById("phone_input31").value = "";  // 전화번호 세 번째 부분 초기화
      document.getElementById("CertificationNumber_inputp").value = "";  // 인증번호 필드 초기화
      document.getElementById("verifyStatus").value = "";  //

    } else {
      // 인증 실패 시 처리
      console.log("인증 실패 - 인증 결과:", result);
      document.getElementById("verifyStatus").textContent =
          "인증 실패: " + result; // 실패 메시지 표시
      document.getElementById("verifyStatus").style.color = "red"; // 실패 메시지 색상 설정
    }

  } catch (error) {
    // 오류 발생 시 메시지 UI에 표시
    document.getElementById("sendStatus").textContent = `오류 발생: ${error.message}`;
    document.getElementById("sendStatus").style.color = "red"; // 오류 메시지 색상 설정
  }
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
function showPopup(popupId) {
var popup = document.getElementById(popupId);
if (popup) {
  popup.style.display = "block";
} else {
 console.log("Popup element with ID " + popupId + " not found.");
}
}