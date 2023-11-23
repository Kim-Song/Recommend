document.getElementById('analyzeButton').addEventListener('click', handleButtonClick);

function handleButtonClick() {
  chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
      const submitNumber = document.getElementById('submitNumber').value;
      const onUpdatedCallback = function (tabId, info) {
          if (info.status === 'complete') {
              executeScriptOnTab(tabId, scrapeCode, handleScriptResults);
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

function scrapeCode() {
    const lines = document.querySelector('body').innerText.split("\n");
    const codeLines = [];

    if (lines[10].charAt(0)==="#"){
        for (let i = 10; i <= lines.length - 72; i += 2) {
            codeLines.push(lines[i]);
        }
        return codeLines.join("\n");
    } else {
        return "접근할 수 없는 제출 번호입니다.";
    }
}

function handleScriptResults(results) {
    if (chrome.runtime.lastError) {
         console.error(chrome.runtime.lastError);
         return;
    }

    if (results && results[0]) {
        chrome.storage.local.set({ sourceData: results[0].result });
        showCode();
    }
}

function showCode() {
    chrome.storage.local.get('sourceData', function (data) {
        const displayElement = document.getElementById('dataDisplay');
        if (data && data.sourceData) {
            displayElement.innerText=data.sourceData;
        } 
    });
    chrome.storage.local.get('idData', function (data) {
        const displayElement = document.getElementById('idDisplay');
        if (data && data.idData) {
            displayElement.innerText=data.idData;
        } 
    });
}