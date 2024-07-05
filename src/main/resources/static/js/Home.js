// 초기에는 로그인 창을 보여주고 나머지 창을 숨깁니다.
document.addEventListener("DOMContentLoaded", function () {
  var loginPopup = document.getElementById("login_pop_up");
  var joinPopup = document.getElementById("join_pop_up");
  var TermsOfUsePopup = document.getElementById("TermsOfUse_pop_up");
  var PrivacyPopup = document.getElementById("Privacy_pop_up");

  loginPopup.style.display = "block"; // 로그인 팝업 보이기
  joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
  TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
  PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
});

// 로그인 창 닫는 js 코드
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

//창 여는 이벤트
document.addEventListener("DOMContentLoaded", function () {
  var Joinbtn = document.querySelector("#join");
  var signupLink = document.querySelector("#join_membership a");
  var loginLink = document.querySelector("#back a");
  var TermsOfUseOpenLink = document.querySelector("#container2 a");
  var TermsOfUseCloseLink = document.querySelector("#Usebackarrow");
  var TermsOfUseAgreeCloseLink = document.querySelector("#UseAgree_btn");
  var PrivacyOpenLink = document.querySelector("#container1 a");
  var PrivacyCloseLink = document.querySelector("#Privacybackarrow");
  var PrivacyAgreeCloseLink = document.querySelector("#PrivacyAgree_btn");

  var loginPopup = document.getElementById("login_pop_up");
  var joinPopup = document.getElementById("join_pop_up");
  var TermsOfUsePopup = document.getElementById("TermsOfUse_pop_up");
  var PrivacyPopup = document.getElementById("Privacy_pop_up");
  // 로그인 버튼 누르면 회원가입창 열기
  Joinbtn.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "block"; // 로그인 팝업 보이기
    joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 로그인 팝업 창에서 회원가입 페이지로 이동하는 링크 클릭 시 이벤트 처리
  signupLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 회원가입 창에서 로그인 페이지로 돌아가기 링크를 누르면 로그인 팝업 창 열기
  loginLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "block"; // 로그인 팝업 보이기
    joinPopup.style.display = "none"; // 회원가입 팝업 숨기기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 개인정보 취급방침창 열기
  TermsOfUseOpenLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "none"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "block"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 개인정보 취급방침창 닫기
  TermsOfUseCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 개인정보 취급방침 동의 버튼 눌러서 창 닫기
  TermsOfUseAgreeCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 이용약관 창 열기
  PrivacyOpenLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "none"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "block"; // 이용약관 팝업 숨기기
  });
  // 이용약관 창 닫기
  PrivacyCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
  // 이용약관 동의버튼 눌러서 창 닫기
  PrivacyAgreeCloseLink.addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    loginPopup.style.display = "none"; // 로그인 팝업 숨기기
    joinPopup.style.display = "block"; // 회원가입 팝업 보이기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기
  });
});

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

function checkInputs() {
  var emailInput = document.getElementById("email_input");
  var email = emailInput.value;
  var emailRegex =
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
  // var emailText = document.getElementById("email_text");
  var possibleEmail = document.getElementById("possible_email");

  if (emailRegex.test(email)) {
    possibleEmail.innerHTML = "사용 가능한 이메일입니다.";
    possibleEmail.style.color = "#448FFF";
  }
  // } else {
  //   possibleEmail.innerHTML = "유효하지 않은 이메일 주소입니다.";
  //   possibleEmail.style.color = "red";
  // }
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

// 회원가입 버튼 누르면 로그인창으로 넘어가는 함수
document.addEventListener("DOMContentLoaded", function () {
  var joinBtn = document.getElementById("join_btn");
  var joincloseBtn = document.getElementById("close");
  var loginPopup = document.getElementById("login_pop_up");
  var joinPopup = document.getElementById("join_pop_up");
  var TermsOfUsePopup = document.getElementById("TermsOfUse_pop_up");
  var PrivacyPopup = document.getElementById("Privacy_pop_up");

  joinBtn.addEventListener("click", function () {
    // loginPopup.style.display = "none"; // 회원가입 버튼 클릭 시 로그인 팝업 창 숨기기
    loginPopup.style.display = "block"; // 로그인 팝업 창 표시
    joinPopup.style.display = "none"; // 회원가입 팝업 창 숨기기
    TermsOfUsePopup.style.display = "none"; // 개인정보 취급방침 팝업 숨기기
    PrivacyPopup.style.display = "none"; // 이용약관 팝업 숨기기

    // 회원가입 입력값 초기화
  });
  joincloseBtn.addEventListener("click", function () {
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

// 테마별 여행지에서 각 버튼을 누르면 누른 상태로 상세페이지로 넘어가기
// 각 버튼의 오버레이 이미지 상태를 저장할 배열
var isOverlayVisible = [
  false,
  false,
  false,
  false,
  false,
  false,
  false,
  false,
  false,
];

// 각 버튼의 체크 이미지 상태를 저장할 배열
var isCheckVisible = [
  false,
  false,
  false,
  false,
  false,
  false,
  false,
  false,
  false,
];

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

  // 업로드 팝업창 닫는 js 코드
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
