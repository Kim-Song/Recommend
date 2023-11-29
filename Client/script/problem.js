const url = "http://localhost:8080/problem?id=wwqw58";

const tierDict = {
  6: "Silver5",
  7: "Silver4",
  8: "Silver3",
  9: "Silver2",
  10: "Silver1",
  11: "Gold5",
  12: "Gold4",
  13: "Gold3",
  14: "Gold2",
  15: "Gold1",
  16: "Platinum5",
  17: "Platinum4",
  18: "Platinum3",
  19: "Platinum2",
  20: "Platinum1",
  21: "Diamond5",
  22: "Diamond4",
};

function createRow(item) {
  const row = document.createElement("tr");

  const toggleButton = document.createElement("button");
  toggleButton.textContent = "Toggle";
  let algorithmVisible = false;

  toggleButton.addEventListener("click", (event) => {
    event.stopPropagation();
    algorithmVisible = !algorithmVisible;
    const algorithmCell = row.querySelector(".algorithm-cell");
    if (algorithmVisible) {
      algorithmCell.style.display = "table-cell";
    } else {
      algorithmCell.style.display = "none";
    }
  });

  row.addEventListener("click", () => {
    window.open(`https://www.acmicpc.net/problem/${item.number}`, "_blank");
  });

  const idCell = createCell(item.number, "center");
  row.appendChild(idCell);

  const nameTierContainer = document.createElement("td");
  nameTierContainer.style.display = "flex";
  nameTierContainer.style.alignItems = "center";
  nameTierContainer.style.justifyContent = "center"; // 가로 방향 가운데 정렬
  nameTierContainer.style.textAlign = "center";

  const nameTierSpan = document.createElement("span");
  const tierNumber = item.tier;
  nameTierSpan.textContent = item.name;
  const tierImg = document.createElement("img");
  tierImg.src = `https://static.solved.ac/tier_small/${tierNumber}.svg`;
  tierImg.alt = `Tier ${tierNumber}`;
  tierImg.style.width = "20px";
  tierImg.style.height = "20px";
  nameTierContainer.appendChild(tierImg);

  nameTierContainer.appendChild(nameTierSpan);

  row.appendChild(nameTierContainer);

  const algorithmCell = createCell(item.algorithm, "center");
  algorithmCell.classList.add("algorithm-cell");
  algorithmCell.style.display = "none";
  row.appendChild(algorithmCell);

  const toggleCell = document.createElement("td");
  toggleCell.appendChild(toggleButton);
  toggleCell.style.textAlign = "center";
  row.appendChild(toggleCell);
  applyTierColor(row, tierNumber);
  return row;
}

function createCell(text, align) {
  const cell = document.createElement("td");
  cell.textContent = text;
  cell.style.textAlign = align;
  return cell;
}

function applyTierColor(row, tierNumber) {
  const toggleButton = row.querySelector("button");

  if (tierDict[tierNumber] && tierDict[tierNumber].startsWith("Silver")) {
    row.style.color = "rgb(56, 84, 110)";
    toggleButton.style.color = "rgb(56, 84, 110)";
    toggleButton.style.border = "1px solid rgb(56, 84, 110)";
  } else if (tierDict[tierNumber] && tierDict[tierNumber].startsWith("Gold")) {
    row.style.color = "rgb(210, 133, 0)";
    toggleButton.style.color = "rgb(210, 133, 0)";
    toggleButton.style.border = "1px solid rgb(210, 133, 0)";
  } else if (
    tierDict[tierNumber] &&
    tierDict[tierNumber].startsWith("Platinum")
  ) {
    row.style.color = "rgb(0, 199, 139)";
    toggleButton.style.color = "rgb(0, 199, 139)";
    toggleButton.style.border = "1px solid rgb(0, 199, 139)";
  } else if (
    tierDict[tierNumber] &&
    tierDict[tierNumber].startsWith("Diamond")
  ) {
    row.style.color = "rgb(0, 158, 229)";
    toggleButton.style.color = "rgb(0, 158, 229)";
    toggleButton.style.border = "1px solid rgb(0, 158, 229)";
  }
  toggleButton.addEventListener("mouseover", () => {
    if (tierDict[tierNumber] && tierDict[tierNumber].startsWith("Silver")) {
      toggleButton.style.backgroundColor = "rgb(56, 84, 110)";
      toggleButton.style.color = "white";
    } else if (
      tierDict[tierNumber] &&
      tierDict[tierNumber].startsWith("Gold")
    ) {
      toggleButton.style.backgroundColor = "rgb(210, 133, 0)";
      toggleButton.style.color = "white";
    } else if (
      tierDict[tierNumber] &&
      tierDict[tierNumber].startsWith("Platinum")
    ) {
      toggleButton.style.backgroundColor = "rgb(0, 199, 139)";
      toggleButton.style.color = "white";
    } else if (
      tierDict[tierNumber] &&
      tierDict[tierNumber].startsWith("Diamond")
    ) {
      toggleButton.style.backgroundColor = "rgb(0, 158, 229)";
      toggleButton.style.color = "white";
    }
  });

  toggleButton.addEventListener("mouseout", () => {
    if (tierDict[tierNumber] && tierDict[tierNumber].startsWith("Silver")) {
      toggleButton.style.backgroundColor = "transparent";
      toggleButton.style.color = "rgb(56, 84, 110)"; // 기존 티어 색상으로 텍스트 색 원복
    } else if (
      tierDict[tierNumber] &&
      tierDict[tierNumber].startsWith("Gold")
    ) {
      toggleButton.style.backgroundColor = "transparent";
      toggleButton.style.color = "rgb(210, 133, 0)"; // 기존 티어 색상으로 텍스트 색 원복
    } else if (
      tierDict[tierNumber] &&
      tierDict[tierNumber].startsWith("Platinum")
    ) {
      toggleButton.style.backgroundColor = "transparent";
      toggleButton.style.color = "rgb(0, 199, 139)"; // 기존 티어 색상으로 텍스트 색 원복
    } else if (
      tierDict[tierNumber] &&
      tierDict[tierNumber].startsWith("Diamond")
    ) {
      toggleButton.style.backgroundColor = "transparent";
      toggleButton.style.color = "rgb(0, 158, 229)"; // 기존 티어 색상으로 텍스트 색 원복
    }
  });
}

fetch(url)
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    const apiData = data.data;
    const tableBody = document.getElementById("dataBody");

    apiData.forEach((item) => {
      const row = createRow(item);
      tableBody.appendChild(row);
    });
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
