// 뒷화면 고정

// 초기에는 로그인 창을 보여주고 회원가입 창을 숨깁니다.
document.addEventListener("DOMContentLoaded", function () {
  var loginPopup = document.getElementById("login_pop_up");
  var joinPopup = document.getElementById("join_pop_up");

  loginPopup.style.display = "block"; // 로그인 팝업 보이기
  joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
});

// 창 닫는 js 코드
var loginBtn = document.getElementById("join");
var loginPopup = document.getElementById("bg_gray");
document.addEventListener("DOMContentLoaded", function () {
  loginBtn.addEventListener("click", function () {
    loginPopup.style.display = "block";
    document.body.style.overflow = ""; // 스크롤 허용
  });
});

// 로그인 창 입력값 확인 후 로그인 버튼 활성화
function submitForm() {
  // 입력값 가져오기
  var emailInput = document.getElementById("email_input");
  var pwInput = document.getElementById("pw_input");

  // 입력값이 모두 채워져 있는지 확인
  if (emailInput.value.trim() !== "" && pwInput.value.trim() !== "") {
    // 로그인 처리 등의 작업 수행 후, 창 닫기
    document.getElementById("bg_gray").style.display = "none";

    // 입력 필드 초기화
    emailInput.value = "";
    pwInput.value = "";
    document.getElementById("checkbtn").checked = false;
  }
}

// 로그인 팝업 창에서 회원가입 페이지로 이동하는 링크 클릭 시 이벤트 처리
document.addEventListener("DOMContentLoaded", function () {
  var signupLink = document.querySelector("#join_membership a");
  var loginPopup = document.getElementById("login_pop_up");
  var joinPopup = document.getElementById("join_pop_up");
  signupLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
  });
});

// 회원가입 창에서 로그인 페이지로 돌아가기 링크를 누르면 로그인 팝업 창 열기
document.addEventListener("DOMContentLoaded", function () {
  var loginLink = document.querySelector("#back a");
  var loginPopup = document.getElementById("login_pop_up");
  var joinPopup = document.getElementById("join_pop_up");
  loginLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
    loginPopup.style.display = "block"; // 로그인 팝업 보이기
  });
});

// 이메일 파트
function emailCheck(email_address) {
  email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
  if (!email_regex.test(email_address)) {
    return false;
  } else {
    return true;
  }
}

function validateEmail() {
  var emailInput = document.getElementById("email_input1");
  var resultDiv = document.getElementById("email_text");

  var email = emailInput.value;

  if (emailCheck(email)) {
    resultDiv.innerHTML = "사용 가능한 이메일입니다.";
  } else {
    resultDiv.innerHTML = "";
  }
}

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

//닉네임
// function Validation() {
//   var n_RegExp = /^[가-힣]{2,15}$/; //이름 유효성검사 정규식
//   var objName = document.getElementById("name_input"); //이름

//   if (objName.value == "") {
//     resultDiv.innerHTML = "사용 가능한 닉네임입니다.";
//     return false;
//   }
//   if (!n_RegExp.test(objName.value)) {
//     resultDiv.innerHTML = "";
//     return false;
//   }
// }

// function nameCheck(_name) {
//   n_RegExp = /^[가-힣]{2,15}$/;
//   if (!n_RegExp.test(_name)) {
//     return false;
//   } else {
//     return true;
//   }
// }

// function validateName() {
//   var NameInput = document.getElementById("name_input"); //이름
//   var resultDiv2 = document.getElementById("name_possible");

//   var name = emailInput.value;

//   if (nameCheck(name)) {
//     resultDiv2.innerHTML = "사용 가능한 닉네임입니다.";
//   } else {
//     resultDiv2.innerHTML = "";
//   }
// }

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
  document.getElementById(`button${selectedButton}`).classList.add("selected");
}

// 회원가입 버튼 누르면 로그인창으로 넘어가는 함수
document.addEventListener("DOMContentLoaded", function () {
  var joinBtn = document.getElementById("join_btn");
  var loginPopup = document.getElementById("login_pop_up");
  var joinPopup = document.getElementById("join_pop_up");

  joinBtn.addEventListener("click", function () {
    // 회원가입 버튼 클릭 시 로그인 팝업 창 숨기기
    loginPopup.style.display = "none";

    // 로그인 팝업 창 표시
    loginPopup.style.display = "block";
    // 회원가입 팝업 창 숨기기
    joinPopup.style.display = "none";

    // 회원가입 입력값 초기화
    resetJoinForm();
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
  // 전화번호 입력칸 초기화
  document.getElementById("phone_input1").value = "";
  document.getElementById("phone_input2").value = "";
  document.getElementById("phone_input3").value = "";
  // 인증번호 입력칸 초기화
  document.getElementById("CertificationNumber_input").value = "";
  // 이용약관 입력칸 초기화
  var agreementCheckbox1 = document.getElementById("checkbtn");
  var agreementCheckbox2 = document.getElementById("checkbtn2");
  var agreementCheckbox3 = document.getElementById("checkbtn3");

  agreementCheckbox1.checked = false;
  agreementCheckbox2.checked = false;
  agreementCheckbox3.checked = false;
  // 지역 선택 카테고리 초기화
  resetAreaSelection();
  // 시/도 선택 초기화
  document.getElementById("area1").selectedIndex = 0;
  // 시/구/군 선택 초기화
  document.getElementById("area2").selectedIndex = 0;
  // 동/읍/면 선택 초기화
  document.getElementById("area3").selectedIndex = 0;

  // 이벤트 리스너 제거
  document
    .getElementById("join_pw_input")
    .removeEventListener("keyup", checkInputs);
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
let currentSlide = 0;
const slides = document.querySelectorAll(".slide");

function showSlide(n) {
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

showSlide(currentSlide);

// 넘어갈 때 파라미터 전달하고 페이지 이동하는 함수
function passParamsAndNavigate(number) {
  // 오버레이 이미지와 체크 이미지의 상태를 전달하는 부분
  var overlayVisible = isOverlayVisible[number - 1];
  var checkVisible = isCheckVisible[number - 1];

  // 파라미터를 전달하고 페이지 이동
  window.location.href =
    "ThemeDetailPage.html?overlay=" +
    overlayVisible +
    "&check=" +
    checkVisible +
    "&index=" +
    number;
}
