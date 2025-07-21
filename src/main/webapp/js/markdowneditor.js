marked.setOptions({
    breaks: true,
    gfm: true,
    smartLists: true,
    smartypants: true,
});

function updatePreview() {
    const text = document.getElementById("editor").value;
    document.getElementById("preview").innerHTML = marked.parse(text);
}
window.onload = updatePreview;

function wrap(tag) {
    const textarea = document.getElementById("editor");
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const selected = textarea.value.substring(start, end);
    const newText = tag + selected + tag;
    textarea.setRangeText(newText, start, end, "end");
    updatePreview();
}

function insertLink() {
    const url = prompt("Enter URL:");
    if (!url)
        return;
    const text = prompt("Enter link text:") || "text";
    const textarea = document.getElementById("editor");
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const markdown = "[" + text + "](" + url + ")";
    textarea.setRangeText(markdown, start, end, "end");
    updatePreview();
}

function insertImage() {
    const url = prompt("Enter image URL:");
    if (!url)
        return;
    const alt = prompt("Enter alt text (optional):") || "image";
    const textarea = document.getElementById("editor");
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const markdown = "![" + alt + "](" + url + ")";
    textarea.setRangeText(markdown, start, end, "end");
    updatePreview();
}

function insertHeading() {
    const textarea = document.getElementById("editor");
    const lineStart =
            textarea.value.lastIndexOf("\n", textarea.selectionStart - 1) + 1;
    textarea.setRangeText("# ", lineStart, lineStart, "end");
    updatePreview();
}

function insertBlockquote() {
    const textarea = document.getElementById("editor");
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const selected = textarea.value.substring(start, end);
    const markdown = "> " + selected.replace(/\n/g, "\n> ");
    textarea.setRangeText(markdown, start, end, "end");
    updatePreview();
}

function insertHr() {
    const textarea = document.getElementById("editor");
    const pos = textarea.selectionStart;
    const markdown = "\n\n---\n\n";
    textarea.setRangeText(markdown, pos, pos, "end");
    updatePreview();
}

function insertList(prefix) {
    const textarea = document.getElementById("editor");
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const selected = textarea.value.substring(start, end);
    const lines = selected.split("\n");
    const modified = lines.map((line) => prefix + line).join("\n");
    textarea.setRangeText(modified, start, end, "end");
    updatePreview();
}

let isSyncingEditorScroll = false;
let isSyncingPreviewScroll = false;

const editor = document.getElementById("editor");
const preview = document.getElementById("preview");

editor.addEventListener("scroll", () => {
    if (isSyncingEditorScroll)
        return;
    isSyncingPreviewScroll = true;
    const ratio =
            editor.scrollTop / (editor.scrollHeight - editor.clientHeight);
    preview.scrollTop = ratio * (preview.scrollHeight - preview.clientHeight);
    isSyncingPreviewScroll = false;
});

preview.addEventListener("scroll", () => {
    if (isSyncingPreviewScroll)
        return;
    isSyncingEditorScroll = true;
    const ratio =
            preview.scrollTop / (preview.scrollHeight - preview.clientHeight);
    editor.scrollTop = ratio * (editor.scrollHeight - editor.clientHeight);
    isSyncingEditorScroll = false;
});
