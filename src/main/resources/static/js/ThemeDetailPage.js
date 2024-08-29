// 선택된 테마를 저장하는 Set
const selectedThemes = new Set();

// 테마를 클릭하면 테마 선택 및 페이지 리디렉션을 수행하는 함수
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

  // 현재 선택된 정렬 기준 가져오기
  const currentUrl = new URL(window.location.href);
  const sortParam = currentUrl.searchParams.get('sort') || '';

  // 선택된 테마와 정렬 기준을 포함한 새로운 URL로 리디렉션
  const newUrl = `/theme?themes=${encodeURIComponent(themesParam)}&sort=${encodeURIComponent(sortParam)}`;
  window.location.href = newUrl;
}

// 정렬 폼을 제출하는 함수 (정렬 기준 변경 시 호출)
function submitSortForm() {
  const hiddenThemes = document.getElementById("hiddenThemes");
  hiddenThemes.value = Array.from(selectedThemes).sort((a, b) => a - b).join(',');

  // 폼 제출
  document.getElementById("sortForm").submit();
}

// 페이지 로드 시 URL에 있는 테마 ID를 읽어와 체크박스 상태를 업데이트
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
