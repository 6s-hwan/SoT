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

// 각 버튼의 체크된 이미지 상태를 저장할 배열
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

// 버튼을 누르면 체크이미지와 오버레이된 이미지가 나타남
function toggleImageAndCheck(index) {
  var overlayImage = document.getElementById("overlayImage" + index);
  var checkImage = document.getElementById("themecheck" + index);

  if (!isOverlayVisible[index - 1]) {
    overlayImage.style.display = "block"; // 오버레이 이미지 표시
    checkImage.src = "../static/images/theme_check.png"; // 체크된 이미지로 변경
  } else {
    overlayImage.style.display = "none"; // 오버레이 이미지 숨김
    checkImage.src = "../static/images/theme_checked.png"; // 체크 해제된 이미지로 변경
  }

  // 상태 반전
  isOverlayVisible[index - 1] = !isOverlayVisible[index - 1];
  isCheckVisible[index - 1] = !isCheckVisible[index - 1];
}

// 페이지가 로드될 때 실행되는 함수
window.onload = function () {
  // URL에서 파라미터 추출
  const urlParams = new URLSearchParams(window.location.search);
  // const overlayVisible = urlParams.get("overlay");
  // const checkVisible = urlParams.get("check");
  const overlayIndex = parseInt(urlParams.get("index"));

  // 오버레이 이미지와 체크 이미지의 상태에 따라 화면에 보여주거나 숨김
  if (overlayIndex) {
    document.getElementById("overlayImage" + overlayIndex).style.display =
      "block";
    document.getElementById("themecheck" + overlayIndex).src =
      "../static/images/theme_check.png";
  }
  // 오버레이 이미지와 체크 이미지의 상태에 따라 화면에 보여주거나 숨김
  // if (overlayVisible === "false") {
  //   document.getElementById("overlayImage1").style.display = "block";
  // }
  // if (checkVisible === "false") {
  //   document.getElementById("themecheck1").src =
  //     "../static/images/theme_check.png";
  // }
};

const selectedThemes = new Set();

function toggleTheme(themeId) {
  const checkElement = document.getElementById(`themecheck${themeId}`);
  if (selectedThemes.has(themeId)) {
    selectedThemes.delete(themeId);
    checkElement.style.display = 'none';
  } else {
    selectedThemes.add(themeId);
    checkElement.style.display = 'block';
  }

  // Set을 배열로 변환한 후 정렬
  const themesParam = Array.from(selectedThemes).sort((a, b) => a - b).join(',');
  const newUrl = themesParam.length > 0 ? `/theme?themes=${themesParam}` : `/theme`;
  window.location.href = newUrl;
}

// 페이지 로드 시 URL에 있는 테마 ID를 읽어와 체크박스 상태를 업데이트합니다.
document.addEventListener('DOMContentLoaded', () => {
  const urlParams = new URLSearchParams(window.location.search);
  const themes = urlParams.get('themes');
  if (themes) {
    themes.split(',').forEach(themeId => {
      selectedThemes.add(parseInt(themeId));
      document.getElementById(`themecheck${themeId}`).style.display = 'block';
    });
  }
});

