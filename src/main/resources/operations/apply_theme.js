try {

    function getSelectForm() {
        var html = "<div id='show_with_theme'><select id='options_theme' onchange='selectChange()'>" + createOptions(operationStructure.data['themes']) + "</select></div>";
        return html;
    }

    function generateDiagram(theme, content) {

        var split = content.split("\n")

        split.splice(1, 0, "!theme " + theme + "\n")

        operationUri =
            ModeManager.getBaseUri(
                ModeManager.calculateModelIdFromExt(
                    ModeManager.calculateExtFromFileUri(fileUri)
                )
            ) + DEPRECATED_EXEC_OP_URI.replace("$opId", "generate_console");


        var formData = new FormData();
        formData.append('content', split.join("\n"));
        formData.append('fileUri', fileUri);
        formData.append('id', "generate_console");

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            url: operationUri,
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function(data) {
                $("#base64PumlDiagram").remove();
                document.getElementById("show_with_theme").innerHTML += data.htmlMessage;
                $("#options_theme").val(theme);
            },
            error: function(e) {
                document.write('<div>' + e + '</div>');
            }
        });

    }

    function createOptions(themes) {

        var result = "";

        for (var theme of themes.split("#")) {
            result += "<option value='" + theme + "'>" + theme + "</option>\n";
        }

        return result;
    }

    operationId = operationStructure.id;
    var data = {};
    data.fileUri = fileUri;
    data.content = EditorManager.getEditorContentByUri(fileUri);
    data.id = operationId;
    data.username = principalUser;

    // Devuelve 'http://localhost:8081/ideas-plantuml-language/language/operation/$opId/execute'
    operationUri =
        ModeManager.getBaseUri(
            ModeManager.calculateModelIdFromExt(
                ModeManager.calculateExtFromFileUri(fileUri)
            )
        ) + DEPRECATED_EXEC_OP_URI.replace("$opId", operationId);

    function selectChange() {

        var theme = document.getElementById("options_theme").value;

        var html = getSelectForm();

        if(data.content.includes("!theme")){

            var split = data.content.split("\n");

            for(i = 0; i< split.length; i++){
                        if(split[i].includes("!theme")) split[i] = "";
                    }
            data.content = split.join("\n");
        }

        html += generateDiagram(theme, data.content);

    }

    showModal(
        "Choose a theme",
        getSelectForm(),
        "Ok",
        function() {
            theme = $("#options_theme").find(":selected").val();
            var split = data.content.split("\n");
            split.splice(1, 0, "!theme " + theme + "\n");
            data.theme = theme;
            OperationMetrics.play(operationId);
            sendRequest(operationUri, data);
            FileApi.saveFileContents(fileUri, split.join("\n"), function(result){
                window.location.reload();
            });
            closeModal();
        },
        closeModal,
        ""
    );

} catch (error) {
    console.error(error);
}