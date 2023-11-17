document.getElementById('copyButton').addEventListener('click', function() {
  chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
    let currentTabId = tabs[0].id;
    let tabUrl = tabs[0].url;
    let targetUrl = "https://www.acmicpc.net/source/"

    if (tabUrl.includes(targetUrl)){
      chrome.scripting.executeScript({
        target: {tabId: currentTabId},
        func: scrapeData
      }, function(results) {
        if (chrome.runtime.lastError) {
          console.error(chrome.runtime.lastError);
          return;
        }

        if (results && results[0]) {
          chrome.storage.local.set({sourceData: results});
        }
      });
    }

    else {
      alert("THIS EXTENSION ONLY WORKS ON BOJ SOURCE PAGES!!");
    }
    
  });
});
  
document.getElementById('pasteButton').addEventListener('click', function() {
  chrome.storage.local.get('sourceData', function(data) {
    if (data && data.sourceData) {
      if (data.sourceData[0].result[0]=='#'){
        document.getElementById('dataDisplay').innerText = data.sourceData[0].result;
        console.log(data.sourceData[0]);
      }
      else {
        document.getElementById('dataDisplay').innerText = "숫자만 나옴!! DOM 확인해!!";
      }
      
    } else {
      document.getElementById('dataDisplay').innerText = "저장된 데이터가 없습니다.";
    }
  });
});

function scrapeData() {
  let body = document.querySelector('body');
  let lines = body.innerText.split("\n");
  let filteredLines = [];
  for (let i=10; i<=lines.length-72; i+=2){ //숫자만 나오는 에러 생기면 여기 수정해야 함.
      filteredLines.push(lines[i]);
  }
  console.log(filteredLines);
  return filteredLines.join("\n");
}
