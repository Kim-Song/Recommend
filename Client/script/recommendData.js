const originalQuestions = [
  {
    id: 10111,
    name: "구슬 탈출 2",
    percentage: 11.5,
  },
  {
    id: 302011,
    name: "2048 (Easy)",
    percentage: 30.4,
  },
  {
    id: 402030,
    name: "테트로미노",
    percentage: 45,
  },
  {
    id: 202022,
    name: "스타트와 링크",
    percentage: 0.9,
  },
  {
    id: 132131,
    name: "백준 친구 프로그래머스",
    percentage: 2,
  },
  {
    id: 412412,
    name: "드래곤 커브",
    percentage: 10,
  },
  {
    id: 71111,
    name: "축구 경기 예측",
    percentage: 10,
  },
];

let questions = [...originalQuestions]; // 복사된 배열 사용

function getRandomQuestions() {
  const selectedQuestions = [];
  const numQuestionsToSelect = 3;

  for (let i = 0; i < numQuestionsToSelect; i++) {
    const randomIndex = Math.floor(Math.random() * questions.length);
    const randomQuestion = questions[randomIndex];
    selectedQuestions.push(randomQuestion);
  }

  return selectedQuestions;
}

function displayRandomQuestions() {
  const selectedRandomQuestions = getRandomQuestions();

  const randomQuestionsBody = document.getElementById("randomQuestionsBody");
  randomQuestionsBody.innerHTML = "";

  selectedRandomQuestions.forEach(function (question) {
    const row = document.createElement("tr");

    const idCell = document.createElement("td");
    idCell.textContent = question.id;
    row.appendChild(idCell);

    const nameCell = document.createElement("td");
    nameCell.textContent = question.name;
    row.appendChild(nameCell);

    const percentageCell = document.createElement("td");
    percentageCell.textContent = question.percentage + "%";
    row.appendChild(percentageCell);

    randomQuestionsBody.appendChild(row);
  });
}

document
  .getElementById("updateButton")
  .addEventListener("click", displayRandomQuestions);
window.onload = displayRandomQuestions;
