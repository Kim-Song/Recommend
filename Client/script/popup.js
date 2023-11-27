document.getElementById('analyzeButton').addEventListener('click', analyzeButtonHandler);

function analyzeButtonHandler() {
    chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
        const currentTab = tabs[0];
        submitNumber = document.getElementById('submitNumber').value;
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
    //코드
    let lines = document.querySelector('body').innerText.split("\n");
    let codeLines = [];

    let startIdx=0;
    for (let i=0; i<lines.length; i++){
        if (lines[i].startsWith("1")){
            startIdx=i+1;
            break;
        }
    }

    let endIdx=0;
    for (let i=startIdx+1; i<lines.length; i++){
        if (lines[i].startsWith("복사다운로드")){
            endIdx=i-1;
            break;
        }
        if (lines[i].startsWith("소스 코드 공개")){
            endIdx=i-1;
            break;
        }
    }

    for (let i=startIdx; i<=endIdx; i+=2){
        codeLines.push(lines[i]);
    }
    
    //접근할 수 없는 코드의 경우
    if (codeLines.length==0){
        return "접근할 수 없는 제출 번호입니다.";
    }

    //접근할 수 있는 코드의 경우
    else {
        let targetData = [];
        targetData.push(codeLines.join("\n"));
        targetData.push(document.querySelector('tbody').innerText.split("\t")[2]);

        //맞았습니다!!
        const submitResult = document.querySelector('tbody').innerText.split("\t")[4]; 
        if (submitResult=="맞았습니다!!"){
            targetData.push(document.querySelector('tbody').innerText.split("\t")[5]);
            targetData.push(document.querySelector('tbody').innerText.split("\t")[6]);
            targetData.push(document.querySelector('tbody').innerText.split("\t")[7]);
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

        if (targetData.length()==5){ //맞았습니다!!
            const requestData = {
                "number": targetData[1],
                "time": targetData[3],
                "memory": targetData[2],
                "language": targetData[4],
                "code": targetData[0]
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
        }
        else { //그 와
            const requestData = {
                "number": targetData[1],
                "code": targetData[0]
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

function handleScriptResults2(results){
    console.log(results[0]);

    if (result){
        console.log("code");
    }
    else {
        console.log("hint");
    }
}

function showResponse(data) { //힌트 api response 보고 수정해줘야 함
    
    document.getElementById('timeComplexity').innerText = data.bigO;
    document.getElementById('spaceComplexity').innerText = data.spaceComplexDegree;
    document.getElementById('algorithm').innerText = data.whatAlgo;
    document.getElementById('peronsalAnalyze').classList.add('styled-text');

    document.getElementById('bestTimeComplexityCode').innerText = data.compareOtherBigOCode;
    document.getElementById('bestTimeComplexityWords').innerText = data.compareOtherBigO;
    document.getElementById('bestTimeComplexity').classList.add('styled-text');

    document.getElementById('bestSpaceComplexityCode').innerText = data.compareOtherSpaceComplexDegreeCode;
    document.getElementById('bestSpaceComplexityWords').innerText = data.compareOtherSpaceComplexDegree;  
    document.getElementById('bestSpaceComplexity').classList.add('styled-text');
    
}