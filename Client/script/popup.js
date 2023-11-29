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

      fetch("http://localhost:8080/analysis", {
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
        });
    } else {
      //그 와
      const requestData = {
        number: targetData[1],
        code: targetData[0],
      };

      fetch("", {
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
          showResponse(data.data); //힌트 api response 보고 수정해줘야 함
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

function showResponse(data) {
  // 버튼 생성
  /*  margin-left: 30px;
  border: 1px solid var(--border-color);
  padding: 8px;
  border-radius: 15px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  cursor: pointer;*/
  const showMoreButton1 = document.getElementById("showMoreButton1");
  const codeSections1 = document.querySelectorAll(".code-section1");
  showMoreButton1.innerText = "see more";
  showMoreButton1.style.marginLeft = "30px";
  showMoreButton1.style.fontSize = "10px";
  showMoreButton1.style.border = "1px solid var(--border-color)";
  showMoreButton1.style.padding = "8px";
  showMoreButton1.style.borderRadius = "15px";
  showMoreButton1.style.boxShadow = "0px 4px 4px 0px rgba(0, 0, 0, 0.25)";
  showMoreButton1.style.cursor = "pointer";

  showMoreButton1.addEventListener("click", function () {
    codeSections1.forEach((section) => section.classList.toggle("show-more"));
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
  showMoreButton2.style.boxShadow = "0px 4px 4px 0px rgba(0, 0, 0, 0.25)";
  showMoreButton2.style.cursor = "pointer";
  showMoreButton2.addEventListener("click", function () {
    codeSections2.forEach((section) => section.classList.toggle("show-more"));
  });
  const createLabelDiv = (text) => {
    const labelDiv = document.createElement("div");
    labelDiv.innerText = text;
    labelDiv.style.marginBottom = "5px"; // Add margin-bottom to the div
    labelDiv.style.display = "flex"; // Set display to flex for horizontal layout
    labelDiv.style.justifyContent = "space-between"; // Arrange content with space-between
    labelDiv.style.alignItems = "center"; // Center align items vertically
    return labelDiv;
  };

  // Insert labels before the content set by innerText
  const timeComplexityDiv = document.getElementById("timeComplexity");
  const spanTimeComplexity = createLabelDiv("Time");
  timeComplexityDiv.parentNode.insertBefore(
    spanTimeComplexity,
    timeComplexityDiv
  );

  const spaceComplexityDiv = document.getElementById("spaceComplexity");
  const spanSpaceComplexity = createLabelDiv("Space");
  spaceComplexityDiv.parentNode.insertBefore(
    spanSpaceComplexity,
    spaceComplexityDiv
  );

  const algorithmDiv = document.getElementById("algorithm");
  const spanAlgorithm = createLabelDiv("Algorithm");
  algorithmDiv.parentNode.insertBefore(spanAlgorithm, algorithmDiv);

  // Create Hint div and set content to "Hint"
  const hintDiv = document.getElementById("hint");
  const spanHint = createLabelDiv("Hint");
  hintDiv.parentNode.insertBefore(spanHint, hintDiv);

  // Set text content using innerText
  document.getElementById("timeComplexity").innerText = data.bigO;
  document.getElementById("spaceComplexity").innerText =
    data.spaceComplexDegree;
  document.getElementById("algorithm").innerText = data.whatAlgo;
  document.getElementById("hint").innerText = "hint";
  // Apply styles to the div elements (timeComplexity, spaceComplexity, algorithm)
  [timeComplexityDiv, spaceComplexityDiv, algorithmDiv, hintDiv].forEach(
    (div) => {
      div.style.width = "50px"; // Set a fixed width
      div.style.height = "50px"; // Set a fixed height
      div.style.border = "1px solid #ccc";
      div.style.display = "flex";

      div.style.justifyContent = "center";
      div.style.alignItems = "center";
      div.style.padding = "10px";
      div.style.fontSize = "10px";
      div.style.borderRadius = "15px";
      div.style.textAlign = "center";
      div.style.marginBottom = "15px";
      div.style.boxShadow = "0px 4px 4px 0px rgba(0, 0, 0, 0.25)";
    }
  );
  // Create a function to handle the click event
  const handleHintClick = () => {
    alert("This is a hint!");
  };

  // Apply hover effect and click functionality to the hintDiv
  hintDiv.addEventListener("mouseenter", () => {
    hintDiv.style.backgroundColor = "#f0f0f0";
    hintDiv.style.scale = "1.1";
    hintDiv.style.transition = "all 0.3s ease-in";
    hintDiv.style.cursor = "pointer";
  });

  hintDiv.addEventListener("mouseleave", () => {
    hintDiv.style.backgroundColor = "transparent";
  });

  hintDiv.addEventListener("click", handleHintClick);

  // Add class to the parent container
  document.getElementById("peronsalAnalyze").classList.add("styled-text");

  // Set text and add class for other elements
  document.getElementById("bestTimeComplexityTitle").innerText =
    "bestTimeComplexity";
  document.getElementById("bestTimeComplexityCode").innerText =
    data.compareOtherBigOCode;
  document.getElementById("bestTimeComplexityWords").innerText =
    data.compareOtherBigO;
  document.getElementById("bestTimeComplexity").classList.add("styled-text");
  document.getElementById("bestSpaceComplexityTitle").innerText =
    "bestSpaceComplexity";
  document.getElementById("bestSpaceComplexityCode").innerText =
    data.compareOtherSpaceComplexDegreeCode;
  document.getElementById("bestSpaceComplexityWords").innerText =
    data.compareOtherSpaceComplexDegree;
  document.getElementById("bestSpaceComplexity").classList.add("styled-text");
}
