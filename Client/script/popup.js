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
    let lines = document.querySelector('body').innerText.split("\n");
    let codeLines = [];

    let endIdx=0;
    for (let i=11; i<lines.length; i++){
        if (lines[i].startsWith("복사다운로드")){
            endIdx=i-1;
            break;
        }
        if (lines[i].startsWith("소스 코드 공개")){
            endIdx=i-1;
            break;
        }
    }

    for (let i=10; i<=endIdx; i+=2){
        codeLines.push(lines[i]);
    }
    
    if (codeLines.length>0){
        return codeLines.join("\n");
    }
    else {
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
        displayElement.classList.add('styled-text');
    });
}