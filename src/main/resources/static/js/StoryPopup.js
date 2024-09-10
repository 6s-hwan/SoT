// 전역 변수 정의
let currentStoryId = null;
let isLiked = false;
let isBookmarked = false;
let isAuthenticated = false;
let isOwnStory = false; // 자신의 스토리 여부

// 좋아요와 북마크 버튼 비활성화 처리
function disableButtonsIfOwnStory(isOwnStory) {
    const likeButton = document.querySelector(".Story_like");
    const bookmarkButton = document.querySelector(".Story_book");

    if (isOwnStory) {
        // 버튼 비활성화
        likeButton.disabled = true;
        bookmarkButton.disabled = true;

        // 버튼 클릭 이벤트 제거 (좋아요, 북마크 기능 무효화)
        likeButton.onclick = null;
        bookmarkButton.onclick = null;

        // 버튼 이미지 반투명 처리
        likeButton.querySelector("img").style.opacity = "0.5";
        bookmarkButton.querySelector("img").style.opacity = "0.5";
    } else {
        // 버튼 활성화
        likeButton.disabled = false;
        bookmarkButton.disabled = false;

        // 클릭 이벤트 복구
        likeButton.onclick = toggleLike;
        bookmarkButton.onclick = toggleBookmark;

        // 버튼 이미지 원래 상태로 복구
        likeButton.querySelector("img").style.opacity = "1";
        bookmarkButton.querySelector("img").style.opacity = "1";
    }
}

// 인증되지 않은 사용자를 위한 버튼 비활성화
function disableButtonsIfNotAuthenticated(isAuthenticated) {
    const likeButton = document.querySelector(".Story_like");
    const bookmarkButton = document.querySelector(".Story_book");

    if (!isAuthenticated) {
        likeButton.disabled = true;
        bookmarkButton.disabled = true;

        // 버튼 클릭 이벤트 제거
        likeButton.onclick = null;
        bookmarkButton.onclick = null;

        likeButton.querySelector("img").style.opacity = "0.5";
        bookmarkButton.querySelector("img").style.opacity = "0.5";
    } else {
        likeButton.disabled = false;
        bookmarkButton.disabled = false;

        // 클릭 이벤트 복구
        likeButton.onclick = toggleLike;
        bookmarkButton.onclick = toggleBookmark;

        likeButton.querySelector("img").style.opacity = "1";
        bookmarkButton.querySelector("img").style.opacity = "1";
    }
}

// 좋아요 버튼 상태 업데이트
function updateLikeButton() {
    const likeButton = document.querySelector(".Story_like img");

    // 현재 경로가 /my-page일 경우 무조건 like.png로 설정
    if (window.location.pathname === '/my-page') {
        likeButton.src = "/images/like.png";
        likeButton.title = "좋아요 취소";
        return;
    }

    // 일반적인 경우
    if (isLiked) {
        likeButton.src = "/images/like.png";
        likeButton.title = "좋아요 취소";
    } else {
        likeButton.src = "/images/favorite.png";
        likeButton.title = "좋아요";
    }
}

// 북마크 버튼 상태 업데이트
function updateBookmarkButton() {
    const bookmarkButton = document.querySelector(".Story_book img");

    // 현재 경로가 /my-page일 경우 무조건 bookmarked.png로 설정
    if (window.location.pathname === '/my-page') {
        bookmarkButton.src = "/images/bookmarked.png";
        return;
    }

    // 일반적인 경우
    if (isBookmarked) {
        bookmarkButton.src = "/images/bookmarked.png";
    } else {
        bookmarkButton.src = "/images/StoryDetailbtn2.png";
    }
}


// 스토리 세부 정보 팝업 표시
function showStoryPopup(storyId) {
    currentStoryId = storyId;

    fetch(`/story/detail/${storyId}`, {
        headers: { 'Accept': 'application/json' }
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
            document.getElementById("popupLikes").textContent = data.likesCount;
            document.getElementById("popupBookmarks").textContent = data.bookmarksCount;
            document.getElementById('popupTitle2').textContent = `'${data.title}'`;

            // 날짜 포맷팅
            const date = new Date(data.date);
            document.getElementById("popupDate").textContent = `${String(date.getFullYear()).slice(2)}년 ${date.getMonth() + 1}월`;

            // 인증 상태 및 좋아요/북마크 상태 업데이트
            isAuthenticated = data.authenticated;
            isLiked = data.likedByUser;
            isBookmarked = data.bookmarkedByUser;
            isOwnStory = data.ownStory;


            disableButtonsIfNotAuthenticated(isAuthenticated);
            disableButtonsIfOwnStory(isOwnStory);
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
                headers: { 'Accept': 'application/json' }
            })
                .then(response => response.json())
                .then(resultData => {
                    document.getElementById("searchResultCount").textContent = resultData.resultCount;
                });

            // 프로필 이미지 클릭 이벤트
            const profileLink = `/writer/${data.username}`;
            document.getElementById("popupProfileImage").onclick = () => window.location.href = profileLink;
            document.getElementById("popupProfileName").onclick = () => window.location.href = profileLink;
            document.getElementById("popupProfileFollowers").onclick = () => window.location.href = profileLink;

            // 팝업 표시
            showPopupd();
        })
        .catch(error => console.error('Error fetching story details:', error));
}

// 팝업 이미지 크기 조정
function adjustImageSize() {
    const image = document.getElementById('popupImage');
    const container = document.querySelector('.container');
    const bg_gray9 = document.querySelector('.bg_gray9');
    const related = document.querySelector('.related');

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

function showPopupd() {
    document.getElementById("storyPopup").style.display = "block";
    document.querySelector(".bg_gray9").style.display = "block";
    adjustImageSize();
}

function closePopup9() {
    const popup = document.getElementById("storyPopup");
    const background = document.querySelector(".bg_gray9");

    if (popup) popup.style.display = "none";
    if (background) background.style.display = "none";

    // 현재 경로가 /bookmark이면 페이지 새로고침
    if (window.location.pathname === '/bookmark') {
        window.location.reload();
    }
    // 현재 경로가 /bookmark이면 페이지 새로고침
    if (window.location.pathname === '/best') {
        window.location.reload();
    }
}

// 좋아요 기능
function toggleLike() {
    const likeButton = document.querySelector(".Story_like");

    // 버튼이 비활성화 상태인 경우, 서버 요청을 차단
    if (likeButton.disabled) {
        console.log("좋아요 버튼이 비활성화되었습니다.");
        return; // 더 이상 요청을 진행하지 않음
    }

    if (!currentStoryId || !isAuthenticated) {
        console.error('스토리 ID가 없거나 사용자가 인증되지 않았습니다.');
        return;
    }

    // 서버로 좋아요 요청
    fetch(`/story/${currentStoryId}/like`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                // 좋아요 상태 업데이트
                const likesElement = document.getElementById("popupLikes");
                let currentLikes = parseInt(likesElement.textContent.replace('좋아요 ', '').replace('개', ''), 10);

                // 좋아요 상태 반전
                if (isLiked) {
                    likesElement.textContent = currentLikes - 1;
                } else {
                    likesElement.textContent = currentLikes + 1;
                }
                isLiked = !isLiked; // 좋아요 상태 반전
                updateLikeButton(); // UI 업데이트
            }
        })
        .catch(error => console.error('좋아요 상태 변경 중 오류 발생:', error));
}

// 북마크 기능
function toggleBookmark() {
    const bookmarkButton = document.querySelector(".Story_book");

    // 버튼이 비활성화 상태인 경우, 서버 요청을 차단
    if (bookmarkButton.disabled) {
        console.log("북마크 버튼이 비활성화되었습니다.");
        return; // 더 이상 요청을 진행하지 않음
    }

    if (!currentStoryId || !isAuthenticated) {
        console.error('스토리 ID가 없거나 사용자가 인증되지 않았습니다.');
        return;
    }

    fetch(`/story/${currentStoryId}/bookmark`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json()) // JSON 응답을 받음
        .then(data => {
            if (data.bookmarked !== undefined) {
                const bookmarksElement = document.getElementById("popupBookmarks");
                let currentBookmarks = parseInt(bookmarksElement.textContent.replace('북마크 ', '').replace('개', ''), 10);

                if (data.bookmarked) {
                    bookmarksElement.textContent = currentBookmarks + 1;
                } else {
                    bookmarksElement.textContent = currentBookmarks - 1;
                }

                isBookmarked = data.bookmarked; // 북마크 상태를 UI에 반영
                updateBookmarkButton();
            }
        })
        .catch(error => console.error('북마크 상태 변경 중 오류 발생:', error));
}
// 삭제 확인 팝업을 띄우고 storyId를 전역 변수에 저장
function toggleDelete(storyId) {
    document.getElementById('bg_gray19').style.display = 'block';  // 삭제 확인 팝업 표시
    window.currentStoryId = storyId;  // 선택된 스토리 ID를 전역 변수에 저장
}
// 스토리 삭제 처리
function submitDeleteForm(storyId) {
    if (!storyId) return;

    fetch(`/api/stories/${storyId}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                throw new Error('스토리 삭제 실패');
            }
        })
        .catch(error => console.error('스토리 삭제 중 오류:', error));
}

// 검색 실행
function searchByTitle() {
    const query = document.getElementById("popupTitle").textContent;
    window.location.href = `/search?query=${encodeURIComponent(query)}`;
}

// 창 크기 조정 시 이미지 크기 조정
window.onresize = adjustImageSize;
