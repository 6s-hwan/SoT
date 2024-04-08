// var imageVisible = false;

// function toggleImage() {
//   var overlayImage = document.getElementById("overlayImage1");
//   if (imageVisible) {
//     overlayImage.style.display = "none"; // 이미지를 숨깁니다.
//   } else {
//     overlayImage.style.display = "block"; // 이미지를 보이게 합니다.
//   }
//   imageVisible = !imageVisible; // 이미지 상태를 토글합니다.
// }
// var imageVisible = false;

// function toggleImage() {
//   var overlayImage = document.getElementById("overlayImage");
//   if (!imageVisible) {
//     overlayImage.style.display = "block"; // 이미지를 보이게 합니다.
//   } else {
//     overlayImage.style.display = "none"; // 이미지를 숨깁니다.
//   }
//   imageVisible = !imageVisible; // 이미지 상태를 토글합니다.
// }

var imageVisible = false;

function toggleImage() {
  var overlayImage = document.getElementById("overlayImage1");
  if (!imageVisible) {
    overlayImage.style.display = "block"; // 이미지를 보이게 합니다.
  } else {
    overlayImage.style.display = "none"; // 이미지를 숨깁니다.
  }
  imageVisible = !imageVisible; // 이미지 상태를 토글합니다.
}
