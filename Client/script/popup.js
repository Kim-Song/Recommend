document.getElementById('copyButton').addEventListener('click', function() {
  chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
    let currentTabId = tabs[0].id;
    let tabUrl = tabs[0].url;
    let targetUrl = "https://www.acmicpc.net/status"
    //https://www.acmicpc.net/status?from_mine=1&problem_id=13414&user_id=trankill1127
    //좀 더 엄격하게 처리하면 좋겠지만?? 일단은 대충

    if (tabUrl.includes(targetUrl)){
      chrome.scripting.executeScript({
        target: {tabId: currentTabId},
        func: scrapeSubmitNumber
      }, function(results){

        let moveUrl = "https://www.acmicpc.net/source/"+results[0].result;
        chrome.tabs.update({url:moveUrl});

        chrome.scripting.executeScript({
          target: {tabId: currentTabId},
          func: scrapeData
        }, function(results) {
          if (results && results[0]) {
            chrome.storage.local.set({sourceData: results});
            showData();
          }
        }); 
      })
    }
    else {
      alert("THIS EXTENSION ONLY WORKS ON BOJ MY SUBMIT PAGES!!")
    }
  });
});

function scrapeSubmitNumber(){
  let body = document.querySelector('tbody');
  let lines = body.innerText.split("\t");
  let submitNumber = lines[0];

  console.log(submitNumber);

  return submitNumber;
}

function scrapeData() {
  let body = document.querySelector('tbody');
  let lines = body.innerText.split("\n");
  let filteredLines = [];

  for (let i=10; i<=lines.length-72; i+=2){ //숫자만 나오는 에러 생기면 여기 수정해야 함.
    filteredLines.push(lines[i]);
  }
  return filteredLines.join("\n");
}

function showData(){
  chrome.storage.local.get('sourceData', function(data) {
    if (data && data.sourceData) {
      if (data.sourceData[0].result[0]=='#'){
        document.getElementById('dataDisplay').innerText = data.sourceData[0].result;
      }
      else {
        document.getElementById('dataDisplay').innerText = "숫자만 나옴!! DOM 확인해!!";
      }
    } else {
      document.getElementById('dataDisplay').innerText = "저장된 데이터가 없습니다.";
    }
  });
}