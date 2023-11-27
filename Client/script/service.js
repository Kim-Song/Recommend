// contentScript.js

let currentIndex = 0;
const itemWidth = 300; // 아이템의 가로 크기를 임의로 설정했습니다. 실제 값에 맞게 수정해주세요.
const itemsPerSlide = 1; // 한 번에 보여줄 아이템의 개수를 설정해주세요.
let totalItems = 0;
const carousel = document.getElementById("carousel");

// 데이터를 불러와 케러셀 아이템 생성하고, 슬라이드 기능을 추가하는 함수
function createCarouselWithSlide() {
  let url2 = "http://localhost:8080/video?id=wwqw58";
  fetch(url2)
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      const apiData = data.data;
      totalItems = apiData.length; // 데이터의 개수를 전역 변수에 저장합니다.

      apiData.forEach((item) => {
        const link = `https://www.youtube.com/watch?v=${item.link}`;
        const carouselItem = document.createElement("div");
        carouselItem.classList.add("carousel-item");

        const linkElement = document.createElement("span");
        linkElement.href = link;
        linkElement.textContent = item.weakAlgorithm;

        linkElement.addEventListener("click", (event) => {
          event.preventDefault();
          chrome.tabs.create({ url: link });
        });

        carouselItem.appendChild(linkElement);
        carousel.appendChild(carouselItem); // 슬라이드 기능을 추가할 캐러셀 요소에 아이템을 추가합니다.
      });

      // 케러셀 슬라이드 기능 추가
      document.querySelector(".prev").addEventListener("click", prev);
      document.querySelector(".next").addEventListener("click", next);

      // 아이템 수가 10개를 넘어갈 경우, 인덱스를 처음으로 되돌립니다.
      if (totalItems > 10) {
        currentIndex = totalItems - 10; // 처음 인덱스를 설정합니다.
        showSlide();
      }
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
    });
}

function showSlide() {
  const newPosition = currentIndex * itemWidth * -1;
  carousel.style.transform = `translateX(${newPosition}px)`;
}

function next() {
  currentIndex = (currentIndex + itemsPerSlide) % totalItems;
  showSlide();
}

function prev() {
  currentIndex = (currentIndex - itemsPerSlide + totalItems) % totalItems;
  showSlide();
}

// 페이지 로드 시 케러셀 생성과 슬라이드 초기화
window.addEventListener("DOMContentLoaded", createCarouselWithSlide);
