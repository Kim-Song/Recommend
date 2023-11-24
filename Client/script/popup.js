document.getElementById('analyzeButton').addEventListener('click', analyzeButtonHandler);

function analyzeButtonHandler() {
    chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
        const currentTab = tabs[0];
        const submitNumber = document.getElementById('submitNumber').value;
          
        const onUpdatedCallback = function (tabId, info) {
              if (info.status === 'complete') {
                  chrome.tabs.onUpdated.removeListener(onUpdatedCallback);
                  executeScriptOnTab(currentTab.id, scrapeTargetData, handleScriptResults);
              }
          };
          chrome.tabs.onUpdated.addListener(onUpdatedCallback);
  
          const targetURL = "https://www.acmicpc.net/source/" + submitNumber ;
          if (currentTab.url !== targetURL){
              chrome.tabs.update({ url: targetURL});
          }  
          else {
            executeScriptOnTab(currentTab.id, scrapeTargetData, handleScriptResults);
          }
      
    });
  }

function executeScriptOnTab(tabId, scriptFunction, callback) {
    chrome.scripting.executeScript({
        target: { tabId: tabId },
        func: scriptFunction
    }, callback);
}

function scrapeTargetData(){
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
    
    if (codeLines.length==0){
        return "접근할 수 없는 제출 번호입니다.";
    }

    else {
        let targetData = [];
        targetData.push(codeLines.join("\n"));
        targetData.push(document.querySelector('tbody').innerText.split("\t")[5]);
        targetData.push(document.querySelector('tbody').innerText.split("\t")[6]);
        targetData.push(document.querySelector('tbody').innerText.split("\t")[7]);
        return targetData.join("\t");
    }
}

function handleScriptResults(results) {
    if (chrome.runtime.lastError) {
         console.error(chrome.runtime.lastError);
         return;
    }

    console.log(results[0].result.split("\t"));

    if (results && results[0]) {
        let targetData = results[0].result.split("\t");
        chrome.storage.local.set({ 
            code: targetData[0],
            memoryUsage: targetData[1],
            timeUsage: targetData[2], 
            language: targetData[3],
        });
        showCode();
    }
}

function showCode() {
    chrome.storage.local.get(null, function (data) {
        const displayElement = document.getElementById('dataDisplay');
        if (data && data.code) {
            displayElement.innerText=data.code+"\n\n"+data.memoryUsage+"\n\n"+data.timeUsage+"\n\n"+data.language;
        } 
        displayElement.classList.add('styled-text');
    });
}