chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
  const currentTab = tabs[0];

  if (currentTab && currentTab.url.includes("https://www.acmicpc.net")) {
    chrome.scripting.executeScript(
      {
        target: { tabId: currentTab.id },
        func: scrapeUserId,
      },
      function (results) {
        if (chrome.runtime.lastError) {
          console.error(chrome.runtime.lastError);
          return;
        }
        if (results && results[0]) {
          chrome.storage.local.set({ idData: results[0].result });
          showUserId();
        }
      }
    );
  }
});

function scrapeUserId() {
  const userId = document.querySelector("ul > li > a").innerText;
  if (userId === "회원가입") {
    chrome.tabs.update({ url: "https://www.acmicpc.net/login" });
  }
  return userId;
}

function showUserId() {
  chrome.storage.local.get("idData", function (data) {
    const displayElement = document.getElementById("idDisplay");
    if (data) {
      displayElement.innerText = data.idData;
    }
  });
}
