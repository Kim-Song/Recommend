const carousel = document.querySelector(".carousel");
const carouselItems = document.querySelectorAll(".carousel-item");
const totalItems = carouselItems.length;
const itemWidth = carouselItems[0].offsetWidth;
const itemsPerSlide = 3; // 한 번에 보여지는 아이템 개수
let url1 = "http://localhost:8080/problem?id=wwqw58";
fetch(url1)
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
  });
let url2 = "http://localhost:8080/video?id=wwqw58";
fetch(url2)
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
  });

let currentIndex = 0;

function showSlide() {
  const newPosition = currentIndex * itemWidth * -1;
  carousel.style.transform = 'translateX(${newPosition}px)';
}

function next() {
  currentIndex = (currentIndex + itemsPerSlide) % totalItems;
  showSlide();
}

function prev() {
  currentIndex = (currentIndex - itemsPerSlide + totalItems) % totalItems;
  showSlide();
}

document.querySelector(".prev").addEventListener("click", prev);
document.querySelector(".next").addEventListener("click", next);
