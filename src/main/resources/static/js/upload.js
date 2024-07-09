document.addEventListener("DOMContentLoaded", function () {
  var StoryUploadPopup = document.getElementById("Upload_pop_up");
  StoryUploadPopup.style.display = "block"; //팝업 보이기

  // 창 닫는 js 코드
  var loginBtn = document.getElementById("join");

  loginBtn.addEventListener("click", function () {
    StoryUploadPopup.style.display = "block";
    document.body.style.overflow = ""; // 스크롤 허용
    resetForm();
  });

  // 닫기 버튼 클릭 시
  const closeButton = document.getElementById("uploadclose");
  closeButton.addEventListener("click", function () {
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
  // 폼 제출 시
  const form = document.getElementById("uploadForm");
  form.addEventListener("submit", function (event) {
    event.preventDefault();
    uploadPost(event);
  });

  // 이미지 업로드 클릭 오류 해결
  const uploadPlaceholder = document.querySelector(".upload-placeholder");
  uploadPlaceholder.addEventListener("click", function () {
    document.getElementById("input-file").click();
  });

  // '년', '월', '일' 셀렉트 박스 option 목록 동적 생성
  setupDateSelectors();
  setupSubmitHandler();
});

function getURL(e) {
  if (e.files && e.files[0]) {
    let reader = new FileReader();
    reader.onload = function (event) {
      let imgElement = document.getElementById('uploadedImage');
      imgElement.src = event.target.result;
      imgElement.style.display = "block"; // 이미지 보이기
      document.querySelector(".upload-placeholder").classList.add('active');
      document.querySelector(".upload-placeholder div").style.display = 'none'; // 이미지 선택시 텍스트를 숨김
      document.querySelector(".upload-placeholder p").style.display = 'none'; // 이미지 선택시 텍스트를 숨김
      document.querySelector(".upload-placeholder img").style.display = 'none'; // 이미지 선택시 텍스트를 숨김
      document.querySelector(".upload-placeholder label").style.display = 'none';
    };
    reader.readAsDataURL(e.files[0]);

    // Get presigned URL for S3
    const fileName = encodeURIComponent(e.files[0].name);
    fetch(`/presigned-url?filename=${fileName}`)
        .then(response => response.text())
        .then(url => {
          return fetch(url, {
            method: 'PUT',
            body: e.files[0],
          });
        })
        .then(response => {
          if (response.ok) {
            const imageUrl = response.url.split('?')[0];
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
  var formData = new FormData(document.querySelector('form'));

  // 데이터 콘솔에 출력
  console.log(formData);

  // 나머지 코드
  // ...

  // 서버로 전송
  // ...

  // 이벤트 기본 동작 방지 (페이지 새로고침 방지)
  event.preventDefault();

  if (!title || !date_year || !date_month || !date_day || !location || !description || !imageUrl) {
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



function setupDateSelectors() {
  // '년' 셀렉트 박스 option 목록 동적 생성
  //날짜
// '년' 셀렉트 박스 option 목록 동적 생성
const birthYearEl = document.querySelector("#upload-year");
// option 목록 생성 여부 확인
isYearOptionExisted = false;
birthYearEl.addEventListener("focus", function () {
  // year 목록 생성되지 않았을 때 (최초 클릭 시)
  if (!isYearOptionExisted) {
    isYearOptionExisted = true;
    for (var i = 2024; i >= 2000; i--) {
      // option element 생성
      const YearOption = document.createElement("option");
      YearOption.setAttribute("value", i);
      YearOption.innerText = i;
      // birthYearEl의 자식 요소로 추가
      this.appendChild(YearOption);

    }
  }

  // '월' 셀렉트 박스 option 목록 동적 생성
  const birthMonthEl = document.querySelector("#upload-month");
  let isMonthOptionExisted = false;
  birthMonthEl.addEventListener("focus", function () {
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
  const birthDayEl = document.querySelector("#upload-day");
  let isDayOptionExisted = false;
  birthDayEl.addEventListener("focus", function () {
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

