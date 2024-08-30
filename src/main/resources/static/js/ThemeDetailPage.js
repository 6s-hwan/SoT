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
// var isOverlayVisible = [false];
// var isCheckVisible = [false];

function executeBothFunctions(index) {
  toggleImageAndCheck(index);
  toggleTheme(index);
}

function toggleImageAndCheck(index) {
  var overlayImage = document.getElementById("overlayImage" + index);
  var checkImage = document.getElementById("themecheck" + index);

  if (!isOverlayVisible[index - 1]) {
    overlayImage.style.display = "block"; // 오버레이 이미지 표시
    checkImage.style.display = "inline"; // 체크 이미지 표시
    isOverlayVisible[index - 1] = true;
    isCheckVisible[index - 1] = true;
  } else {
    overlayImage.style.display = "none"; // 오버레이 이미지 숨김
    checkImage.style.display = "none"; // 체크 이미지 숨김
    isOverlayVisible[index - 1] = false;
    isCheckVisible[index - 1] = false;
  }
  updateURL();
}

const selectedThemes = new Set();
const selectedOverlays = new Set();

function toggleTheme(themeId) {
  const checkElement = document.getElementById(`themecheck${themeId}`);
  if (selectedThemes.has(themeId)) {
    selectedThemes.delete(themeId);
    checkElement.style.display = "none";
    selectedOverlays.delete(themeId); // 오버레이도 같이 숨김
  } else {
    selectedThemes.add(themeId);
    checkElement.style.display = "inline";
    selectedOverlays.add(themeId); // 오버레이도 같이 표시
  }
  updateURL();
}

function updateURL() {
  const themesParam = Array.from(selectedThemes)
    .sort((a, b) => a - b)
    .join(",");
  const overlaysParam = Array.from(selectedOverlays)
    .sort((a, b) => a - b)
    .join(",");

  let newUrl = "/theme";
  if (themesParam || overlaysParam) {
    newUrl += "?";
    if (themesParam) {
      newUrl += `themes=${themesParam}`;
    }
    if (overlaysParam) {
      if (themesParam) newUrl += "&";
      newUrl += `overlays=${overlaysParam}`;
    }
  }
  window.location.href = newUrl;
}

// 페이지 로드 시 URL에 있는 상태를 읽어와 체크박스와 오버레이 상태를 업데이트합니다.
document.addEventListener("DOMContentLoaded", () => {
  const urlParams = new URLSearchParams(window.location.search);
  const themes = urlParams.get("themes");
  const overlays = urlParams.get("overlays");

  if (themes) {
    themes.split(",").forEach((themeId) => {
      selectedThemes.add(parseInt(themeId));
      document.getElementById(`themecheck${themeId}`).style.display = "block";
    });
  }

  if (overlays) {
    overlays.split(",").forEach((overlayId) => {
      selectedOverlays.add(parseInt(overlayId));
      document.getElementById(`overlayImage${overlayId}`).style.display =
        "block";
    });
  }
});
