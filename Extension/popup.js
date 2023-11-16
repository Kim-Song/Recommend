document.getElementById('copyButton').addEventListener('click', function() {
  chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
    let tabId = tabs[0].id;
    chrome.scripting.executeScript({
      target: {tabId: tabId},
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
  });
});
  
document.getElementById('pasteButton').addEventListener('click', function() {
  chrome.storage.local.get('sourceData', function(data) {
    if (data && data.sourceData) {
      document.getElementById('dataDisplay').innerText = data.sourceData[0].result;
      console.log(data.sourceData[0]);
    } else {
      document.getElementById('dataDisplay').innerText = "저장된 데이터가 없습니다.";
    }
  });
});

function scrapeData() {
  let body = document.querySelector('body');
  let lines = body.innerText.split("\n");
  let filteredLines = [];
  for (let i=10; i<=lines.length-72; i+=2){ //10일 떄가 있고, 3일 때가 있음...
      filteredLines.push(lines[i]);
  }
  console.log(filteredLines);
  return filteredLines.join("\n");
}
