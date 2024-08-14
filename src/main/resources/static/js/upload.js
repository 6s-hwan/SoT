document.addEventListener("DOMContentLoaded", function () {
  var StoryUploadPopup = document.getElementById("Upload_pop_up");
  StoryUploadPopup.style.display = "block"; // 팝업 보이기

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

  // 폼 제출 시
  const form = document.getElementById("uploadForm");
  form.addEventListener("submit", function (event) {
    event.preventDefault();
    updateLocationInput(); // location 값을 업데이트
    uploadPost(event);
  });

  // 이미지 업로드 클릭 오류 해결
  const uploadPlaceholder = document.querySelector(".upload-placeholder");
  uploadPlaceholder.addEventListener("click", function () {
    document.getElementById("input-file").click();
  });

  // '년', '월', '일' 셀렉트 박스 option 목록 동적 생성
  setupDateSelectors();

  // 위치 선택 관련 코드 추가
  populateCategories();

  const locationCategorySelect = document.getElementById("location-category");
  locationCategorySelect.addEventListener("change", function () {
    populateRegions(this.value);
  });

  const regionSelect = document.getElementById('region');
  regionSelect.addEventListener("change", function () {
    populateCities(this.value);
  });

  const citySelect = document.getElementById("city");
  citySelect.addEventListener("change", updateLocationInput);
});

// 위치 정보를 업데이트하는 함수
function updateLocationInput() {
  const category = document.getElementById('location-category').value; // 국내/국외
  const region = document.getElementById('region').value;
  const city = document.getElementById('city').value;

  const locationInput = document.getElementById('location');

  // '국내' 또는 '국외' 접두어를 빼고 순수 위치만 설정
  if (category === '국내' || category === '국외') {
    locationInput.value = `${region} ${city}`;
  } else {
    locationInput.value = city;
  }

  console.log('Updated location:', locationInput.value); // 값이 정상인지 확인하는 로그
}

function uploadPost(event) {
  event.preventDefault();

  let form = event.target;
  let title = form.title.value;
  let date_year = form.date_year.value;
  let date_month = form.date_month.value;
  let date_day = form.date_day.value;
  let location = form.location.value;  // updateLocationInput()으로 설정된 값
  let tags = form.tags.value;
  let description = form.description.value;
  let imageUrl = form.imageUrl.value;

  if (!title || !date_year || !date_month || !date_day || !location || !description || !imageUrl) {
    alert("모든 필드를 채워주세요!");
    return false;
  }

  form.submit();
}

// 국내, 국외, 어딘가 카테고리 정의
const categoriesData = ["국내", "국외", "어딘가"];

// 국내외 지역 및 도시 데이터 정의
const regionsData = {
  "대한민국": [
    "서울특별시", "부산광역시", "인천광역시", "대구광역시", "대전광역시",
    "광주광역시", "울산광역시", "세종특별자치시", "경기도", "충청남도",
    "충청북도", "전라남도", "전라북도", "경상남도", "경상북도",
    "강원특별자치도", "제주특별자치도", "어딘가"
  ],
  "해외": [
      "일본", "중국", "베트남", "태국", "필리핀", "미국", "대만", "말레이시아", "싱가포르", "인도네시아"
  ]
};

const citiesData = {
  "서울특별시": [
    "강남구", "강동구", "강북구", "강서구", "관악구", "광진구",
    "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구",
    "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구",
    "양천구", "영등포구", "용산구", "은평구", "종로구", "중구",
    "중랑구", "어딘가"
  ],
  "부산광역시": [
    "강서구", "금정구", "기장군", "남구", "동구", "동래구", "부산진구",
    "북구", "사상구", "사하구", "서구", "수영구", "연제구", "영도구",
    "중구", "해운대구", "어딘가"
  ],
  "인천광역시": [
    "강화군", "계양구", "남동구", "동구", "미추홀구", "부평구",
    "서구", "연수구", "옹진군", "중구", "어딘가"
  ],
  "대구광역시": [
    "남구", "달서구", "달성군", "동구", "북구", "서구", "수성구",
    "중구", "어딘가"
  ],
  "대전광역시": [
    "대덕구", "동구", "서구", "유성구", "중구", "어딘가"
  ],
  "광주광역시": [
    "광산구", "남구", "동구", "북구", "서구", "어딘가"
  ],
  "울산광역시": [
    "남구", "동구", "북구", "울주군", "중구", "어딘가"
  ],
  "세종특별자치시": [
    "조치원읍", "금남면", "장군면", "연서면", "연동면", "부강면",
    "전의면", "전동면", "소정면", "고운동", "아름동", "종촌동",
    "한솔동", "새롬동", "도담동", "해밀동", "어딘가"
  ],
  "경기도": [
    "고양시", "과천시", "광명시", "광주시", "구리시", "군포시",
    "김포시", "남양주시", "동두천시", "부천시", "성남시", "수원시",
    "시흥시", "안산시", "안성시", "안양시", "양주시", "여주시",
    "오산시", "용인시", "의왕시", "의정부시", "이천시", "파주시",
    "평택시", "포천시", "하남시", "화성시", "어딘가"
  ],
  "충청남도": [
    "천안시", "공주시", "보령시", "아산시", "서산시", "논산시",
    "계룡시", "당진시", "금산군", "부여군", "서천군", "청양군",
    "홍성군", "예산군", "태안군", "어딘가"
  ],

  // 충청북도
  "충청북도": [
    "청주시", "충주시", "제천시", "보은군", "옥천군", "영동군",
    "증평군", "진천군", "괴산군", "음성군", "단양군", "어딘가"
  ],

  // 전라남도
  "전라남도": [
    "목포시", "여수시", "순천시", "나주시", "광양시", "담양군",
    "곡성군", "구례군", "고흥군", "보성군", "화순군", "장흥군",
    "강진군", "해남군", "영암군", "무안군", "함평군", "영광군",
    "장성군", "완도군", "진도군", "신안군", "어딘가"
  ],

  // 전라북도
  "전라북도": [
    "전주시", "군산시", "익산시", "정읍시", "남원시", "김제시",
    "완주군", "진안군", "무주군", "장수군", "임실군", "순창군",
    "고창군", "부안군", "어딘가"
  ],

  // 경상남도
  "경상남도": [
    "창원시", "진주시", "통영시", "사천시", "김해시", "밀양시",
    "거제시", "양산시", "의령군", "함안군", "창녕군", "고성군",
    "남해군", "하동군", "산청군", "함양군", "거창군", "합천군", "어딘가"
  ],

  // 경상북도
  "경상북도": [
    "포항시", "경주시", "김천시", "안동시", "구미시", "영주시",
    "영천시", "상주시", "문경시", "경산시", "군위군", "의성군",
    "청송군", "영양군", "영덕군", "청도군", "고령군", "성주군",
    "칠곡군", "예천군", "봉화군", "울진군", "울릉군", "어딘가"
  ],

  // 강원특별자치도
  "강원특별자치도": [
    "춘천시", "원주시", "강릉시", "동해시", "태백시", "속초시",
    "삼척시", "홍천군", "횡성군", "영월군", "평창군", "정선군",
    "철원군", "화천군", "양구군", "인제군", "고성군", "양양군", "어딘가"
  ],

  // 제주특별자치도
  "제주특별자치도": [
    "제주시", "서귀포시", "어딘가"
  ],

  "일본": [
    "도쿄도", "오사카부", "교토부", "후쿠오카현", "홋카이도", "어딘가"
  ],
  "중국": ["베이징", "상하이", "광저우", "청두", "시안", "어딘가"],
  "베트남": ["하노이", "호치민", "다낭", "호이안", "나트랑", "어딘가"],
  "태국": ["방콕", "푸켓", "치앙마이", "파타야", "끄라비", "어딘가"],
  "필리핀": ["마닐라", "세부", "보라카이", "다바오", "클락", "어딘가"],
  "미국": ["New York", "Los Angeles", "San Francisco", "Chicago", "Las Vegas", "Hawaii", "어딘가"],
  "대만": ["타이베이", "가오슝", "타이중", "타이난", "타이둥", "단수이", "어딘가"],
  "말레이시아": ["쿠알라룸푸르", "조호르바루", "코타키나발루", "페낭", "말라카", "어딘가"],
  "싱가포르": ["싱가포르", "어딘가"],
  "인도네시아": ["자카르타", "발리", "수라바야", "반둥", "족자카르타", "어딘가"],
  "어딘가": ["어딘가"]  // '어딘가'를 위한 특별 처리
};

function populateCategories() {
  const categorySelect = document.getElementById('location-category');
  categoriesData.forEach(category => {
    const option = document.createElement('option');
    option.value = category;
    option.textContent = category;
    categorySelect.appendChild(option);
  });
}

function populateRegions(category) {
  const regionSelect = document.getElementById('region');
  regionSelect.innerHTML = ''; // 기존 옵션 제거
  document.getElementById('city').innerHTML = ''; // 도시 선택도 초기화

  if (category === '국내') {
    regionsData["대한민국"].forEach(region => {
      const option = document.createElement('option');
      option.value = region;
      option.textContent = region;
      regionSelect.appendChild(option);
    });
  } else if (category === '국외') {
    regionsData["해외"].forEach(region => {
        const option = document.createElement('option');
        option.value = region;
        option.textContent = region;
        regionSelect.appendChild(option);

    });
  } else if (category === '어딘가') {
    const option = document.createElement('option');
    option.value = '어딘가';
    option.textContent = '어딘가';
    regionSelect.appendChild(option);

    const citySelect = document.getElementById('city');
    citySelect.innerHTML = ''; // 도시 선택 초기화
    const cityOption = document.createElement('option');
    cityOption.value = '어딘가';
    cityOption.textContent = '어딘가';
    citySelect.appendChild(cityOption);
  }

  // 첫 번째 지역 선택 후 해당 도시에 대해 populateCities 호출
  if (regionSelect.value) {
    populateCities(regionSelect.value);
  }
}

function populateCities(region) {
  const citySelect = document.getElementById('city');
  citySelect.innerHTML = ''; // 기존 옵션 제거

  // region에 해당하는 citiesData가 있는 경우에만 처리
  if (region && citiesData[region]) {
    citiesData[region].forEach(city => {
      const option = document.createElement('option');
      option.value = city;
      option.textContent = city;
      citySelect.appendChild(option);
    });
  } else {
    // 도시 데이터가 없는 경우 '어딘가'만 선택지로 제공
    const option = document.createElement('option');
    option.value = '어딘가';
    option.textContent = '어딘가';
    citySelect.appendChild(option);
  }
}

// 위치 정보를 업데이트하는 함수
function updateLocationInput() {
  const category = document.getElementById('location-category').value; // 국내/국외/어딘가
  const region = document.getElementById('region').value;
  const city = document.getElementById('city').value;

  const locationInput = document.getElementById('location');

  // '어딘가' 카테고리에서 선택한 경우
  if (category === '어딘가') {
    locationInput.value = '어딘가';
  }
  // '국내' 카테고리에서 '어딘가'를 선택한 경우
  else if (category === '국내' && (region === '어딘가' || city === '어딘가')) {
    locationInput.value = '대한민국 어딘가';
  }
  // 그 외의 경우
  else {
    locationInput.value = `${region} ${city}`;
  }

  console.log('Updated location:', locationInput.value); // 값이 정상인지 확인하는 로그
}

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

  // location 값 확인을 위한 로그 추가
  console.log('Title:', title);
  console.log('Date:', date_year + "-" + date_month + "-" + date_day);
  console.log('Location:', location); // 이 로그가 제대로 location 값을 출력하는지 확인하세요.
  console.log('Tags:', tags);
  console.log('Description:', description);
  console.log('Image URL:', imageUrl);

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
  const birthYearEl = document.querySelector("#upload-year");
  // option 목록 생성 여부 확인
  let isYearOptionExisted = false;
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
  });

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
}
