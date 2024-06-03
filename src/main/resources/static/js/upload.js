document.addEventListener("DOMContentLoaded", function () {
  var StoryUploadPopup = document.getElementById("Upload_pop_up");
  StoryUploadPopup.style.display = "block"; //팝업 보이기
});

// 창 닫는 js 코드
var loginBtn = document.getElementById("join");
var StoryUploadPopup = document.getElementById("bg_gray1");
document.addEventListener("DOMContentLoaded", function () {
  loginBtn.addEventListener("click", function () {
    StoryUploadPopup.style.display = "block";
    document.body.style.overflow = ""; // 스크롤 허용
    resetForm();
  });
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
          document.getElementById("imageUrl").value = imageUrl;
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
    newTag.textContent = "#" + tagValue;
    newTag.classList.add("spantags"); // spantags 클래스 추가
    let label = document.querySelector('label[for="tagInput"]');
    label.parentNode.insertBefore(newTag, label.nextSibling);
    tagInput.value = "";
    updateTagsInput();
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
  let location = form.location.value;
  let tags = form.tags.value;
  let description = form.description.value;
  let imageUrl = form.image_url.value;

  if (!title || !location || !description || !imageUrl) {
    alert("모든 필드를 채워주세요!");
    return false;
  }

  form.submit();
}

const form = document.getElementById("uploadForm");
const closeButton = document.getElementById("uploadclose");
const bgGray1 = document.getElementById("bg_gray1");
const fileInput = document.getElementById("input-file");
const uploadedImage = document.getElementById("uploadedImage");
const uploadPlaceholder = document.querySelector(".upload-placeholder");
const uploadImg = document.getElementById("uploadimg");
const uploadText = document.querySelector(".upload-placeholder p");
const inputFileButton = document.querySelector(".upload-placeholder label");
const uploadBlank = document.querySelector(".UploadBlank");
const spantag = document.querySelector(".spantags");

// 폼 초기화 함수
function resetForm() {
  form.reset();
  fileInput.value = "";
  uploadedImage.src = "";
  uploadedImage.style.display = "none";
  uploadImg.style.display = "block";
  uploadText.style.display = "block";
  inputFileButton.style.display = "block";
  uploadBlank.style.display = "block";
}

// 닫기 버튼 클릭 시
closeButton.addEventListener("click", function () {
  bgGray1.style.display = "none";
  resetForm();
  window.location.reload(); // 페이지 새로고침
});

// 폼 제출 시
form.addEventListener("submit", function (event) {
  event.preventDefault(); // 실제 서버로의 제출을 막기 위해 사용
  resetForm();
  bgGray1.style.display = "none";
  window.location.reload(); // 페이지 새로고침
});

// 이미지 파일 선택 시 미리보기 설정
function getURL(input) {
  if (input.files && input.files[0]) {
    const reader = new FileReader();
    reader.onload = function (e) {
      uploadedImage.src = e.target.result;
      uploadedImage.style.display = "block";
      uploadImg.style.display = "none";
      uploadText.style.display = "none";
      inputFileButton.style.display = "none";
      uploadBlank.style.display = "none";
    };
    reader.readAsDataURL(input.files[0]);
  }
}

//날짜
// '년' 셀렉트 박스 option 목록 동적 생성
const birthYearEl = document.querySelector("#upload-year");
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
const birthMonthEl = document.querySelector("#upload-month");
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
const birthDayEl = document.querySelector("#upload-day");
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
