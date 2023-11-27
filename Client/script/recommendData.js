const url = "http://localhost:8080/problem?id=wwqw58";

fetch(url)
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    const apiData = data.data;
    const tableBody = document.getElementById("dataBody");

    apiData.forEach((item) => {
      const row = document.createElement("tr");
      row.addEventListener("click", () => {
        window.location.href = `https://www.acmicpc.net/problem/${item.number}`;
      });

      const idCell = document.createElement("td");
      idCell.textContent = item.number;
      row.appendChild(idCell);

      const nameCell = document.createElement("td");
      nameCell.textContent = item.name;
      row.appendChild(nameCell);

      const tierCell = document.createElement("td");
      tierCell.textContent = item.tier;
      row.appendChild(tierCell);

      const algorithmCell = document.createElement("td");
      algorithmCell.textContent = item.algorithm; // 배열을 문자열로 변환하여 출력
      row.appendChild(algorithmCell);

      tableBody.appendChild(row);
    });
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
