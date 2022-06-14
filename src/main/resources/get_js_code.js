// From https://github.com/IDEAS2Organization/ideas-dockerfile-module/blob/francisco/src/main/resources/get_js_code.js

function getCode(operationStructure, fileUri) {
  if (document.getElementById("plantuml-module-data"))
    document.getElementById("plantuml-module-data").remove();
  var s = document.createElement("script"); // Crea un elemento <script> en el documento
  s.type = "application/javascript";
  s.id = "plantuml-module-data";
  // Se definen las variables de JS en el documento para que puedan ser usados después por el segundo script que se crea
  s.text =
    "var operationStructure = " +
    JSON.stringify(operationStructure) +
    ";var fileUri = '" +
    fileUri +
    "';";
  document.body.appendChild(s);

  if (document.getElementById("plantuml-module-action"))
    document.getElementById("plantuml-module-action").remove();

  var s1 = document.createElement("script");
  s1.type = "application/javascript";
  s1.id = "plantuml-module-action";

  // Devuelve 'http://localhost:8081/ideas-plantuml-language/language/operation/$opId/javascript'
  s1.src =
    ModeManager.getBaseUri(
      ModeManager.calculateModelIdFromExt(
        ModeManager.calculateExtFromFileUri(fileUri)
      )
    ) +
    "/language/operation/" +
    operationStructure.id +
    "/javascript";
  document.body.appendChild(s1);

  if (!document.getElementById("plantuml-module-util")) {
    var s2 = document.createElement("script");
    s2.type = "application/javascript";
    s2.id = "plantuml-module-util";
    s2.src =
      ModeManager.getBaseUri(
        ModeManager.calculateModelIdFromExt(
          ModeManager.calculateExtFromFileUri(fileUri)
        )
      ) + "/language/operation/util/javascript";
    document.body.appendChild(s2);
  }
}
