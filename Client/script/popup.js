document.getElementById('analyzeButton').addEventListener('click', handleButtonClick);

function handleButtonClick() {
  chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
      const currentTab = tabs[0];
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

function handleScriptResults(results) {
    if (chrome.runtime.lastError) {
         console.error(chrome.runtime.lastError);
         return;
    }

    if (results && results[0]) {
        chrome.storage.local.set({ sourceData: results });
        showCode();
    }
}

function scrapeUserId(){
    const userId = document.querySelector('ul > li > a').innerText;
    return userId;
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

function showCode() {
    chrome.storage.local.get('sourceData', function (data) {
        const displayElement = document.getElementById('dataDisplay');
        if (data && data.sourceData) {
            displayElement.innerText=data.sourceData[0].result;
        } 
    });
}