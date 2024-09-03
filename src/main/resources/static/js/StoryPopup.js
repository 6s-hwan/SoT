// 전역 변수 정의
let currentStoryId = null;
let isLiked = false;
let isBookmarked = false;
let isAuthenticated = false;

// disableButtonsIfNotAuthenticated 함수 정의
function disableButtonsIfNotAuthenticated(isAuthenticated) {
    const likeButton = document.querySelector(".Story_like");
    const bookmarkButton = document.querySelector(".Story_book");

    if (!isAuthenticated) {
        likeButton.disabled = true;
        bookmarkButton.disabled = true;
        likeButton.querySelector("img").style.opacity = "0.5";
        bookmarkButton.querySelector("img").style.opacity = "0.5";
    } else {
        likeButton.disabled = false;
        bookmarkButton.disabled = false;
        likeButton.querySelector("img").style.opacity = "1";
        bookmarkButton.querySelector("img").style.opacity = "1";
    }
}

function updateLikeButton() {
    const likeButton = document.querySelector(".Story_like img");
    const likesElement = document.getElementById("popupLikes");

    if (isLiked) {
        likeButton.src = "/images/like.png"; // 좋아요 누른 상태를 나타내는 이미지로 변경
        if (!likesElement.textContent.includes(" ✓")) {
            likesElement.textContent += " ✓"; // 좋아요 수 옆에 체크 표시 추가
        }
        likeButton.title = "좋아요 취소"; // 툴팁으로 '좋아요 취소' 표시
    } else {
        likeButton.src = "/images/favorite.png"; // 기본 이미지
        likesElement.textContent = likesElement.textContent.replace(" ✓", ""); // 체크 표시 제거
        likeButton.title = "좋아요"; // 툴팁으로 '좋아요' 표시
    }
}

// 북마크 버튼 상태 업데이트 함수
function updateBookmarkButton() {
    const bookmarkButton = document.querySelector(".Story_book img");
    const bookmarksElement = document.getElementById("popupBookmarks");

    if (isBookmarked) {
        bookmarkButton.src = "/images/bookmarked.png"; // 북마크 누른 상태를 나타내는 이미지로 변경
        if (!bookmarksElement.textContent.includes(" ✓")) {
            bookmarksElement.textContent += " ✓"; // 북마크 수 옆에 체크 표시 추가
        }
        bookmarkButton.title = "북마크 취소"; // 툴팁으로 '북마크 취소' 표시
    } else {
        bookmarkButton.src = "/images/StoryDetailbtn2.png"; // 기본 이미지
        bookmarksElement.textContent = bookmarksElement.textContent.replace(" ✓", ""); // 체크 표시 제거
        bookmarkButton.title = "북마크"; // 툴팁으로 '북마크' 표시
    }
}

// showStoryPopup 함수 정의
function showStoryPopup(storyId) {
    currentStoryId = storyId;

    // 스토리 세부 정보 가져오기
    fetch(`/story/detail/${storyId}`, {
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            // 팝업에 스토리 정보 채우기
            document.getElementById("popupTitle").textContent = data.title;
            document.getElementById("popupImage").src = data.imageUrl;
            document.getElementById("popupAddress").textContent = data.location;
            document.getElementById("popupDescription").textContent = data.description;
            document.getElementById("popupProfileImage").src = data.profileImageUrl;
            document.getElementById("popupProfileName").textContent = data.username;
            document.getElementById("popupProfileFollowers").textContent = `팔로워 ${data.followersCount}명`;
            document.getElementById("popupLikes").textContent = `좋아요 ${data.likesCount}개`;
            document.getElementById("popupBookmarks").textContent = `북마크 ${data.bookmarksCount}개`;
            document.getElementById("popupDate").textContent = `날짜: ${data.date}`;

            // 사용자 인증 상태와 좋아요/북마크 상태를 저장
            isAuthenticated = data.authenticated;
            isLiked = data.likedByUser;
            isBookmarked = data.bookmarkedByUser;

            // UI 업데이트
            disableButtonsIfNotAuthenticated(isAuthenticated);
            updateLikeButton();
            updateBookmarkButton();

            // 태그 초기화 및 추가
            const tagsContainer = document.getElementById("popupTagsContainer");
            tagsContainer.innerHTML = '';
            const tags = data.tags.split(',');
            tags.forEach(tag => {
                const tagElement = document.createElement('p');
                tagElement.className = 'tag1_text';
                tagElement.textContent = `# ${tag.trim()}`;
                tagsContainer.appendChild(tagElement);
            });

            // 검색 결과 개수 가져오기
            fetch(`/api/search-count?query=${encodeURIComponent(data.title)}`, {
                headers: {
                    'Accept': 'application/json'
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(resultData => {
                    document.getElementById("searchResultCount").textContent = `총 ${resultData.resultCount}개`;
                });

            // 프로필 관련 요소에 클릭 이벤트 추가
            const profileLink = `/writer/${data.username}`;
            document.getElementById("popupProfileImage").onclick = () => { window.location.href = profileLink; };
            document.getElementById("popupProfileName").onclick = () => { window.location.href = profileLink; };
            document.getElementById("popupProfileFollowers").onclick = () => { window.location.href = profileLink; };

            // 팝업을 표시
            showPopup();
        })
        .catch(error => console.error('Error fetching story details:', error));
}

// 팝업 이미지 크기 조정 함수
function adjustImageSize() {
    const container = document.querySelector('.container');
    const bg_gray9 = document.querySelector('.bg_gray9');
    const related = document.querySelector('.related');
    const image = document.getElementById('popupImage');

    const imageWidth = image.naturalWidth;
    const imageHeight = image.naturalHeight;

    let newWidth, newHeight;

    if (imageWidth < imageHeight) {
        newWidth = 390;
        newHeight = (imageHeight / imageWidth) * 390;
    } else {
        newHeight = 350;
        newWidth = (imageWidth / imageHeight) * 350;
    }

    image.style.width = `${newWidth}px`;
    image.style.height = `${newHeight}px`;

    container.style.width = `${newWidth}px`;
    container.style.height = `${newHeight + 295}px`;

    bg_gray9.style.height = `${newHeight + 610}px`;

    related.style.width = `${newWidth}px`;
}

function showPopup() {
    document.getElementById("storyPopup").style.display = "block";
    document.querySelector(".bg_gray9").style.display = "block"; // 배경 표시
    adjustImageSize(); // 팝업을 열 때 이미지 크기 조정
}
function closePopup9() {
    console.log("closePopup 함수가 호출되었습니다."); // 함수 호출 확인용 로그
    const popup = document.getElementById("storyPopup");
    const background = document.querySelector(".bg_gray9");

    if (popup) {
        popup.style.display = "none"; // 팝업 닫기
    } else {
        console.error("팝업 요소를 찾을 수 없습니다.");
    }

    if (background) {
        background.style.display = "none"; // 배경 숨기기
    } else {
        console.error("배경 요소를 찾을 수 없습니다.");
    }
}

// toggleLike 함수 정의
function toggleLike() {
    if (!currentStoryId || !isAuthenticated) {
        console.error('Story ID is undefined or user is not authenticated');
        return;
    }

    fetch(`/story/${currentStoryId}/like`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                const likesElement = document.getElementById("popupLikes");
                let currentLikes = parseInt(likesElement.textContent.replace('좋아요 ', '').replace('개', ''), 10);

                if (isLiked) {
                    likesElement.textContent = `좋아요 ${currentLikes - 1}개`;
                } else {
                    likesElement.textContent = `좋아요 ${currentLikes + 1}개`;
                }

                isLiked = !isLiked; // 상태 반전
                updateLikeButton(); // UI 업데이트
            }
        })
        .catch(error => console.error('Error toggling like:', error));
}

// toggleBookmark 함수 정의
function toggleBookmark() {
    if (!currentStoryId || !isAuthenticated) {
        console.error('Story ID is undefined or user is not authenticated');
        return;
    }

    fetch(`/story/${currentStoryId}/bookmark`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                const bookmarksElement = document.getElementById("popupBookmarks");
                let currentBookmarks = parseInt(bookmarksElement.textContent.replace('북마크 ', '').replace('개', ''), 10);

                if (isBookmarked) {
                    bookmarksElement.textContent = `북마크 ${currentBookmarks - 1}개`;
                } else {
                    bookmarksElement.textContent = `북마크 ${currentBookmarks + 1}개`;
                }

                isBookmarked = !isBookmarked; // 상태 반전
                updateBookmarkButton(); // UI 업데이트
            }
        })
        .catch(error => console.error('Error toggling bookmark:', error));
}

function searchByTitle() {
    const query = document.getElementById("popupTitle").textContent;
    window.location.href = `/search?query=${encodeURIComponent(query)}`;
}

window.onresize = adjustImageSize;
