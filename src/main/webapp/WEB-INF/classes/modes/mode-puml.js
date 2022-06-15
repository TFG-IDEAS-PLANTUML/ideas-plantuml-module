
ace.define('ace/mode/puml', ['require', 'exports', 'module' ,'ace/mode/text', 'ace/lib/oop'], function(require, exports, module) {

var Rules = require("./sintaxis_highlight_rules").PumlHighlightRules;

var TextMode = require("./text").Mode;
var oop = require("../lib/oop");

function Mode() {
    this.HighlightRules = Rules;
}

oop.inherits(Mode, TextMode);

(function() {
    this.$id = "ace/mode/sintaxis";
}).call(Mode.prototype);

exports.Mode = Mode;

});


ace.define('ace/mode/sintaxis_highlight_rules', ['require', 'exports', 'module' , 'ace/lib/oop', 'ace/mode/text_highlight_rules'],function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var lang = require("../lib/lang");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var PumlHighlightRules = function() {

    var keywordMapper = this.createKeywordMapper({

                "keyword": "if|else|do|while|endif|endwhile|then|package|theme|" +
                           "skinparam|left|right|document|componentDiagram|" +
                           "class|Annotation|alt|as|note|end",
                "entity.name.function.activity": "start|stop|cloud|actor|"+
                           "component|node|agent|rectangle|query|rectangle|" +
                           "string|int|bool|double|float"

            }, "identifier");

            this.$rules = {
                "start": [
                /**{
                    token : "variable", // single line
                    regex : ".*(as).*"
                }
                ,**/
                {
                    token : "string", // single line
                    regex : '"',
                    next  : "string"
                },
                {
                    token: "comment",
                    regex: ".*(' ).*"
                }, {
                    token: "comment.start",
                    regex: ".*(\\/' ).*",
                    next: "comment"
                },
                {
                        token : "text",
                        regex : "\\s+"
                    },
                {
                        token : keywordMapper,
                        regex : "\\b\\w+\\b"
                },
                {

                    token: "storage.type",
                    regex: ".*(@).*"
                }
                ],
                "string": [{
                        token : "string",
                        regex : '"|$',
                        next  : "start"
                    }, {
                        defaultToken : "string"
                    }],
                "comment": [{
                    token: "comment.end",
                    regex: ".*('\\/).*",
                    next: "start"
                }, {
                    defaultToken: "comment"
                }]
            };


    this.normalizeRules();
};

oop.inherits(PumlHighlightRules, TextHighlightRules);

exports.PumlHighlightRules = PumlHighlightRules;

});