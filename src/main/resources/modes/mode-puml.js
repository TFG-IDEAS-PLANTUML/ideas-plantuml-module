
ace.define('ace/mode/puml', ['require', 'exports', 'module' , 'ace/tokenizer', 'ace/mode/puml_highlight_rules', 
    'ace/mode/folding/puml', 'ace/range', 'ace/mode/text', 'ace/lib/oop'], function(require, exports, module) {

var Tokenizer = require("../tokenizer").Tokenizer;
var Rules = require("./sintaxis_highlight_rules").PumlHighlightRules;
var FoldMode = require("./folding/puml").FoldMode;
var Range = require("../range").Range;
var TextMode = require("./text").Mode;
var oop = require("../lib/oop");

function Mode() {
    this.HighlightRules = Rules;
    this.foldingRules = new FoldMode();
}

oop.inherits(Mode, TextMode);

(function() {
    
    this.getNextLineIndent = function(state, line, tab) {
        var indent = this.$getIndent(line);
        return indent;
    };
    
    this.toggleCommentLines = function(state, doc, startRow, endRow){
        var range = new Range(0, 0, 0, 0);
        for (var i = startRow; i <= endRow; ++i) {
            var line = doc.getLine(i);
            if (hereComment.test(line))
                continue;
                
            if (commentLine.test(line))
                line = line.replace(commentLine, '$1');
            else
                line = line.replace(indentation, '$&#');
    
            range.end.row = range.start.row = i;
            range.end.column = line.length + 1;
            doc.replace(range, line);
        }
    };
    
    this.$id = "ace/mode/sintaxis";
}).call(Mode.prototype);

exports.Mode = Mode;

});

//Sintaxis
ace.define('ace/mode/sintaxis_highlight_rules', ['require', 'exports', 'module' , 'ace/lib/oop', 'ace/mode/text_highlight_rules'],function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var lang = require("../lib/lang");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;
var TexHighlightRules = require("./tex_highlight_rules").TexHighlightRules;
 

var PumlHighlightRules = function() {  

    this.$rules = {
        "start" : [{
            // A comment. Tex comments start with % and go to 
            // the end of the line
            token : "comment",
            regex : "%.*$"
        }, {
            // Documentclass and usepackage
            token : ["keyword", "lparen", "variable.parameter", "rparen", "lparen", "storage.type", "rparen"],
            regex : "(\\\\(?:documentclass|usepackage|input))(?:(\\[)([^\\]]*)(\\]))?({)([^}]*)(})"
        }, {
            // A label
            token : ["keyword","lparen", "variable.parameter", "rparen"],
            regex : "(\\\\(?:label|v?ref|cite(?:[^{]*)))(?:({)([^}]*)(}))?"
        }, {
            // A Verbatim block
            token : ["storage.type", "lparen", "variable.parameter", "rparen"],
            regex : "(\\\\begin)({)(verbatim)(})",
            next : "verbatim"
        },  {
            token : ["storage.type", "lparen", "variable.parameter", "rparen"],
            regex : "(\\\\begin)({)(lstlisting)(})",
            next : "lstlisting"
        },  {
            // A block
            token : ["storage.type", "lparen", "variable.parameter", "rparen"],
            regex : "(\\\\(?:begin|end))({)([\\w*]*)(})"
        }, {
            token : "storage.type",
            regex : /\\verb\b\*?/,
            next : [{
                token : ["keyword.operator", "string", "keyword.operator"],
                regex : "(.)(.*?)(\\1|$)|",
                next : "start"
            }]
        }, {
            // A tex command e.g. \foo
            token : "storage.type",
            regex : "\\\\[a-zA-Z]+"
        }, {
            // Curly and square braces
            token : "lparen",
            regex : "[[({]"
        }, {
            // Curly and square braces
            token : "rparen",
            regex : "[\\])}]"
        }, {
            // Escaped character (including new line)
            token : "constant.character.escape",
            regex : "\\\\[^a-zA-Z]?"
        }, {
            // An equation
            token : "string",
            regex : "\\${1,2}",
            next  : "equation"
        }],
        "equation" : [{
            token : "comment",
            regex : "%.*$"
        }, {
            token : "string",
            regex : "\\${1,2}",
            next  : "start"
        }, {
            token : "constant.character.escape",
            regex : "\\\\(?:[^a-zA-Z]|[a-zA-Z]+)"
        }, {
            token : "error", 
            regex : "^\\s*$", 
            next : "start" 
        }, {
            defaultToken : "string"
        }],
        "verbatim": [{
            token : ["storage.type", "lparen", "variable.parameter", "rparen"],
            regex : "(\\\\end)({)(verbatim)(})",
            next : "start"
        }, {
            defaultToken : "text"
        }],
        "lstlisting": [{
            token : ["storage.type", "lparen", "variable.parameter", "rparen"],
            regex : "(\\\\end)({)(lstlisting)(})",
            next : "start"
        }, {
            defaultToken : "text"
        }]
    };
    
    this.normalizeRules();
};

oop.inherits(PumlHighlightRules, TextHighlightRules);

exports.PumlHighlightRules = PumlHighlightRules;

});



ace.define('ace/mode/folding/puml', ['require', 'exports', 'module' , 'ace/lib/oop', 'ace/mode/folding/fold_mode', 'ace/range'], 
    function(require, exports, module) {


var oop = require("../../lib/oop");
var BaseFoldMode = require("./fold_mode").FoldMode;
var Range = require("../../range").Range;

var FoldMode = exports.FoldMode = function() {};
oop.inherits(FoldMode, BaseFoldMode);

(function() {

    this.getFoldWidgetRange = function(session, foldStyle, row) {
        var range = this.indentationBlock(session, row);
        if (range)
            return range;

        var re = /\S/;
        var line = session.getLine(row);
        var startLevel = line.search(re);
        if (startLevel == -1 || line[startLevel] != "%")
            return;

        var startColumn = line.length;
        var maxRow = session.getLength();
        var startRow = row;
        var endRow = row;

        while (++row < maxRow) {
            line = session.getLine(row);
            var level = line.search(re);

            if (level == -1)
                continue;

            if (line[level] != "%")
                break;

            endRow = row;
        }

        if (endRow > startRow) {
            var endColumn = session.getLine(endRow).length;
            return new Range(startRow, startColumn, endRow, endColumn);
        }
    };
    this.getFoldWidget = function(session, foldStyle, row) {
        var line = session.getLine(row);
        var indent = line.search(/\S/);
        var next = session.getLine(row + 1);
        var prev = session.getLine(row - 1);
        var prevIndent = prev.search(/\S/);
        var nextIndent = next.search(/\S/);

        if (indent == -1) {
            session.foldWidgets[row - 1] = prevIndent!= -1 && prevIndent < nextIndent ? "start" : "";
            return "";
        }
        if (prevIndent == -1) {
            if (indent == nextIndent && line[indent] == "#" && next[indent] == "%") {
                session.foldWidgets[row - 1] = "";
                session.foldWidgets[row + 1] = "";
                return "start";
            }
        } else if (prevIndent == indent && line[indent] == "#" && prev[indent] == "%") {
            if (session.getLine(row - 2).search(/\S/) == -1) {
                session.foldWidgets[row - 1] = "start";
                session.foldWidgets[row + 1] = "";
                return "";
            }
        }

        if (prevIndent!= -1 && prevIndent < indent)
            session.foldWidgets[row - 1] = "start";
        else
            session.foldWidgets[row - 1] = "";

        if (indent < nextIndent)
            return "start";
        else
            return "";
    };

}).call(FoldMode.prototype);

});

//Text highlight rules
ace.define('ace/mode/tex_highlight_rules', ['require', 'exports', 'module' , 'ace/lib/oop'],
    function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var lang = require("../lib/lang");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;


var PumlHighlightRules = function() {

    var keywords = (
        "across|activate|again|" +
        "allow_mixing|allowmixing|also|alt|as|autonumber|bold|bottom|box|break|caption|center|"+
        "circle|color|create|critical|dashed|deactivate|description|destroy|detach|dotted|down|"+
        "else|elseif|empty|end|endif|endwhile|false|footbox|footer|fork|group|header|hide|hnote|"+
        "if|is|italic|kill|left|left to right direction|legend|link|loop|mainframe|map|members|" +
        "namespace|newpage|normal|note|of|on|opt|order|over|package|page|par|partition|plain|ref|"+
        "repeat|return|right|rnote|rotate|show|skin|skinparam|split|sprite|start|stereotype|stop|"+
        "style|then|title|top|top to bottom direction|true|up|while|!assert|!define|!definelong|"+
        "!dump_memory|!else|!enddefinelong|!endfunction|!endif|!endprocedure|!endsub|!exit|!function|"+
        "!if|!ifdef|!ifndef|!import|!include|!local|!log|!pragma|!procedure|!return|!startsub|!theme|"+
        "!undef|!unquoted|abstract|actor|agent|annotation|archimate|artifact|boundary|card|class|cloud|"+
        "collections|component|control|database|diamond|entity|enum|file|folder|frame|hexagon|interface|"+
        ".label|node|object|package|participant|person|queue|rectangle|stack|state|storage|usecase"
    );

    var builtinConstants = (
        ""
    );

    var builtinFunctions = (
        ""
    );

    var keywordMapper = this.createKeywordMapper({
        "support.function": builtinFunctions,
        "keyword": keywords,
        "constant.language": builtinConstants
    }, "identifier", true);

    this.$rules = {
        "start" : [ {
            token : "comment",
            regex : "--.*$"
        },  {
            token : "comment",
            start : "/\\*",
            end : "\\*/"
        }, {
            token : "string",           // " string
            regex : '".*?"'
        }, {
            token : "string",           // ' string
            regex : "'.*?'"
        }, {
            token : "string",           // ` string (apache drill)
            regex : "`.*?`"
        }, {
            token : "constant.numeric", // float
            regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
        }, {
            token : keywordMapper,
            regex : "[a-zA-Z_$][a-zA-Z0-9_$]*\\b"
        }, {
            token : "keyword.operator",
            regex : "\\+|\\-|\\/|\\/\\/|%|<@>|@>|<@|&|\\^|~|<|>|<=|=>|==|!=|<>|="
        },{
            token : "storage.type",
            regex : ".*(@endboard|@endbpm|@endcreole|@endcute|@enddef|@endditaa|@enddot|@endflow|"+
        "@endgantt|@endgit|@endjcckit|@endjson|@endlatex|@endmath|@endmindmap|@endnwdiag|"+
        "@endproject|@endsalt|@endtree|@enduml|@endwbs|@endwire|@endyaml|@startboard|@startbpm|"+
        "@startcreole|@startcute|@startdef|@startditaa|@startdot|@startflow|@startgantt|@startgit|" +
        "@startjcckit|@startjson|@startlatex|@startmath|@startmindmap|@startnwdiag|@startproject|"+
        "@startsalt|@starttree|@startuml|@startwbs|@startwire|@startyaml).*"
        }, {
            token : "paren.lparen",
            regex : "[\\(]"
        }, {
            token : "paren.rparen",
            regex : "[\\)]"
        }, {
            token : "text",
            regex : "\\s+"
        } ]
    };
    this.normalizeRules();
};

oop.inherits(PumlHighlightRules, TextHighlightRules);

exports.PumlHighlightRules = PumlHighlightRules;

});
