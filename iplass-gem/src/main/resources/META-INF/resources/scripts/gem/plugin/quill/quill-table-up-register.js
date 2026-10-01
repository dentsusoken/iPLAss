import TableUp, { defaultCustomSelect, TableSelection, TableMenuContextmenu, TableResizeLine } from "quill-table-up";
const quill = window.Quill;

if (!quill) {
	throw new Error("Quill UMD must be loaded before quill-table-up.");
}

quill.register({ [`modules/${TableUp.moduleName}`]: TableUp }, true);
window.scriptContext.gem.richtext.quill.tableUp = { TableUp, defaultCustomSelect, TableSelection, TableMenuContextmenu, TableResizeLine };