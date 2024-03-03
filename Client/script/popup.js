document
  .getElementById("analyzeButton")
  .addEventListener("click", analyzeButtonHandler);

function analyzeButtonHandler() {
  chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
    const currentTab = tabs[0];
    submitNumber = document.getElementById("submitNumber").value;
    const onUpdatedCallback = function (tabId, info) {
      if (info.status === "complete") {
        chrome.tabs.onUpdated.removeListener(onUpdatedCallback);
        executeScriptOnTab(
          currentTab.id,
          scrapeTargetData,
          handleScriptResults
        );
      }
    };
    chrome.tabs.onUpdated.addListener(onUpdatedCallback);

    const targetURL = "https://www.acmicpc.net/source/" + submitNumber;
    if (currentTab.url !== targetURL) {
      chrome.tabs.update({ url: targetURL });
    } else {
      executeScriptOnTab(currentTab.id, scrapeTargetData, handleScriptResults);
    }
  });
}

function executeScriptOnTab(tabId, scriptFunction, callback) {
  chrome.scripting.executeScript(
    {
      target: { tabId: tabId },
      func: scriptFunction,
    },
    callback
  );
}

function scrapeTargetData() {
  //코드
  let lines = document.querySelector("body").innerText.split("\n");
  let codeLines = [];

  let startIdx = 0;
  for (let i = 0; i < lines.length; i++) {
    if (lines[i].startsWith("1")) {
      startIdx = i + 1;
      break;
    }
  }

  let endIdx = 0;
  for (let i = startIdx + 1; i < lines.length; i++) {
    if (lines[i].startsWith("복사다운로드")) {
      endIdx = i - 1;
      break;
    }
    if (lines[i].startsWith("소스 코드 공개")) {
      endIdx = i - 1;
      break;
    }
  }

  for (let i = startIdx; i <= endIdx; i += 2) {
    codeLines.push(lines[i]);
  }

  //접근할 수 없는 코드의 경우
  if (codeLines.length == 0) {
    return "접근할 수 없는 제출 번호입니다.";
  }

  //접근할 수 있는 코드의 경우
  else {
    let targetData = [];
    targetData.push(codeLines.join("\n"));
    targetData.push(document.querySelector("tbody").innerText.split("\t")[2]);

    //맞았습니다!!
    const submitResult = document
      .querySelector("tbody")
      .innerText.split("\t")[4];
    if (submitResult == "맞았습니다!!") {
      targetData.push(document.querySelector("tbody").innerText.split("\t")[5]);
      targetData.push(document.querySelector("tbody").innerText.split("\t")[6]);
      targetData.push(document.querySelector("tbody").innerText.split("\t")[7]);
    }

    return targetData.join("\t");
  }
}

function handleScriptResults(results) {
  if (chrome.runtime.lastError) {
    console.error(chrome.runtime.lastError);
    return;
  }

  if (results && results[0]) {
    let targetData = results[0].result.split("\t");

    if (targetData.length == 5) {
      //맞았습니다!!
      const requestData = {
        number: targetData[1],
        time: targetData[3],
        memory: targetData[2],
        language: targetData[4],
        code: targetData[0],
      };

      fetch("http://172.16.42.205:8080/analysis/correct", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      })
        .then((response) => {
          return response.json();
        })
        .then((data) => {
          showResponse(data.data);
          function showResponse(data) {
            document.querySelector(".loader").style.display = "none";

            const showMoreButton1 = document.getElementById("showMoreButton1");
            const codeSections1 = document.querySelectorAll(".code-section1");
            showMoreButton1.innerText = "see more";
            showMoreButton1.style.marginLeft = "20px";
            showMoreButton1.style.fontSize = "10px";
            showMoreButton1.style.border = "1px solid var(--border-color)";
            showMoreButton1.style.padding = "8px";
            showMoreButton1.style.borderRadius = "15px";
            showMoreButton1.style.boxShadow =
              "0px 4px 4px 0px rgba(0, 0, 0, 0.25)";
            showMoreButton1.style.cursor = "pointer";

            showMoreButton1.addEventListener("click", function () {
              codeSections1.forEach((section) =>
                section.classList.toggle("show-more")
              );
            });
            // 버튼 생성
            const showMoreButton2 = document.getElementById("showMoreButton2");
            const codeSections2 = document.querySelectorAll(".code-section2");
            showMoreButton2.innerText = "see more";
            showMoreButton2.style.marginLeft = "30px";
            showMoreButton2.style.fontSize = "10px";
            showMoreButton2.style.border = "1px solid var(--border-color)";
            showMoreButton2.style.padding = "8px";
            showMoreButton2.style.borderRadius = "15px";
            showMoreButton2.style.boxShadow =
              "0px 4px 4px 0px rgba(0, 0, 0, 0.25)";
            showMoreButton2.style.cursor = "pointer";
            showMoreButton2.addEventListener("click", function () {
              codeSections2.forEach((section) =>
                section.classList.toggle("show-more")
              );
            });

            let timeComplexity = document.getElementById("timeComplexity");
            let spanTimeComplexity = document.getElementById("Time");
            spanTimeComplexity.innerText = "Time";
            spanTimeComplexity.style.fontSize = "11px";
            spanTimeComplexity.style.fontWeight = "700";

            spanTimeComplexity.style.marginBottom = "5px"; // Add margin-bottom to the div
            spanTimeComplexity.style.display = "flex"; // Set display to flex for horizontal layout
            spanTimeComplexity.style.justifyContent = "space-between"; // Arrange content with space-between
            spanTimeComplexity.style.alignItems = "center"; // Center align items vertically

            let spaceComplexity = document.getElementById("spaceComplexity");
            let spanSpaceComplexity = document.getElementById("Space");
            spanSpaceComplexity.innerText = "Space";
            spanSpaceComplexity.style.fontSize = "11px";
            spanSpaceComplexity.style.marginBottom = "5px"; // Add margin-bottom to the div
            spanSpaceComplexity.style.display = "flex"; // Set display to flex for horizontal layout
            spanSpaceComplexity.style.justifyContent = "space-between"; // Arrange content with space-between
            spanSpaceComplexity.style.alignItems = "center"; // Center align items vertically
            spanSpaceComplexity.style.fontWeight = "700"; // Center align items vertically

            let algorithm = document.getElementById("algorithm");
            let spanAlgorithm = document.getElementById("Algorithm");
            spanAlgorithm.style.fontSize = "11px";
            spanAlgorithm.innerText = "Algorithm";
            spanAlgorithm.style.marginBottom = "5px"; // Add margin-bottom to the div
            spanAlgorithm.style.display = "flex"; // Set display to flex for horizontal layout
            spanAlgorithm.style.justifyContent = "space-between"; // Arrange content with space-between
            spanAlgorithm.style.alignItems = "center"; // Center align items vertically
            spanAlgorithm.style.fontWeight = "700"; // Center align items vertically

            // Set text content using innerText
            document.getElementById("TimeContent").innerText = data.bigO;
            document.getElementById("SpaceContent").innerText =
              data.spaceComplexDegree;
            document.getElementById("AlgorithmContent").innerText =
              data.whatAlgo;

            // Apply styles to the div elements (timeComplexity, spaceComplexity, algorithm)
            [timeComplexity, spaceComplexity, algorithm].forEach((div) => {
              div.style.width = "50px"; // Set a fixed width
              div.style.height = "50px"; // Set a fixed height
              div.style.border = "1px solid #ccc";
              div.style.display = "flex";
              div.style.flexDirection = "column";
              div.style.justifyContent = "center";
              div.style.alignItems = "center";
              div.style.padding = "10px";
              div.style.fontSize = "10px";
              div.style.borderRadius = "15px";
              div.style.textAlign = "center";
              div.style.marginBottom = "15px";
              div.style.boxShadow = "0px 4px 4px 0px rgba(0, 0, 0, 0.25)";
            });

            // Add class to the parent container
            document
              .getElementById("peronsalAnalyze")
              .classList.add("styled-text");

            // Set text and add class for other elements
            document.getElementById("bestTimeComplexityTitle").innerText =
              "bestTimeComplexity";
            document.getElementById("bestTimeComplexityCode").innerText =
              data.compareOtherBigOCode;
            document.getElementById("bestTimeComplexityWords").innerText =
              data.compareOtherBigO;
            document
              .getElementById("bestTimeComplexity")
              .classList.add("styled-text");
            document.getElementById("bestSpaceComplexityTitle").innerText =
              "bestSpaceComplexity";
            document.getElementById("bestSpaceComplexityCode").innerText =
              data.compareOtherBigOCode;
            document.getElementById("bestSpaceComplexityWords").innerText =
              data.compareOtherSpaceComplexDegree;

            document
              .getElementById("bestSpaceComplexity")
              .classList.add("styled-text");
          }
        });
    } else {
      //그 와
      const requestData = {
        number: targetData[1],
        code: targetData[0],
      };

      fetch("http://172.16.42.205:8080/analysis/wrong", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      })
        .then((response) => {
          console.log("위위쪽" + response);
          return response.json();
        })
        .then((data) => {
          console.log("위쪽" + data);
          showHint(data.data); //힌트 api response 보고 수정해줘야 함
        });
    }
  }
}

function handleScriptResults2(results) {
  console.log(results[0]);

  if (result) {
    console.log("code");
  } else {
    console.log("hint");
  }
}

function showHint(data) {
  console.log(data);
  document.getElementById("hint").innerText = data.contents;
}

document.getElementById("analyzeButton").addEventListener("click", function () {
  // 버튼 클릭 시 loader 표시
  document.querySelector(".loader").style.display = "block";

  // 3초 후에 showResponse 함수 호출
  setTimeout(showResponse, 3000);
});
