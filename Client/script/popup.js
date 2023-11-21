document.getElementById('analyzeButton').addEventListener('click', handleButtonClick);

function handleButtonClick() {
  chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
      const currentTab = tabs[0];
      const submitNumber = document.getElementById('submitNumber').value;
      const onUpdatedCallback = function (tabId, info) {
          if (info.status === 'complete') {
              executeScriptOnTab(tabId, scrapeData, handleScriptResults);
              chrome.tabs.onUpdated.removeListener(onUpdatedCallback);
          }
      };

      chrome.tabs.onUpdated.addListener(onUpdatedCallback);
      chrome.tabs.update({ url: "https://www.acmicpc.net/source/" + submitNumber });
  });
}

function executeScriptOnTab(tabId, scriptFunction, callback) {
    chrome.scripting.executeScript({
        target: { tabId: tabId },
        func: scriptFunction
    }, callback);
}

function handleScriptResults(results) {
    if (chrome.runtime.lastError) {
         console.error(chrome.runtime.lastError);
         return;
    }

    if (results && results[0]) {
        storeResultsInLocalStorage(results);
        showData();
    }
}

function storeResultsInLocalStorage(results) {
    chrome.storage.local.set({ sourceData: results });
}

function scrapeData() {
    const body = document.querySelector('body');
    const lines = body.innerText.split("\n");
    const filteredLines = [];

    for (let i = 10; i <= lines.length - 72; i += 2) {
        filteredLines.push(lines[i]);
    }

    return filteredLines.join("\n");
}

function showData() {
    chrome.storage.local.get('sourceData', function (data) {
        const displayElement = document.getElementById('dataDisplay');
        
        if (data && data.sourceData) {
            displayElement.innerText = data.sourceData[0].result[0] === '#' ? data.sourceData[0].result : "숫자만 나옴!! DOM 확인해!!";
        } else {
            displayElement.innerText = "저장된 데이터가 없습니다.";
        }
    });
}