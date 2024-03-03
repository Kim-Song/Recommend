let currentIndex = 0;
const itemsPerSlide = 3; // 한 번에 보여줄 아이템의 개수를 설정해주세요.
let totalItems = 0;
const carousel = document.querySelector(".carousel");
let timer; // 타이머 변수
const intervalTime = 2000; // 3초
let url2 = "http://172.16.42.205:8080/video?id=llsy159";
fetch(url2)
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    let apiData = data.data;

    totalItems = apiData.length;

    startCarousel(); // 캐러셀 시작
    showSlide(currentIndex); // 초기 아이템 표시
    function startCarousel() {
      timer = setInterval(() => {
        showNextItems();
      }, intervalTime);
    }

    function stopCarousel() {
      clearInterval(timer);
    }

    function showNextItems() {
      const nextIndex = (currentIndex + itemsPerSlide) % totalItems;

      showSlide(nextIndex);
    }

    function showSlide(index) {
      currentIndex = index;

      carousel.innerHTML = "";

      const group = apiData.slice(index, index + itemsPerSlide);

      group.forEach((item) => {
        const link = `https://www.youtube.com/watch?v=${item.link}`;
        const thumbnailUrl = `https://img.youtube.com/vi/${item.link}/hqdefault.jpg`; // 썸네일 이미지 URL

        const carouselItem = document.createElement("div");
        carouselItem.classList.add("carousel-item");

        const linkElement = document.createElement("a");
        linkElement.classList.add("carousel-link");
        linkElement.href = link;
        linkElement.target = "_blank";
        linkElement.style.display = "flex";
        linkElement.style.flexDirection = "column";
        linkElement.style.alignItems = "center"; // 이미지와 텍스트를 수직 가운데 정렬

        const thumbnailImage = document.createElement("img");
        thumbnailImage.src = thumbnailUrl;
        thumbnailImage.alt = item.weakAlgorithm;
        thumbnailImage.style.width = "120px"; // 이미지 너비 설정
        thumbnailImage.style.height = "80px"; // 이미지 높이 설정
        thumbnailImage.style.borderRadius = "10px";
        const weakAlgorithmElement = document.createElement("div");
        weakAlgorithmElement.textContent = item.weakAlgorithm;
        weakAlgorithmElement.style.marginTop = "10px"; // 텍스트에 위쪽 마진 추가

        linkElement.appendChild(thumbnailImage);
        linkElement.appendChild(weakAlgorithmElement); // weakAlgorithm 추가
        carouselItem.appendChild(linkElement);
        carousel.appendChild(carouselItem);
      });
    }

    carousel.addEventListener("mouseenter", () => {
      stopCarousel();
    });

    carousel.addEventListener("mouseleave", () => {
      startCarousel();
    });
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
