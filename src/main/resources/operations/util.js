// From https://github.com/IDEAS2Organization/ideas-dockerfile-module/blob/francisco/src/main/resources/get_js_code.js

function sendRequest(operationUri, data) {
  RequestHelper.ajax(operationUri, {
    type: "POST",
    data: data,
    onSuccess: async function (result) {
      console.log("onSuccess");
      console.log(operationUri);
      OperationMetrics.stop();
    },
    onProblems: async function (result) {
      console.log("onProblems");
      OperationMetrics.stop();
    },
  });
}
function closeModal() {
  $("#appGenericModal").attr("style", "display: none;");
  $("#appGenericModal").attr("class", "modal");
  $("#appGenericModal").attr("aria-hidden", "true");
  $(".modal-backdrop").remove();
}
